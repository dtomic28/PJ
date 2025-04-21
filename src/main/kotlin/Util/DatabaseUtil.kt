package util

import java.sql.Connection
import java.sql.DriverManager
import java.io.File
import org.json.JSONObject

object DatabaseUtil {
    private val config: JSONObject by lazy {
        val configFile = File("src/main/kotlin/db/config.json")
        val content = configFile.readText(Charsets.UTF_8)
        JSONObject(content)
    }

    val connection: Connection by lazy {
        val url = config.getString("url")
        val username = config.getString("username")
        val password = config.getString("password")
        DriverManager.getConnection(url, username, password)
    }
}
