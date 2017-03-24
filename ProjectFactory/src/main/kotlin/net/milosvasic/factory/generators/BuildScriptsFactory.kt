package net.milosvasic.factory.generators

import net.milosvasic.factory.configuration.Configuration
import net.milosvasic.factory.dependency.Classpath
import net.milosvasic.factory.module.Module

class BuildScriptsFactory {

    private val rootBuildGradle =
            """
buildscript {
    repositories {
        jcenter()
        mavenCentral()
    }
}

repositories {
    jcenter()
    mavenCentral()
}
"""

    fun build(): String {
        return rootBuildGradle
    }

    fun build(module: Module, classpath: Classpath): String {
        return """
buildscript {
    repositories {
        jcenter()
        mavenCentral()
        maven {
            url uri("${Configuration.repo}")
        }
    }
    dependencies {
        ${classpath.print()}
    }
}
"""
    }

}
