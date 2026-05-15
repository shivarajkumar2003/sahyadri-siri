    package com.sahyadrisiri.app.ui

import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.sahyadrisiri.app.data.model.ReportFormData
import com.sahyadrisiri.app.data.model.WaterReport
import com.sahyadrisiri.app.data.repository.AiAnalysisResult
import com.sahyadrisiri.app.data.repository.GeminiService
import com.sahyadrisiri.app.data.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class AppUiState(
    val currentScreen: Screen = Screen.MAP,
    val user: FirebaseUser? = null,
    val reports: List<WaterReport> = emptyList(),
    val selectedReport: WaterReport? = null,
    val userLocation: Location? = null,
    val isSubmitSheetOpen: Boolean = false,
    val isSubmitting: Boolean = false,
    val submitError: String? = null
)

enum class Screen { MAP, REPORTS, REPORT_DETAIL, PROFILE }

@HiltViewModel
class MainViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    private val geminiService: GeminiService,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState(user = auth.currentUser))
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        observeAuth()
        loadReports()
    }

    private fun observeAuth() {
        auth.addAuthStateListener { firebaseAuth ->
            _uiState.update { it.copy(user = firebaseAuth.currentUser) }
        }
    }

    private fun loadReports() {
        viewModelScope.launch {
            reportRepository.getReportsFlow().collect { reports ->
                _uiState.update { it.copy(reports = reports) }
            }
        }
    }

    fun navigateTo(screen: Screen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun selectReport(report: WaterReport) {
        _uiState.update { it.copy(selectedReport = report, currentScreen = Screen.REPORT_DETAIL) }
    }

    fun openSubmitSheet() {
        _uiState.update { it.copy(isSubmitSheetOpen = true) }
    }

    fun closeSubmitSheet() {
        _uiState.update { it.copy(isSubmitSheetOpen = false) }
    }

    fun setUserLocation(location: Location) {
        _uiState.update { it.copy(userLocation = location) }
    }

    suspend fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
    }

    fun signOut() {
        auth.signOut()
    }

    fun submitReport(formData: ReportFormData) {
        val location = _uiState.value.userLocation

        if (location == null) {
            _uiState.update {
                it.copy(
                    submitError = "Unable to fetch location. Please try again."
                )
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, submitError = null) }
            try {
                val aiResult: AiAnalysisResult? = geminiService.analyzeWaterQuality(
                    streamName = formData.streamName,
                    clarityRating = formData.clarityRating,
                    flowCondition = formData.flowCondition,
                    smell = formData.smell,
                    pollutionDetails = formData.pollutionDetails
                )
                reportRepository.submitReport(
                    formData = formData,
                    lat = location.latitude,
                    lng = location.longitude,
                    aiResult = aiResult
                )
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        isSubmitSheetOpen = false,
                        currentScreen = Screen.REPORTS
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isSubmitting = false, submitError = "Failed to submit. Please try again.")
                }
            }
        }
    }

    fun getUserReportCount(): Int {
        return reportRepository.getUserReportCount(_uiState.value.reports)
    }
}
