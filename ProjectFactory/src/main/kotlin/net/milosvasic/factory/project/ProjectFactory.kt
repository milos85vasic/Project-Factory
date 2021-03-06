package net.milosvasic.factory.project

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import getHome
import net.milosvasic.factory.authorization.Credential
import net.milosvasic.factory.configuration.Configuration
import net.milosvasic.factory.content.Labels
import net.milosvasic.factory.content.Messages
import net.milosvasic.factory.dependency.Classpath
import net.milosvasic.factory.exception.DirectoryCreationException
import net.milosvasic.factory.exception.GitInitializationException
import net.milosvasic.factory.generators.BuildScriptsFactory
import net.milosvasic.factory.generators.GitignoreFactory
import net.milosvasic.factory.module.Module
import net.milosvasic.factory.utils.OS
import net.milosvasic.factory.utils.Zip
import net.milosvasic.logger.SimpleLogger
import java.io.BufferedInputStream
import java.io.File
import java.net.URL

abstract class ProjectFactory {

    protected val gson = Gson()
    protected val logger = SimpleLogger()
    protected abstract val workingFolderName: String
    protected val gitignoreFactory = GitignoreFactory()
    protected val buildGradleFactory = BuildScriptsFactory()

    fun create(json: File): Boolean {
        val jsonConfiguration = json.readText()
        val project: Project
        try {
            project = gson.fromJson<Project>(jsonConfiguration)
        } catch (e: Exception) {
            logger.e("", "${Labels.ERROR.toUpperCase()} [ ${e.message} ]")
            return false
        }
        val home = getHome(workingFolderName)
        val gradleHome = initLocalGradleDistribution(home)
        val name = project.name.replace(" ", "_")
        val destination = File(home.absolutePath, name)
        if (destination.exists()) throw IllegalStateException(
                "${Messages.PROJECT_ALREADY_EXIST}: ${destination.absolutePath}"
        )
        if (destination.mkdirs()) {
            initRootDirectory(project, destination)
            project.modules?.forEach {
                module ->
                val moduleName = module.name.replace(" ", "_")
                val moduleDirectory = File(destination.absolutePath, moduleName)
                if (!moduleDirectory.exists()) {
                    if (!moduleDirectory.mkdirs()) throw DirectoryCreationException(moduleDirectory)
                }
                initModule(project, module, destination)
            }

        } else throw DirectoryCreationException(destination)
        return runGradle(gradleHome, destination)
    }

    protected abstract fun getModuleNonJavaDirectories(module: Module, root: File): List<File>

    protected abstract fun getClasspath(project: Project): Classpath

    private fun initRootDirectory(project: Project, root: File) {
        initGitRepository(project, root)
        createChangelog(root)
        createBuildGradle(root)
        createGitignore(root)
        createSettingsGradle(project, root)
    }

    private fun initModule(project: Project, module: Module, root: File) {
        createGitignore(module, root)
        createCredentials(project, module, root)
        createSrcStructure(module, root)
        createBuildGradle(project, module, root)
    }

    private fun createChangelog(root: File) {
        val localFile = File(root.absolutePath, "CHANGELOG.md")
        if (!localFile.exists()) {
            logger.v("", Messages.INITIALIZING(localFile.name))
            localFile.appendText("# TBD\n\n")
            localFile.appendText("- TBD.\n")
            logger.v("", Messages.INITIALIZED(localFile.name))
        } else {
            logger.w("", Messages.FILE_ALREADY_EXIST(localFile))
        }
    }

    private fun createBuildGradle(root: File) {
        val localFile = File(root.absolutePath, "build.gradle")
        if (!localFile.exists()) {
            logger.v("", Messages.INITIALIZING(localFile.name))
            localFile.appendText(buildGradleFactory.build())
            logger.v("", Messages.INITIALIZED(localFile.name))
        } else {
            logger.w("", Messages.FILE_ALREADY_EXIST(localFile))
        }
    }

