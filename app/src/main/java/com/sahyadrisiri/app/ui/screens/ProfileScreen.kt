package com.sahyadrisiri.app.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseUser
import com.sahyadrisiri.app.data.model.WaterReport
import com.sahyadrisiri.app.ui.theme.Blue50
import com.sahyadrisiri.app.ui.theme.Blue600
import com.sahyadrisiri.app.ui.theme.Gray100
import com.sahyadrisiri.app.ui.theme.Gray400
import com.sahyadrisiri.app.ui.theme.Gray500
import com.sahyadrisiri.app.ui.theme.Gray600
import com.sahyadrisiri.app.ui.theme.Red500
import com.sahyadrisiri.app.ui.theme.White

@Composable
fun ProfileScreen(
    user: FirebaseUser?,
    reports: List<WaterReport>,
    onSignIn: () -> Unit,
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        if (user == null) {
            // Not signed in state
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Gray100),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Gray400, modifier = Modifier.size(40.dp))
                }
                Spacer(Modifier.height(16.dp))
                Text("Sign in to track your reports", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(Modifier.height(8.dp))
                Text("Your reports and history will be saved.", fontSize = 14.sp, color = Gray500)
                Spacer(Modifier.height(28.dp))

                Button(
                    onClick = onSignIn,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(2.dp)
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF4285F4), modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(10.dp))
                    Text("Sign in with Google", color = Color(0xFF111827), fontWeight = FontWeight.SemiBold)
                }
            }
        } else {
            // Signed in state
            if (user.photoUrl != null) {
                AsyncImage(
                    model = user.photoUrl,
                    contentDescription = "Profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Blue50),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Blue600, modifier = Modifier.size(40.dp))
                }
            }

            Spacer(Modifier.height(14.dp))
            Text(
                user.displayName ?: "Community Member",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color(0xFF111827)
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Email, contentDescription = null, tint = Gray400, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text(user.email ?: "", fontSize = 13.sp, color = Gray500)
            }

            Spacer(Modifier.height(28.dp))

            // Stats
            Row(modifier = Modifier.fillMaxWidth()) {
                val myReports = reports.filter { it.userId == user.uid }
                StatPill(Modifier.weight(1f), "${myReports.size}", "My Reports")
                Spacer(Modifier.width(12.dp))
                StatPill(Modifier.weight(1f), "${myReports.count { it.severity == "high" }}", "High Alerts")
            }

            Spacer(Modifier.height(24.dp))

            // Sign out
            OutlinedButton(
                onClick = onSignOut,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Red500),
                border = androidx.compose.foundation.BorderStroke(1.dp, Red500.copy(alpha = 0.3f))
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Sign Out", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun StatPill(modifier: Modifier, value: String, label: String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Blue600)
            Text(label, fontSize = 12.sp, color = Gray500, fontWeight = FontWeight.Medium)
        }
    }
}
