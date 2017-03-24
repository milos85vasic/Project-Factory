package net.milosvasic.factory.configuration

import net.milosvasic.factory.dependency.Dependency


object Groot {
    val name = "Groot"
    val version = "1.0.0-Beta-1"
    val group = "net.milosvasic.groot"

    fun getDependency(): Dependency {
        return Dependency(group, name, version)
    }
}