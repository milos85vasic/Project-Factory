import java.io.File

fun getHome(target: String): File {
    val home = System.getProperty("user.home")
    val root = File("$home${File.separator}$target")
    if (!root.exists()) {
        root.mkdirs()
    }
    return root
}