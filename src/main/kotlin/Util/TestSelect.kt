package util

import Util.DatabaseUtil

fun main() {
    val conn = DatabaseUtil.connection
    val statement = conn.createStatement()
    val result = statement.executeQuery("SELECT * FROM company")

    while (result.next()) {
        println("Company: " + result.getString("name"))
    }

    result.close()
    statement.close()
    conn.close()
}