    private fun createBuildGradle(project: Project, module: Module, root: File) {
        val name = module.name.replace(" ", "_")
        val localFile = File("${root.absolutePath}${File.separator}$name", "build.gradle")
        if (!localFile.exists()) {
            logger.v("", Messages.INITIALIZING("$name${File.separator}${localFile.name}"))
            val classpath = getClasspath(project)
            classpath.dependencies.add(Configuration.groot.getDependency())
            localFile.appendText(buildGradleFactory.build(project, module, classpath))
            logger.v("", Messages.INITIALIZED("$name${File.separator}${localFile.name}"))
        } else {
            logger.w("", Messages.FILE_ALREADY_EXIST(localFile))
        }
    }

    private fun createGitignore(root: File) {
        val localFile = File(root.absolutePath, ".gitignore")
        if (localFile.exists()) {
            if (!localFile.delete()) {
                throw IllegalStateException(Messages.COULD_NOT_DELETE_FILE(localFile))
            }
        }
        logger.v("", Messages.INITIALIZING(localFile.name))
        localFile.appendText(gitignoreFactory.build())
        logger.v("", Messages.INITIALIZED(localFile.name))
    }

    private fun createGitignore(module: Module, root: File) {
        val name = module.name.replace(" ", "_")
        val localFile = File("${root.absolutePath}${File.separator}$name", ".gitignore")
        if (localFile.exists()) {
            if (!localFile.delete()) {
                throw IllegalStateException(Messages.COULD_NOT_DELETE_FILE(localFile))
            }
        }
        logger.v("", Messages.INITIALIZING("$name${File.separator}${localFile.name}"))
        localFile.appendText(gitignoreFactory.build(module))
        logger.v("", Messages.INITIALIZED("$name${File.separator}${localFile.name}"))
    }

    private fun createSettingsGradle(project: Project, root: File) {
        val localFile = File(root.absolutePath, "settings.gradle")
        if (!localFile.exists()) {
            logger.v("", Messages.INITIALIZING(localFile.name))
            localFile.appendText("include ")
            project.modules?.forEachIndexed {
                index, module ->
                val settingsModule = "':${module.name.replace(" ", "_")}'"
                if (index > 0) {
                    localFile.appendText(", $settingsModule")
                } else {
                    localFile.appendText(" $settingsModule")
                }
            }
            logger.v("", Messages.INITIALIZED(localFile.name))
        } else {
            logger.w("", Messages.FILE_ALREADY_EXIST(localFile))
        }
    }

    private fun createCredentials(project: Project, module: Module, root: File) {
        if (module.credentials != null && !module.credentials.isEmpty()) {
            module.credentials.forEach {
                credential ->
                createCredential(credential, module, root)
            }
            return
        }
        if (project.credentials != null && !project.credentials.isEmpty()) {
            project.credentials.forEach {
                credential ->
                createCredential(credential, module, root)
            }
        }
    }

    private fun createCredential(credential: Credential, module: Module, root: File) {
        val name = module.name.replace(" ", "_")
        var credentialsName = "credentials.gradle"
        if (credential.name != Labels.DEFAULT.toLowerCase()) {
            credentialsName = "credentials_${credential.name}.gradle"
        }
        val localFile = File("${root.absolutePath}${File.separator}$name", credentialsName)
        if (!localFile.exists()) {
            logger.v("", Messages.INITIALIZING("$name${File.separator}${localFile.name}"))
            localFile.appendText("ext.ftpServer = \"${credential.ftp.server}\"\n")
            localFile.appendText("ext.ftpUsername = \"${credential.ftp.username}\"\n")
            localFile.appendText("ext.ftpPassword = \"${credential.ftp.password}\"\n")
            logger.v("", Messages.INITIALIZED("$name${File.separator}${localFile.name}"))
        } else {
            logger.w("", Messages.FILE_ALREADY_EXIST(localFile))
        }
    }

    private fun createSrcStructure(module: Module, root: File) {
        val name = module.name.replace(" ", "_")
        val localFile = File("${root.absolutePath}${File.separator}$name${File.separator}src")
        val main = File("${localFile.absolutePath}${File.separator}main")
        val test = File("${localFile.absolutePath}${File.separator}test")
        val mainJava = File("${main.absolutePath}${File.separator}java")
        val testava = File("${test.absolutePath}${File.separator}java")
        val directories = mutableListOf(localFile, main, test, mainJava, testava)
        directories.addAll(getModuleNonJavaDirectories(module, root))

        var packageDirectoryJava = mainJava.absolutePath
        var packageDirectoryTest = testava.absolutePath
        module.group.split(".").forEach {
            packageElement ->
            packageDirectoryJava += File.separator + packageElement
            packageDirectoryTest += File.separator + packageElement
        }
        packageDirectoryJava += File.separator + module.pPackage
        packageDirectoryTest += File.separator + module.pPackage
        directories.add(File(packageDirectoryJava))
        directories.add(File(packageDirectoryTest))

        directories.forEach {
            dir ->
            initializeDirectory(dir)
        }
    }

