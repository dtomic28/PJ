package dao

import Core.Types.Company

interface CompanyDao : DaoCrud<Company> {
    fun getByTaxNumber(taxNumber: String): Company?
    fun getAllByTaxpayerStatus(taxpayerStatus: Boolean): List<Company>
}