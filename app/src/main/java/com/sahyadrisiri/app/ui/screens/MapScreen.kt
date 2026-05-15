package com.sahyadrisiri.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.sahyadrisiri.app.data.model.WaterReport
import com.sahyadrisiri.app.ui.theme.Blue600
import com.sahyadrisiri.app.ui.theme.Gray200
import com.sahyadrisiri.app.ui.theme.Gray500
import com.sahyadrisiri.app.ui.theme.Orange500
import com.sahyadrisiri.app.ui.theme.Red500
import com.sahyadrisiri.app.ui.theme.White


@Composable
fun MapScreen(
    reports: List<WaterReport>,
    user: FirebaseUser?,
    userMyReportCount: Int,
    onReportClick: (WaterReport) -> Unit
) {
    val sahyadriCenter = LatLng(14.8, 74.8)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(sahyadriCenter, 8f)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Text(
            "Health Map",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = Color(0xFF111827)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Live water quality status across the Sahyadri region.",
            fontSize = 14.sp,
            color = Gray500
        )

        Spacer(Modifier.height(16.dp))

        // Legend card
        Card(
            colors = CardDefaults.cardColors(containerColor = White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem(color = Blue600, label = "Clean")
                Box(modifier = Modifier
                    .width(1.dp)
                    .height(20.dp)
                    .background(Gray200))
                LegendItem(color = Orange500, label = "Turbid")
                Box(modifier = Modifier
                    .width(1.dp)
                    .height(20.dp)
                    .background(Gray200))
                LegendItem(color = Red500, label = "Muddy")
            }
        }

        Spacer(Modifier.height(16.dp))

        // Map
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(zoomControlsEnabled = false),
                properties = MapProperties(
                    mapStyleOptions = null
                    ),
                googleMapOptionsFactory = {
                    GoogleMapOptions()
                        .mapId("f900ef20a301a6b42d7a2fd5")
                }
            ) {
                reports.forEach { report ->
                    val location = report.location ?: return@forEach
                    val markerColor = when (report.severity) {
                        "high" -> BitmapDescriptorFactory.HUE_RED
                        "medium" -> BitmapDescriptorFactory.HUE_ORANGE
                        else -> BitmapDescriptorFactory.HUE_AZURE
                    }
                    Marker(
                        state = MarkerState(LatLng(location.latitude, location.longitude)),
                        title = report.streamName.ifBlank { "Unnamed Water Source" },
                        snippet = "Severity: ${report.severity}",
                        icon = BitmapDescriptorFactory.defaultMarker(markerColor),
                        onClick = {
                            onReportClick(report)
                            true
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Stats cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                label = "TOTAL REPORTS",
                value = reports.size.toString(),
                valueColor = Color(0xFF111827),
                containerColor = White
            )
            StatCard(
                modifier = Modifier.weight(1f),
                label = "ACTIVE ALERTS",
                value = reports.count { it.severity == "high" }.toString(),
                valueColor = Red500,
                containerColor = White
            )
            StatCard(
                modifier = Modifier.weight(1f),
                label = "MY REPORTS",
                value = userMyReportCount.toString(),
                valueColor = White,
                containerColor = Blue600
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(Modifier.width(6.dp))
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Gray500)
    }
}

@Composable
private fun StatCard(
    modifier: Modifier,
    label: String,
    value: String,
    valueColor: Color,
    containerColor: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                label,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = if (containerColor == Blue600) Color(0xFFBFDBFE) else Gray500,
                letterSpacing = 0.8.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
        }
    }
}
