package net.milosvasic.factory.module

import com.google.gson.annotations.SerializedName
import net.milosvasic.factory.Jar
import net.milosvasic.factory.Repository
import sun.misc.Version
import java.util.*

open class Module(val name: String, @SerializedName("package") val pPackage: String) {

    val version = Version()
    val isApplication = false

//    val repositories = HashSet<Repository>()
//    val moduleDependencies = HashSet<Module>()
//    val jarDependencies = HashSet<Jar>()

}