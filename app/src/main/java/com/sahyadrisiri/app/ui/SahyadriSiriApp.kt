package com.sahyadrisiri.app.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.sahyadrisiri.app.ui.components.ReportFormSheet
import com.sahyadrisiri.app.ui.screens.MapScreen
import com.sahyadrisiri.app.ui.screens.ReportDetailScreen
import com.sahyadrisiri.app.ui.screens.ReportsScreen
import com.sahyadrisiri.app.ui.theme.Blue600
import com.sahyadrisiri.app.ui.theme.Blue50
import com.sahyadrisiri.app.ui.theme.Gray100
import com.sahyadrisiri.app.ui.theme.Gray200
import com.sahyadrisiri.app.ui.theme.Gray400
import com.sahyadrisiri.app.ui.theme.Gray500
import com.sahyadrisiri.app.ui.theme.Gray600
import com.sahyadrisiri.app.ui.theme.Slate400
import com.sahyadrisiri.app.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SahyadriSiriApp(
    viewModel: MainViewModel,
    onGoogleSignIn: () -> Unit,
    onSignOut: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        containerColor = Color(0xFFFDFDFD),
        topBar = {
            AppTopBar(
                user = uiState.user,
                onLogoClick = { viewModel.navigateTo(Screen.MAP) },
                onSignIn = onGoogleSignIn,
                onSignOut = onSignOut,
                onProfileClick = { viewModel.navigateTo(Screen.PROFILE) }
            )
        },
        bottomBar = {
            AppBottomBar(
                currentScreen = uiState.currentScreen,
                onNavigate = { viewModel.navigateTo(it) },
                onAddReport = { viewModel.openSubmitSheet() }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AnimatedContent(
                targetState = uiState.currentScreen,
                transitionSpec = {
                    when (targetState) {
                        Screen.REPORT_DETAIL -> (slideInHorizontally { it } + fadeIn()) togetherWith
                                (slideOutHorizontally { -it } + fadeOut())
                        else -> (fadeIn()) togetherWith (fadeOut())
                    }
                },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    Screen.MAP -> MapScreen(
                        reports = uiState.reports,
                        user = uiState.user,
                        userMyReportCount = viewModel.getUserReportCount(),
                        onReportClick = { viewModel.selectReport(it) }
                    )
                    Screen.REPORTS -> ReportsScreen(
                        reports = uiState.reports,
                        onReportClick = { viewModel.selectReport(it) }
                    )
                    Screen.REPORT_DETAIL -> ReportDetailScreen(
                        report = uiState.selectedReport,
                        onBack = { viewModel.navigateTo(Screen.REPORTS) }
                    )
                    Screen.PROFILE -> ProfileScreen(
                        user = uiState.user,
                        reports = uiState.reports,
                        onSignIn = onGoogleSignIn,
                        onSignOut = onSignOut
                    )
                }
            }
        }
    }

    if (uiState.isSubmitSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeSubmitSheet() },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            containerColor = White
        ) {
            ReportFormSheet(
                hasLocation = uiState.userLocation != null,
                isSubmitting = uiState.isSubmitting,
                errorMessage = uiState.submitError,
                onSubmit = { formData -> viewModel.submitReport(formData) }
            )
        }
    }
}

@Composable
private fun AppTopBar(
    user: com.google.firebase.auth.FirebaseUser?,
    onLogoClick: () -> Unit,
    onSignIn: () -> Unit,
    onSignOut: () -> Unit,
    onProfileClick: () -> Unit
) {
    Surface(
        color = Color.White.copy(alpha = 0.9f),
        shadowElevation = 1.dp,
        modifier = Modifier.statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(
                        onClick = onLogoClick,
                        indication = null,
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                    )
            ) {
                Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Blue600),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.WaterDrop,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        Text(
                            "Sahyadri-Siri",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color(0xFF111827)
                        )
                        Text(
                            "WESTERN GHATS WATER QUALITY",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate400,
                            letterSpacing = 1.sp
                        )
                    }
                }

            // Auth actions
            if (user != null) {
                IconButton(onClick = onProfileClick) {
                    if (user.photoUrl != null) {
                        AsyncImage(
                            model = user.photoUrl,
                            contentDescription = "Profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Gray100),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Gray500)
                        }
                    }
                }
            } else {
                TextButton(onClick = onSignIn) {
                    Text("Sign In", color = Gray600, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
private fun AppBottomBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    onAddReport: () -> Unit
) {
    Surface(
        color = White,
        shadowElevation = 8.dp,
        modifier = Modifier.navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationBar(
                modifier = Modifier.weight(1f),
                containerColor = Color.Transparent,
                tonalElevation = 0.dp
            ) {
                NavigationBarItem(
                    selected = currentScreen == Screen.MAP,
                    onClick = { onNavigate(Screen.MAP) },
                    icon = { Icon(Icons.Default.Map, contentDescription = "Map") },
                    label = { Text("Map") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Blue600,
                        selectedTextColor = Blue600,
                        indicatorColor = Blue50,
                        unselectedIconColor = Gray400,
                        unselectedTextColor = Gray400
                    )
                )
                NavigationBarItem(
                    selected = currentScreen == Screen.REPORTS,
                    onClick = { onNavigate(Screen.REPORTS) },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Reports") },
                    label = { Text("Reports") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Blue600,
                        selectedTextColor = Blue600,
                        indicatorColor = Blue50,
                        unselectedIconColor = Gray400,
                        unselectedTextColor = Gray400
                    )
                )
            }

            // FAB-style Add button
            FloatingActionButton(
                onClick = onAddReport,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(52.dp),
                shape = RoundedCornerShape(16.dp),
                containerColor = Blue600,
                contentColor = White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Report", modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Composable
private fun ProfileScreen(
    user: com.google.firebase.auth.FirebaseUser?,
    reports: List<com.sahyadrisiri.app.data.model.WaterReport>,
    onSignIn: () -> Unit,
    onSignOut: () -> Unit
) {
    com.sahyadrisiri.app.ui.screens.ProfileScreen(
        user = user,
        reports = reports,
        onSignIn = onSignIn,
        onSignOut = onSignOut
    )
}

