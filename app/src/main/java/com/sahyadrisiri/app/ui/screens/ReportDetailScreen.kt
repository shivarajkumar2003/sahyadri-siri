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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.sahyadrisiri.app.ui.theme.Amber100
import com.sahyadrisiri.app.ui.theme.AmberWarning
import com.sahyadrisiri.app.ui.theme.Blue100
import com.sahyadrisiri.app.ui.theme.Blue200
import com.sahyadrisiri.app.ui.theme.Blue50
import com.sahyadrisiri.app.ui.theme.Blue500
import com.sahyadrisiri.app.ui.theme.Blue600
import com.sahyadrisiri.app.ui.theme.Gray50
import com.sahyadrisiri.app.ui.theme.Gray500
import com.sahyadrisiri.app.ui.theme.Orange100
import com.sahyadrisiri.app.ui.theme.Orange500
import com.sahyadrisiri.app.ui.theme.Red100
import com.sahyadrisiri.app.ui.theme.Red500
import com.sahyadrisiri.app.ui.theme.Slate400
import com.sahyadrisiri.app.ui.theme.Slate800
import com.sahyadrisiri.app.ui.theme.Slate900
import com.sahyadrisiri.app.ui.theme.White

@Composable
fun ReportDetailScreen(
    report: WaterReport?,
    onBack: () -> Unit
) {
    if (report == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Report not found", color = Gray500)
        }
        return
    }

    val severityBg = when (report.severity) {
        "high" -> Color(0xFFFFF1F2)
        "medium" -> Color(0xFFFFF7ED)
        else -> Blue50
    }
    val severityColor = when (report.severity) {
        "high" -> Red500
        "medium" -> Orange500
        else -> Blue600
    }
    val severityBadgeBg = when (report.severity) {
        "high" -> Red100
        "medium" -> Orange100
        else -> Blue100
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Back button
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Blue600)
            }
            Text(
                "Back to Feed",
                color = Blue600,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            // Header section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(severityBg)
                    .padding(24.dp)
            ) {
                // Severity badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(severityBadgeBg)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${report.severity.uppercase()} SEVERITY ALERT",
                        color = severityColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = report.streamName.ifBlank { "Unnamed Water Source" },
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF111827),
                    lineHeight = 30.sp
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    report.location?.let { loc ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Gray500,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                " ${String.format("%.4f", loc.latitude)}, ${String.format("%.4f", loc.longitude)}",
                                fontSize = 12.sp,
                                color = Gray500
                            )
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.WatchLater,
                            contentDescription = null,
                            tint = Gray500,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            " ${formatTimestamp(report.createdAt?.toDate())}",
                            fontSize = 12.sp,
                            color = Gray500
                        )
                    }
                }
            }

            // Body
            Column(modifier = Modifier.padding(24.dp)) {

                // Observation Details
                SectionLabel("OBSERVATION DETAILS")
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ObsCard(Modifier.weight(1f), "CLARITY", report.clarityRating.replaceFirstChar { it.uppercase() })
                    ObsCard(Modifier.weight(1f), "FLOW SPEED", report.flowCondition.replaceFirstChar { it.uppercase() })
                }

                if (report.smell.isNotBlank()) {
                    Spacer(Modifier.height(10.dp))
                    ObsCard(Modifier.fillMaxWidth(), "SMELL", report.smell)
                }

                if (report.pollutionDetails.isNotBlank()) {
                    Spacer(Modifier.height(20.dp))
                    SectionLabel("VISUAL POLLUTION")
                    Spacer(Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFFFBEB))
                            .padding(14.dp)
                    ) {
                        Text(
                            report.pollutionDetails,
                            color = Color(0xFF78350F),
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                // AI Analysis panel
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Slate900)
                        .padding(24.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Blue500),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "GENAI SMART ANALYSIS",
                                color = Blue200,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.8.sp
                            )
                        }

                        Spacer(Modifier.height(20.dp))

                        if (report.aiSummary.isNotBlank()) {
                            AiSection("CONDITION SUMMARY", report.aiSummary)
                            Spacer(Modifier.height(16.dp))
                            AiSection("HEALTH & ECOLOGICAL RISKS", report.aiRisks)

                            Spacer(Modifier.height(20.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color(0xFF1E293B))
                            )
                            Spacer(Modifier.height(20.dp))

                            Text(
                                "VILLAGE ACTION PLAN",
                                color = Blue200,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF1E293B))
                                    .padding(14.dp)
                            ) {
                                Text(
                                    report.aiActions,
                                    color = White,
                                    fontSize = 13.sp,
                                    lineHeight = 20.sp
                                )
                            }
                        } else {
                            Text(
                                "AI analysis was not enabled for this report.",
                                color = Slate400,
                                fontSize = 14.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = Gray500,
        letterSpacing = 1.sp
    )
}

@Composable
private fun ObsCard(modifier: Modifier, label: String, value: String) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Gray50)
            .padding(14.dp)
    ) {
        Column {
            Text(label, fontSize = 9.sp, color = Gray500, fontWeight = FontWeight.ExtraBold, letterSpacing = 0.8.sp)
            Spacer(Modifier.height(4.dp))
            Text(value, fontWeight = FontWeight.Bold, color = Color(0xFF111827), fontSize = 14.sp)
        }
    }
}

@Composable
private fun AiSection(label: String, content: String) {
    Column {
        Text(
            label,
            color = White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
        Spacer(Modifier.height(6.dp))
        Text(
            content,
            color = Slate400,
            fontSize = 13.sp,
            lineHeight = 20.sp
        )
    }
}
