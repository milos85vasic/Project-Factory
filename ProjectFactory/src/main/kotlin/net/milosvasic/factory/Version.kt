package net.milosvasic.factory

data class Version(
        val alpha: Int = 0,
        val beta: Int = 0,
        val primary: Int = 1,
        val secondary: Int = 0,
        val tertiary: Int = 0
)