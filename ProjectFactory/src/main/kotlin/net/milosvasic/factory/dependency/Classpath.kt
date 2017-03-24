package net.milosvasic.factory.dependency

import net.milosvasic.logger.SimpleLogger

class Classpath {

    @Transient
    private val logger = SimpleLogger()
    val dependencies = mutableListOf<Dependency>()

    fun print(): String {
        logger.v("", "printing classpath dependencies [ START ]")
        val builder = StringBuilder()
        dependencies.forEachIndexed {
            index, dependency ->
            builder.append("classpath \"${dependency.group}:${dependency.name}:${dependency.version}\"")
            if (index < dependencies.lastIndex) {
                builder.append("\n\t\t\t\t")
            }
        }
        val result = builder.toString()
        logger.v("", "printing classpath dependencies [ END ]")
        return result
    }

}
