package net.milosvasic.factory.content

import net.milosvasic.factory.module.Module
import java.io.File

object Messages {

    fun GRADLE(what: String) = "Gradle [ $what ]"
    val NO_LANGUAGE_SPECIFIED = "No language specified."
    val PROJECT_ALREADY_EXIST = "Project already exists"
    fun RETRIEVING(what: String) = "Retrieving [ $what ]"
    fun EXTRACTING(what: String) = "Extracting [ $what ]"
    fun INITIALIZED(what: String) = "Initialized [ $what ]"
    fun INITIALIZING(what: String) = "Initializing [ $what ]"
    val GIT_INITIALIZATION_FAILED = "Git initialization failed"
    val COULD_NOT_CREATE_DIRECTORY = "Directory could not be created"
    fun INITIALIZATION_FAILED(what: String) = "Initialization failed [ $what ]"
    fun FILE_ALREADY_EXIST(file: File) = "File already exist: ${file.absolutePath}"
    fun NO_VERSION_SPECIFIED(module: Module) = "No version specified for the module [ ${module.name.replace(" ", "_")} ]"
    fun LOCAL_GRADLE_DISTRIBUTION_ALREADY_AVAILABLE(version: String) = "Local Gradle distribution already available [ $version ]"
    fun COULD_NOT_DELETE_FILE(localFile: File) = "Could not delete file [ ${localFile.absolutePath} ]"

}