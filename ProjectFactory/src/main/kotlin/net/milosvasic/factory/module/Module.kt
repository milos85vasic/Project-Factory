package net.milosvasic.factory.module

import net.milosvasic.factory.Jar
import net.milosvasic.factory.Repository
import java.util.*

class Module(val name: String) {

    val repositories = HashSet<Repository>()
    val moduleDependencies = HashSet<Module>()
    val jarDependencies = HashSet<Jar>()

}