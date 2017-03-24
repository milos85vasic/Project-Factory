package net.milosvasic.factory.project

import net.milosvasic.factory.authorization.Credential
import net.milosvasic.factory.module.Module
import java.util.*

open class Project(val name: String, val group: String) {

    val languageVersion = ""
    val modules = HashSet<Module>()
    val credentials = mutableListOf<Credential>()

}

