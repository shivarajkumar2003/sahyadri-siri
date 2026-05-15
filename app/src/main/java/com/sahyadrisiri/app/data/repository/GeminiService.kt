package com.sahyadrisiri.app.data.repository

import android.util.Log
import com.sahyadrisiri.app.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiService @Inject constructor() {
    private val client = OkHttpClient()
    private val apiKey = BuildConfig.GEMINI_API_KEY
    private val endpoint =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$apiKey"

    suspend fun analyzeWaterQuality(
        streamName: String,
        clarityRating: String,
        flowCondition: String,
        smell: String,
        pollutionDetails: String
    ): AiAnalysisResult? = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) return@withContext null

        val prompt = """
            You are an environmental scientist analyzing water quality in the Western Ghats (Sahyadri) region of India.
            Analyze this field report and provide:
            1. A brief condition summary (2-3 sentences)
            2. Health and ecological risks (2-3 sentences)
            3. Recommended village action steps (3-4 bullet points)
            4. A severity rating: low, medium, or high

            Report Data:
            - Stream/Source: $streamName
            - Water Clarity: $clarityRating (clear/turbid/muddy)
            - Flow Condition: $flowCondition (stagnant/normal/fast)
            - Smell: ${smell.ifBlank { "None noted" }}
            - Pollution Details: ${pollutionDetails.ifBlank { "None noted" }}

            Respond ONLY with a JSON object (no markdown):
            {
              "severity": "low|medium|high",
              "summary": "...",
              "risks": "...",
              "actions": "..."
            }
        """.trimIndent()

        try {
            val requestBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                })
            }.toString()

            val request = Request.Builder()
                .url(endpoint)
                .post(requestBody.toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            val body = response.body?.string() ?: return@withContext null

            val json = JSONObject(body)
            val text = json
                .getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")
                .trim()
                .removePrefix("```json")
                .removeSuffix("```")
                .trim()

            val result = JSONObject(text)
            AiAnalysisResult(
                severity = result.optString("severity", "low"),
                summary = result.optString("summary", ""),
                risks = result.optString("risks", ""),
                actions = result.optString("actions", "")
            )
        } catch (e: Exception) {
            Log.e("GeminiService", "Error analyzing water quality", e)
            null
        }
    }
}
