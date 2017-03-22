package net.milosvasic.factory.project

import net.milosvasic.factory.module.Module
import java.util.*

class Project(val name: String) {

    val modules = HashSet<Module>()

    fun printVersion(): String {
        return "1.0.0." // TODO: To be dynamic.
    }

}

