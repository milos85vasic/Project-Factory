package net.milosvasic.factory.project

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import getHome
import net.milosvasic.factory.content.Labels
import net.milosvasic.factory.content.Messages
import net.milosvasic.factory.exception.DirectoryCreationException
import net.milosvasic.factory.generators.BuildScriptsFactory
import net.milosvasic.factory.generators.GitignoreFactory
import net.milosvasic.logger.SimpleLogger
import java.io.File

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
        val name = project.name.replace(" ", "_")
        val destination = File(home.absolutePath, name)
        if (destination.exists()) throw IllegalStateException(
                "${Messages.PROJECT_ALREADY_EXIST}: ${destination.absolutePath}"
        )
        if (destination.mkdirs()) {
            initRootDirectory(project, destination)
            project.modules.forEach {
                module ->
                val moduleName = module.name.replace(" ", "_")
                val moduleDirectory = File(destination.absolutePath, moduleName)
                if (!moduleDirectory.exists()) {
                    if (!moduleDirectory.mkdirs()) throw DirectoryCreationException(moduleDirectory)
                }
                // TODO: Handle module.
            }

        } else throw DirectoryCreationException(destination)
        return true
    }

    private fun initRootDirectory(project: Project, root: File) {
        createChangelog(root)
        createBuildGradle(root)
        createGitignore(root)
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

    private fun createGitignore(root: File) {
        val localFile = File(root.absolutePath, ".gitignore")
        if (!localFile.exists()) {
            logger.v("", Messages.INITIALIZING(localFile.name))
            localFile.appendText(gitignoreFactory.build())
            logger.v("", Messages.INITIALIZED(localFile.name))
        } else {
            logger.w("", Messages.FILE_ALREADY_EXIST(localFile))
        }
    }

}