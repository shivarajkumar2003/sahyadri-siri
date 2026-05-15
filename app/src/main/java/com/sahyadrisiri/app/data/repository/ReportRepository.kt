package com.sahyadrisiri.app.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import com.sahyadrisiri.app.data.model.ReportFormData
import com.sahyadrisiri.app.data.model.WaterReport
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val reportsCollection = firestore.collection("reports")

    fun getReportsFlow(): Flow<List<WaterReport>> = callbackFlow {
        val query = reportsCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(50)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val reports = snapshot?.documents?.mapNotNull { doc ->
                try {
                    doc.toObject(WaterReport::class.java)?.copy(id = doc.id)
                } catch (e: Exception) {
                    null
                }
            } ?: emptyList()
            trySend(reports)
        }
        awaitClose { listener.remove() }
    }

    suspend fun submitReport(
        formData: ReportFormData,
        lat: Double,
        lng: Double,
        aiResult: AiAnalysisResult?
    ) {
        val userId = auth.currentUser?.uid ?: "anonymous"
        val reportMap = hashMapOf(
            "streamName" to formData.streamName,
            "clarityRating" to formData.clarityRating,
            "flowCondition" to formData.flowCondition,
            "smell" to formData.smell,
            "pollutionDetails" to formData.pollutionDetails,
            "isAnonymous" to formData.isAnonymous,
            "location" to GeoPoint(lat, lng),
            "userId" to userId,
            "severity" to (aiResult?.severity ?: "low"),
            "aiSummary" to (aiResult?.summary ?: ""),
            "aiRisks" to (aiResult?.risks ?: ""),
            "aiActions" to (aiResult?.actions ?: ""),
            "createdAt" to Timestamp.now()
        )
        reportsCollection.add(reportMap).await()
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun getUserReportCount(reports: List<WaterReport>): Int {
        val uid = auth.currentUser?.uid ?: return 0
        return reports.count { it.userId == uid }
    }
}

data class AiAnalysisResult(
    val severity: String,
    val summary: String,
    val risks: String,
    val actions: String
)
