package com.example.rvnow.model
data class RVResponse(
    val results: List<RV>
)
//data class RV(
//    val id: Int,
//    val name: String,
//    val type: RVType,  // Rental or Sales type
//    val description: String,
//    val price: Double,
//    val imageUrl: String,
//    val place:String,
//    val driverLicence: String,
//    val kilometerLimitation: Int,
//)


data class RV(
    val id: Int,
    val name: String,
    val type: RVType,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val place: String,
    val additionalImages: List<String> = listOf(),
    val insuranceInfo: String,
    val driverLicence: String,
    val kilometerLimitation: Int
)
