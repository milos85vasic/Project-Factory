package net.milosvasic.factory.content

import java.io.File

object Messages {

    val PROJECT_ALREADY_EXIST = "Project already exists"
    fun INITIALIZED(what: String) = "Initialized [ $what ]"
    fun INITIALIZING(what: String) = "Initializing [ $what ]"
    fun INITIALIZATION_FAILED(what: String) = "Initialization failed [ $what ]"
    val COULD_NOT_CREATE_DIRECTORY = "Directory could not be created"
    fun FILE_ALREADY_EXIST(file: File) = "File already exist: ${file.absolutePath}"

}