package com.sahyadrisiri.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sahyadrisiri.app.data.model.WaterReport
import com.sahyadrisiri.app.ui.theme.Blue500
import com.sahyadrisiri.app.ui.theme.Blue600
import com.sahyadrisiri.app.ui.theme.Gray100
import com.sahyadrisiri.app.ui.theme.Gray200
import com.sahyadrisiri.app.ui.theme.Gray300
import com.sahyadrisiri.app.ui.theme.Gray400
import com.sahyadrisiri.app.ui.theme.Gray500
import com.sahyadrisiri.app.ui.theme.Orange500
import com.sahyadrisiri.app.ui.theme.Red500
import com.sahyadrisiri.app.ui.theme.White
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReportsScreen(
    reports: List<WaterReport>,
    onReportClick: (WaterReport) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Recent Activity",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = Color(0xFF111827)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Latest community reports and AI findings.",
            fontSize = 14.sp,
            color = Gray500
        )
        Spacer(Modifier.height(20.dp))

        if (reports.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Gray100),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No reports yet. Be the first to monitor!",
                    color = Gray400,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(reports, key = { it.id }) { report ->
                    ReportListItem(report = report, onClick = { onReportClick(report) })
                }
            }
        }
    }
}

@Composable
fun ReportListItem(report: WaterReport, onClick: () -> Unit) {
    val severityColor = when (report.severity) {
        "high" -> Red500
        "medium" -> Orange500
        else -> Blue500
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Severity icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(severityColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ErrorOutline,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = report.streamName.ifBlank { "Unnamed Water Source" },
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF111827)
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.WatchLater,
                            contentDescription = null,
                            tint = Gray400,
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            " ${formatTimestamp(report.createdAt?.toDate())}",
                            fontSize = 11.sp,
                            color = Gray400,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Shield,
                            contentDescription = null,
                            tint = Gray400,
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            " ${if (report.isAnonymous) "Anonymous" else "Community Resident"}",
                            fontSize = 11.sp,
                            color = Gray400,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Gray300,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

fun formatTimestamp(date: Date?): String {
    if (date == null) return "Recently"
    val now = System.currentTimeMillis()
    val diff = now - date.time
    return when {
        diff < 60_000 -> "Just now"
        diff < 3_600_000 -> "${diff / 60_000}m ago"
        diff < 86_400_000 -> "${diff / 3_600_000}h ago"
        else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(date)
    }
}
