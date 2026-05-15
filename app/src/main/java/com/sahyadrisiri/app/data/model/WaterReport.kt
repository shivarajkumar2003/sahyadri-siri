package com.sahyadrisiri.app.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.GeoPoint

data class WaterReport(
    @DocumentId val id: String = "",
    val streamName: String = "",
    val clarityRating: String = "clear",   // clear | turbid | muddy
    val flowCondition: String = "normal",  // stagnant | normal | fast
    val smell: String = "",
    val pollutionDetails: String = "",
    val isAnonymous: Boolean = false,
    val location: GeoPoint? = null,
    val userId: String = "",
    val severity: String = "low",          // low | medium | high
    val aiSummary: String = "",
    val aiRisks: String = "",
    val aiActions: String = "",
    val createdAt: Timestamp? = null
) {
    val severityLevel: SeverityLevel
        get() = when (severity) {
            "high" -> SeverityLevel.HIGH
            "medium" -> SeverityLevel.MEDIUM
            else -> SeverityLevel.LOW
        }

    val clarityLevel: ClarityLevel
        get() = when (clarityRating) {
            "muddy" -> ClarityLevel.MUDDY
            "turbid" -> ClarityLevel.TURBID
            else -> ClarityLevel.CLEAR
        }
}

enum class SeverityLevel { LOW, MEDIUM, HIGH }
enum class ClarityLevel { CLEAR, TURBID, MUDDY }

data class ReportFormData(
    val streamName: String = "",
    val clarityRating: String = "clear",
    val flowCondition: String = "normal",
    val smell: String = "",
    val pollutionDetails: String = "",
    val isAnonymous: Boolean = false
)
