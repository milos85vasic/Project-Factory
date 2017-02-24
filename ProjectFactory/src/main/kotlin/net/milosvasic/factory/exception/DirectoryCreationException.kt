package net.milosvasic.factory.exception

import net.milosvasic.factory.content.Messages
import java.io.File


class DirectoryCreationException(val directory: File) : Exception("${Messages.COULD_NOT_CREATE_DIRECTORY} [ ${directory.absolutePath} ]")