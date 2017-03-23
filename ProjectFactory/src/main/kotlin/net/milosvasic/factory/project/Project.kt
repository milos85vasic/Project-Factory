package net.milosvasic.factory.project

import com.google.gson.annotations.SerializedName
import net.milosvasic.factory.authorization.Credential
import net.milosvasic.factory.module.Module
import java.util.*

open class Project(val name: String, @SerializedName("package") val pPackage: String, val group: String) {

    val modules = HashSet<Module>()
    val credentials = mutableListOf<Credential>()

}

