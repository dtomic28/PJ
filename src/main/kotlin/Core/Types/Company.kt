package Core.Types

import Core.Types.Interface.BaseObject

// Company class representing a business entity
class Company(
    val name: String,
    val taxNumber: String,
    val registrationNumber: String,
    val accountNumber: String,
    val email: String,
    val taxpayer: Boolean
) : BaseObject() {

    override fun search(query: String): Boolean {
        val searchText = query.lowercase()
        return name.lowercase().contains(searchText) ||
                taxNumber.lowercase().contains(searchText) ||
                registrationNumber.lowercase().contains(searchText) ||
                accountNumber.lowercase().contains(searchText) ||
                email.lowercase().contains(searchText) ||
                taxpayer.toString().lowercase().contains(searchText)
    }

    override fun toString(): String {
        return """
            $name
            DŠ: $taxNumber
            Matična št.: $registrationNumber
            TRR: $accountNumber
            Email: $email
            ${if (taxpayer) "Davčni zavezanec" else "Ni davčni zavezanec"}
        """.trimIndent()
    }
}