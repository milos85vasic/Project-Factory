package net.milosvasic.factory

data class Version(
        val alpha: Int,
        val beta: Int,
        val primary: Int = 1,
        val secondary: Int,
        val tertiary: Int
)