
package com.example.rvnow.api
import android.util.Log
import com.google.firebase.Timestamp
import com.example.rvnow.model.RV
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RVInformation {

    private val db = FirebaseFirestore.getInstance()
    private val rvCollection = db.collection("rvs")

    // Fetch all RVs from the collection

    suspend fun fetchAllRVs(): List<RV> {
        return try {
            val snapshot = rvCollection.get().await()
            println("DEBUG: Firestore snapshot size = ${snapshot.documents.size}")

            val rvList = snapshot.documents.mapNotNull { it.toObject(RV::class.java) }

            println("DEBUG: Parsed RV list size = ${rvList.size}")
            rvList
        } catch (e: Exception) {
            println("DEBUG: Firestore error - ${e.message}")
            emptyList()
        }
    }

    suspend fun fetchLastRVId(): String? {
        return try {
            val snapshot = rvCollection
                .orderBy("id", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .await()

            val lastRV = snapshot.documents.firstOrNull()
            lastRV?.id // or lastRV?.getString("id") if ID is stored as a field
        } catch (e: Exception) {
            println("DEBUG: Error fetching last RV ID - ${e.message}")
            null
        }
    }




    suspend fun addAllRV(rv:RV) {
        try {

                rvCollection.document(rv.id).set(rv).await()
                Log.d("Firestore", "Document added/updated with ID: ${rv.id}")
                // Use await() to properly suspend execution until Firestore operation completes
//                rvCollection.document(rvObject.id).set(rvObject).await()

//                Log.d("Firestore", "Document added/updated with ID: ${rvObject.id}")

        } catch (e: Exception) {
            Log.e("Firestore", "Error adding RVs: ${e.message}", e)
            throw e
        }
    }
}

    // Add an RV to the collection

//    suspend fun addallRV(rv: RV) {
//        try {
//            for (rvMap in rvnewList) {
//                val rvObject = RV(
//                    id = rvMap["id"] as String,
//                    ownerId = rvMap["ownerId"] as String,
//                    name = rvMap["name"] as String,
////                    type = rvMap["type"] as RVType,
//                    description = rvMap["description"] as String,
//                    pricePerDay = rvMap["pricePerDay"] as Double,
//                    imageUrl = rvMap["imageUrl"] as String,
//                    place = rvMap["place"] as String,
//                    additionalImages = rvMap["additionalImages"] as List<String>,
//                    insurance = rvMap["insurance"] as Map<String, String>,
//                    driverLicenceRequired = rvMap["driverLicenceRequired"] as String,
//                    kilometerLimitation = rvMap["kilometerLimitation"] as Int,
//                    isForSale = rvMap["isForSale"] as Boolean,
//                    status = rvMap["status"] as String,
//                    createdAt = rvMap["createdAt"] as Timestamp,
//                    bookedDates = rvMap["bookedDates"] as List<Map<String, Timestamp>>
//                )
//
//                db.collection("rvs")
//                    .add(rvObject)
//                    .addOnSuccessListener { documentReference ->
//                        Log.d("Firestore", "Document added with ID: ${documentReference.id}")
//                    }
//                    .addOnFailureListener { e ->
//                        Log.w("Firestore", "Error adding document", e)
//                    }
//            }
//        } catch (e: Exception) {
//            // Handle error (e.g., log exception)
//            throw e
//        }
//    }
//





//    val rvnewList = listOf(
//        hashMapOf(
//            "id" to "rv001",
//            "ownerId" to "owner001",
//            "name" to "Explorer 2025",
//            "type" to "Rental", // String equivalent of the enum
//            "description" to "Perfect for off-road adventures with luxurious interiors.",
//            "pricePerDay" to 120.0,
//            "imageUrl" to "https://example.com/images/rv001.jpg",
//            "place" to "New York, USA",
//            "additionalImages" to listOf("https://example.com/images/rv001_1.jpg", "https://example.com/images/rv001_2.jpg"),
//            "insurance" to mapOf("type" to "Comprehensive", "company" to "ABC Insurance"),
//            "driverLicenceRequired" to "B",
//            "kilometerLimitation" to 300,
//            "isForSale" to false,
//            "status" to "Available",
//            "createdAt" to Timestamp.now(),
//            "bookedDates" to listOf(
//                hashMapOf("startDate" to Timestamp.now(), "endDate" to Timestamp.now())
//            )
//        ),
//        hashMapOf(
//            "id" to "rv002",
//            "ownerId" to "owner002",
//            "name" to "Luxury RV 2024",
//            "type" to "Sales",
//            "description" to "A high-end RV with all the comforts of home.",
//            "pricePerDay" to 250.0,
//            "imageUrl" to "https://example.com/images/rv002.jpg",
//            "place" to "Los Angeles, California",
//            "additionalImages" to listOf("https://example.com/images/rv002_1.jpg", "https://example.com/images/rv002_2.jpg"),
//            "insurance" to mapOf("type" to "Full Coverage", "company" to "XYZ Insurance"),
//            "driverLicenceRequired" to "No",
//            "kilometerLimitation" to 400,
//            "isForSale" to true,
//            "status" to "For Sale",
//            "createdAt" to Timestamp.now(),
//            "bookedDates" to listOf(
//                hashMapOf("startDate" to Timestamp.now(), "endDate" to Timestamp.now())
//            )
//        ),
//        hashMapOf(
//            "id" to "rv003",
//            "ownerId" to "owner003",
//            "name" to "Mountain Adventure",
//            "type" to "Rental",
//            "description" to "Designed for mountain trips, with extra space for equipment.",
//            "pricePerDay" to 150.0,
//            "imageUrl" to "https://example.com/images/rv003.jpg",
//            "place" to "Denver, Colorado",
//            "additionalImages" to listOf("https://example.com/images/rv003_1.jpg", "https://example.com/images/rv003_2.jpg"),
//            "insurance" to mapOf("type" to "Third Party", "company" to "MNO Insurance"),
//            "driverLicenceRequired" to "Yes",
//            "kilometerLimitation" to 500,
//            "isForSale" to false,
//            "status" to "Available",
//            "createdAt" to Timestamp.now(),
//            "bookedDates" to listOf(
//                hashMapOf("startDate" to Timestamp.now(), "endDate" to Timestamp.now())
//            )
//        ),
//        hashMapOf(
//            "id" to "rv004",
//            "ownerId" to "owner004",
//            "name" to "Coastal Explorer",
//            "type" to "Rental",
//            "description" to "Ideal for long road trips along the coast, with a spacious interior.",
//            "pricePerDay" to 180.0,
//            "imageUrl" to "https://example.com/images/rv004.jpg",
//            "place" to "Miami, Florida",
//            "additionalImages" to listOf("https://example.com/images/rv004_1.jpg", "https://example.com/images/rv004_2.jpg"),
//            "insurance" to mapOf("type" to "Comprehensive", "company" to "DEF Insurance"),
//            "driverLicenceRequired" to "Yes",
//            "kilometerLimitation" to 600,
//            "isForSale" to true,
//            "status" to "For Sale",
//            "createdAt" to Timestamp.now(),
//            "bookedDates" to listOf(
//                hashMapOf("startDate" to Timestamp.now(), "endDate" to Timestamp.now())
//            )
//        ),
//        hashMapOf(
//            "id" to "rv005",
//            "ownerId" to "owner005",
//            "name" to "Family RV",
//            "type" to "Rental",
//            "description" to "Perfect for family road trips with enough space for everyone.",
//            "pricePerDay" to 160.0,
//            "imageUrl" to "https://example.com/images/rv005.jpg",
//            "place" to "Austin, Texas",
//            "additionalImages" to listOf("https://example.com/images/rv005_1.jpg", "https://example.com/images/rv005_2.jpg"),
//            "insurance" to mapOf("type" to "Third Party", "company" to "XYZ Insurance"),
//            "driverLicenceRequired" to "Yes",
//            "kilometerLimitation" to 450,
//            "isForSale" to false,
//            "status" to "Available",
//            "createdAt" to Timestamp.now(),
//            "bookedDates" to listOf(
//                hashMapOf("startDate" to Timestamp.now(), "endDate" to Timestamp.now())
//            )
//        )
//    )


// Loop to add each RV to Firestore




