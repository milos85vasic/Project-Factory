package net.milosvasic.factory.plugin

import net.milosvasic.logger.SimpleLogger

class Plugins {

    @Transient
    private val logger = SimpleLogger()
    val collection = mutableListOf<Plugin>()

    fun print(): String {
        logger.v("", "printing plugins [ START ]")
        val builder = StringBuilder()
        collection.forEach {
            plugin ->
            builder.append(plugin.print())
        }
        val result = builder.toString()
        logger.v("", "printing plugins [ END ]")
        return result
    }

}