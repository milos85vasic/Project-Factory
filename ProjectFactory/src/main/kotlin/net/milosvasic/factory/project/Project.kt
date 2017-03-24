package net.milosvasic.factory.project

import net.milosvasic.factory.authorization.Credential
import net.milosvasic.factory.langauge.Java
import net.milosvasic.factory.langauge.Language
import net.milosvasic.factory.module.Module
import net.milosvasic.factory.plugin.Plugin
import net.milosvasic.factory.plugin.Plugins
import java.util.*

class Project(val name: String, val group: String) {

    val plugins = Plugins()
    val modules = HashSet<Module>()
    val language: Language = Java()
    val credentials = mutableListOf<Credential>()

    init {
        initPlugins()
    }

    private fun initPlugins() {
        plugins.collection.add(Plugin("groot"))
        plugins.collection.add(Plugin("groot-${language.name.toLowerCase()}"))
        plugins.collection.add(Plugin("groot-credentials"))
    }

}

