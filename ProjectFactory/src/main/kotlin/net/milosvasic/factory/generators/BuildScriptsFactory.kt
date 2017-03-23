package net.milosvasic.factory.generators

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

}
