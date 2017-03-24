package net.milosvasic.factory.module

import com.google.gson.annotations.SerializedName
import net.milosvasic.factory.authorization.Credential
import net.milosvasic.factory.langauge.Language
import net.milosvasic.factory.plugin.Plugin
import net.milosvasic.factory.plugin.Plugins
import net.milosvasic.logger.SimpleLogger
import sun.misc.Version

open class Module(val name: String, @SerializedName("package") val pPackage: String, val group: String) {

    val isApplication = false
    val version : Version? = Version()
    private val plugins : Plugins? = Plugins()
    val credentials: MutableList<Credential>? = mutableListOf()

    @Transient
    private val logger = SimpleLogger()

    fun getPlugins(language: Language): Plugins {
        val pluginsSet = Plugins()
        pluginsSet.collection?.add(Plugin("groot"))
        pluginsSet.collection?.add(Plugin("groot-${language.name.toLowerCase()}"))
        pluginsSet.collection?.add(Plugin("groot-credentials"))
        if (plugins?.collection != null) {
            pluginsSet.collection?.addAll(plugins.collection)
        } else {
            logger.w("", "Module has no additional plugins set.")
        }
        return pluginsSet
    }

}