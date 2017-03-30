package net.milosvasic.factory.project

import net.milosvasic.factory.authorization.Credential
import net.milosvasic.factory.git.Git
import net.milosvasic.factory.langauge.Java
import net.milosvasic.factory.langauge.Language
import net.milosvasic.factory.module.Module
import java.util.*

class Project(val name: String, val group: String) {

    val git: Git? = null
    val modules: HashSet<Module>? = HashSet()
    val language: Language? = Java()
    val credentials: MutableList<Credential>? = mutableListOf()

}

