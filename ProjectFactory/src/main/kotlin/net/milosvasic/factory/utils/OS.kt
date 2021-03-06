package net.milosvasic.factory.utils

object OS {

    val MACOS = "mac"
    val LINUX = "linux"
    val WINDOWS = "windows"

    fun getOS(): String {
        return System.getProperty("os.name").toLowerCase()
    }

    fun isMacOS(): Boolean {
        return getOS().contains(MACOS)
    }

    fun isLinux(): Boolean {
        return getOS().contains(LINUX)
    }

    fun isWindows(): Boolean {
        return getOS().contains(WINDOWS)
    }

}