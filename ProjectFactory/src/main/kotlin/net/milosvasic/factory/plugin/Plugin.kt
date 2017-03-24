package net.milosvasic.factory.plugin


class Plugin(val name: String) {

    fun print():String {
        return "apply plugin: \"$name\""
    }

}