    private fun initializeDirectory(localFile: File) {
        if (!localFile.exists()) {
            logger.v("", Messages.INITIALIZING(localFile.absolutePath))
            if (!localFile.mkdirs()) throw IllegalStateException(Messages.INITIALIZATION_FAILED(localFile.absolutePath))
            logger.v("", Messages.INITIALIZED(localFile.absolutePath))
        } else {
            logger.w("", Messages.FILE_ALREADY_EXIST(localFile))
        }
    }

    private fun initLocalGradleDistribution(home: File): File {
        val zip = Zip()
        val zipFile = "gradle-${Configuration.gradleVersion}-bin.zip"
        val destination = File(home.absolutePath, ".gradle")
        if (!destination.exists()) {
            destination.mkdirs()
        }
        val location = "${Configuration.distributions}/gradle/$zipFile"
        val zipFileDestination = File(destination.absolutePath, zipFile)
        if (!zipFileDestination.exists()) {
            logger.v("", Messages.RETRIEVING(location))
            val url = URL(location)
            val input = url.openConnection().getInputStream()
            val bufferedInput = BufferedInputStream(input)
            zipFileDestination.writeBytes(bufferedInput.readBytes())
            logger.v("", Messages.EXTRACTING(zipFileDestination.absolutePath))
            zip.unzip(zipFileDestination, destination)
        } else {
            logger.v("", Messages.LOCAL_GRADLE_DISTRIBUTION_ALREADY_AVAILABLE(Configuration.gradleVersion))
        }
        return destination
    }

    private fun runGradle(gradleHome: File, root: File): Boolean {
        val version = Configuration.gradleVersion
        logger.v("", Messages.INITIALIZING("Gradle Wrapper $version"))
        var gradleExecutable = "gradle"
        if (OS.isWindows()) {
            gradleExecutable += ".bat"
        }
        val gradleExecutablePath = String.format(
                "%s%s%s%s%s%s%s",
                gradleHome.absolutePath,
                File.separator,
                "gradle-$version",
                File.separator,
                "bin",
                File.separator,
                gradleExecutable
        )
        val gradleExecutableFile = File(gradleExecutablePath)
        gradleExecutableFile.setExecutable(true)
        var pb = ProcessBuilder(
                gradleExecutablePath,
                "wrapper",
                "--gradle-version",
                version
        )
        pb.directory(root)
        if (pb.start().waitFor() != 0) {
            return false
        }
        logger.v("", Messages.INITIALIZED("Gradle Wrapper $version"))
        logger.v("", Messages.GRADLE(Labels.GRADLE_CLEAN))
        pb = ProcessBuilder("./gradlew", "clean")
        pb.directory(root)
        if (pb.start().waitFor() != 0) {
            return false
        }
        logger.v("", Messages.GRADLE(Labels.GRADLE_ASSEMBLE))
        pb = ProcessBuilder("./gradlew", "assemble")
        pb.directory(root)
        if (pb.start().waitFor() != 0) {
            return false
        }
        logger.v("", Messages.GRADLE(Labels.GRADLE_TEST))
        pb = ProcessBuilder("./gradlew", "test")
        pb.directory(root)
        if (pb.start().waitFor() != 0) {
            return false
        }
        return true
    }

    private fun initGitRepository(project: Project, root: File) {
        if (project.git != null) {
            logger.v("", Messages.INITIALIZING(Labels.GIT_REPOSITORY))
            val pb = ProcessBuilder("git", "clone", project.git.cloneUrl, "./")
            pb.directory(root)
            if (pb.start().waitFor() != 0) {
                throw GitInitializationException()
            }
            logger.v("", Messages.INITIALIZED(Labels.GIT_REPOSITORY))
        }
    }

}