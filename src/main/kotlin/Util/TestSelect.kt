package util

fun main() {
    val connection = DatabaseUtil.connection
    val statement = connection.createStatement()
    val result = statement.executeQuery("SELECT * FROM company")

    while (result.next()) {
        println("Company: " + result.getString("name"))
    }

    result.close()
    statement.close()
    connection.close()
}
