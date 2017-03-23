package net.milosvasic.factory.module

import com.google.gson.annotations.SerializedName
import net.milosvasic.factory.authorization.Credential
import sun.misc.Version

open class Module(val name: String, @SerializedName("package") val pPackage: String) {

    val version = Version()
    val isApplication = false
    val credentials = mutableListOf<Credential>()

}