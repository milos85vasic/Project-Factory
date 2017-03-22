package net.milosvasic.factory.project

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import getHome
import net.milosvasic.factory.content.Labels
import net.milosvasic.factory.content.Messages
import net.milosvasic.factory.exception.DirectoryCreationException
import net.milosvasic.logger.SimpleLogger
import java.io.File

abstract class ProjectFactory {

    private val gson = Gson()
    private val logger = SimpleLogger()
    protected abstract val workingFolderName: String

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
                if (moduleDirectory.mkdirs()) {
                    // TODO: Handle module.
                } else throw DirectoryCreationException(moduleDirectory)
            }

        } else throw DirectoryCreationException(destination)
        return true
    }

    private fun initRootDirectory(project: Project, root: File) {
        createChangelog(project, root)
    }

    private fun createChangelog(project: Project, root: File) {
        val localFile = File(root.absolutePath, "CHANGELOG.md")
        if (!localFile.exists()) {
            logger.v("", Messages.INITIALIZING(localFile.name))
            localFile.appendText("# Version ${project.printVersion()}\n\n")
            localFile.appendText("- TBD.\n")
            logger.v("", Messages.INITIALIZED(localFile.name))
        } else {
            logger.w("", Messages.FILE_ALREADY_EXIST(localFile))
        }
    }

}