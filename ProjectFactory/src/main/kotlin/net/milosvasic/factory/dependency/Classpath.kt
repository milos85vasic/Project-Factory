package net.milosvasic.factory.dependency

class Classpath {

    val dependencies = mutableListOf<Dependency>()

    fun print(): String {
        val builder = StringBuilder()
        dependencies.forEachIndexed {
            index, dependency ->
            builder.append("classpath \"${dependency.group}:${dependency.name}:${dependency.version}\"")
            if (index < dependencies.lastIndex) {
                builder.append("\n\t\t")
            }
        }
        return builder.toString()
    }

}
