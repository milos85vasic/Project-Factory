package net.milosvasic.factory.generators

import net.milosvasic.factory.configuration.Configuration
import net.milosvasic.factory.content.Messages
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
        if (project.language != null) {
            if (module.version != null) {
                val builder = StringBuilder()
                builder.append("buildscript {\n")
                        .append("\trepositories {\n")
                        .append("\t\tjcenter()\n")
                        .append("\t\tmavenCentral()\n")
                        .append("\t\tmaven {\n")
                        .append("\t\t\turl uri(\"${Configuration.repo}\")\n")
                        .append("\t\t}\n")
                        .append("\t}\n")
                        .append("\tdependencies {\n")
                        .append("\t\t${classpath.print()}\n")
                        .append("\t}\n")
                        .append("}\n\n")
                        .append(module.getPlugins(project.language).print())
                        .append("groot.registerRepository(\"${Configuration.repo}\")\n\n")
                        .append("groot.${project.language.name.toLowerCase()}.version = \"${project.language.version}\"\n\n")
                        .append("final alpha = ${module.version.alpha}\n")
                        .append("final beta = ${module.version.beta}\n")
                        .append("final version = ${module.version.primary}\n")
                        .append("final secondaryVersion = ${module.version.secondary}\n")
                        .append("final tertiaryVersion = ${module.version.tertiary}\n")
                        .append("final projectPackage = \"${module.pPackage}\"\n")
                        .append("final projectGroup = \"${module.group}\"\n\n")
                        .append("groot.${project.language.name.toLowerCase()}.project.setup(\n")
                        .append("\talpha,\n")
                        .append("\tbeta,\n")
                        .append("\tversion,\n")
                        .append("\tsecondaryVersion,\n")
                        .append("\ttertiaryVersion,\n")
                        .append("\tprojectGroup,\n")
                        .append("\tprojectPackage\n")
                        .append(")\n\n")
                        .append("String fullPackage = groot.${project.language.name.toLowerCase()}.project.projectPackage\n")
                        .append("String fullVersion = groot.${project.language.name.toLowerCase()}.project.projectVersion\n\n")

                if (module.isApplication) {
                    builder.append("groot.${project.language.name.toLowerCase()}.application.setup(fullPackage)\n\n")
                }

                if (module.credentials != null || project.credentials != null) {
                    builder.append("groot.deployment.ftp.host = ftpServer\n")
                            .append("groot.deployment.ftp.username = ftpUsername\n")
                            .append("groot.deployment.ftp.password = ftpPassword\n")
                            .append("groot.deployment.setup(fullPackage, fullVersion)\n\n")
                }

                return builder.toString()
            } else throw IllegalStateException(Messages.NO_VERSION_SPECIFIED(module))
        } else throw IllegalStateException(Messages.NO_LANGUAGE_SPECIFIED)
    }

}

