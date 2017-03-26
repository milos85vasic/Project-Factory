package net.milosvasic.factory.plugin

class Plugins {

    val collection: MutableList<Plugin>? = mutableListOf()

    fun print(): String {
        val builder = StringBuilder()
        collection?.forEach {
            plugin ->
            builder
                    .append(plugin.print())
                    .append("\n")
            if (collection.indexOf(plugin) == collection.lastIndex) {
                builder.append("\n")
            }
        }
        return builder.toString()
    }

}