package util

fun main() {
    val conn = DatabaseUtil.connection
    val stmt = conn.createStatement()
    val rs = stmt.executeQuery("SELECT * FROM company")

    while (rs.next()) {
        println("Company: " + rs.getString("name"))
    }

    rs.close()
    stmt.close()
    conn.close()
}
