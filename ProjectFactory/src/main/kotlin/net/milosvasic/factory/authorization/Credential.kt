package net.milosvasic.factory.authorization


class Credential(val name: String) {

    val ftp = GeneralCredential("ftp.example.com", "default@example.com", "12345")

}