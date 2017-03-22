package net.milosvasic.factory.project

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import getHome
import net.milosvasic.factory.content.Messages
import net.milosvasic.factory.exception.DirectoryCreationException
import java.io.File

abstract class ProjectFactory {

    val gson = Gson()

    protected abstract val workingFolderName: String

    fun create(json: File): Boolean {
        val jsonConfiguration = json.readText()
        val project: Project
        try {
            project = gson.fromJson<Project>(jsonConfiguration)
        } catch (e: Exception) {
            return false
        }
        val home = getHome(workingFolderName)
        val destination = File(home.absolutePath, project.name)
        if (destination.exists()) throw IllegalStateException(
                "${Messages.PROJECT_ALREADY_EXIST}: ${destination.absolutePath}"
        )
        if (destination.mkdirs()) {
            project.modules.forEach {
                module ->
                val moduleDirectory = File(destination.absolutePath, module.name)
                if (moduleDirectory.mkdirs()) {

                } else throw DirectoryCreationException(moduleDirectory)
            }

        } else throw DirectoryCreationException(destination)
        return true
    }

}