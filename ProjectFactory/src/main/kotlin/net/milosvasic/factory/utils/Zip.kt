package net.milosvasic.factory.utils

import java.io.FileOutputStream
import java.io.BufferedOutputStream
import java.io.IOException
import java.util.zip.ZipInputStream
import java.io.File
import java.util.zip.ZipEntry
import java.io.FileInputStream



class Zip {

    private val BUFFER_SIZE = 4096

    @Throws(IOException::class)
    fun unzip(zipFilePath: File, destDir: File) {
        val zipIn = ZipInputStream(FileInputStream(zipFilePath))
        var entry: ZipEntry? = zipIn.nextEntry
        while (entry != null) {
            val filePath = "${destDir.absolutePath}${File.separator}${entry.name}"
            if (!entry.isDirectory) {
                extractFile(zipIn, filePath)
            } else {
                val dir = File(filePath)
                dir.mkdir()
            }
            zipIn.closeEntry()
            entry = zipIn.nextEntry
        }
        zipIn.close()
    }

    @Throws(IOException::class)
    private fun extractFile(zipIn: ZipInputStream, filePath: String) {
        val bos = BufferedOutputStream(FileOutputStream(filePath))
        val bytesIn = ByteArray(BUFFER_SIZE)
        var read = zipIn.read(bytesIn)
        while (read  != -1) {
            bos.write(bytesIn, 0, read)
            read = zipIn.read(bytesIn)
        }
        bos.close()
    }

}