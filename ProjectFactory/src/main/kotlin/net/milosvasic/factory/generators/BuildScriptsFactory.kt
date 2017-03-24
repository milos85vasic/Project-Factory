package net.milosvasic.factory.generators

import net.milosvasic.factory.configuration.Configuration
import net.milosvasic.factory.dependency.Classpath
import net.milosvasic.factory.module.Module
import net.milosvasic.factory.project.Project

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

    fun build(project: Project, module: Module, classpath: Classpath): String {
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

${project.plugins.print()}
${module.plugins.print()}

"""
    }

}

/**
 *
groot.registerRepository("http://repo.milosvasic.net/releases")
groot.registerRepository("http://repo.milosvasic.net/development")

groot.kotlin.version = "1.1.1"

final alpha = 1
final beta = 0
final version = 1
final secondaryVersion = 0
final tertiaryVersion = 0
final projectPackage = "kotlin"
final projectGroup = "net.milosvasic.tryout.groot"

groot.kotlin.project.setup(
alpha,
beta,
version,
secondaryVersion,
tertiaryVersion,
projectGroup,
projectPackage
)

String fullPackage = groot.kotlin.project.projectPackage
String fullVersion = groot.kotlin.project.projectVersion

groot.kotlin.application.setup(fullPackage)

groot.deployment.ftp.host = ftpServer
groot.deployment.ftp.username = ftpUsername
groot.deployment.ftp.password = ftpPassword
groot.deployment.setup(fullPackage, fullVersion)
 *
 */
