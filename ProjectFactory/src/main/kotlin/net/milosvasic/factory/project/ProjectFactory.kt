package net.milosvasic.factory.project

import net.milosvasic.factory.content.Messages
import net.milosvasic.factory.exception.DirectoryCreationException
import java.io.File

abstract class ProjectFactory {

    fun create(project: Project, home: File) {
        val destination = File(home.absolutePath, project.name)
        if (destination.exists()) throw IllegalStateException(
                "${Messages.PROJECT_ALREADY_EXIST}: ${destination.absolutePath}"
        )
        if (destination.mkdirs()) {
            project.modules.forEachIndexed {
                i, module ->
                val moduleDirectory = File(destination.absolutePath, module.name)
                if (moduleDirectory.mkdirs()) {

                } else throw DirectoryCreationException(moduleDirectory)
            }

        } else throw DirectoryCreationException(destination)
    }

}