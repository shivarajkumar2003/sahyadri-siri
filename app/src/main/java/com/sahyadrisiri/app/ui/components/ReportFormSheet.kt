package com.sahyadrisiri.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material.icons.filled.WindPower
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sahyadrisiri.app.data.model.ReportFormData
import com.sahyadrisiri.app.ui.theme.Blue50
import com.sahyadrisiri.app.ui.theme.Blue600
import com.sahyadrisiri.app.ui.theme.Gray100
import com.sahyadrisiri.app.ui.theme.Gray200
import com.sahyadrisiri.app.ui.theme.Gray500
import com.sahyadrisiri.app.ui.theme.Gray600
import com.sahyadrisiri.app.ui.theme.Gray700
import com.sahyadrisiri.app.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportFormSheet(
    hasLocation: Boolean,
    isSubmitting: Boolean,
    errorMessage: String?,
    onSubmit: (ReportFormData) -> Unit
) {
    var streamName by remember { mutableStateOf("") }
    var clarityRating by remember { mutableStateOf("clear") }
    var flowCondition by remember { mutableStateOf("normal") }
    var smell by remember { mutableStateOf("") }
    var pollutionDetails by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(false) }

    var clarityExpanded by remember { mutableStateOf(false) }
    var flowExpanded by remember { mutableStateOf(false) }

    val clarityOptions = listOf("clear" to "Clear", "turbid" to "Turbid (Cloudy)", "muddy" to "Muddy / Brown")
    val flowOptions = listOf("stagnant" to "Stagnant", "normal" to "Normal", "fast" to "Fast")

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Blue600,
        unfocusedBorderColor = Gray200,
        focusedContainerColor = White,
        unfocusedContainerColor = Gray100,
        focusedTextColor = Color(0xFF111827),
        unfocusedTextColor = Color(0xFF111827)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 4.dp)
    ) {
        // Sheet handle
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(50))
                .background(Gray200)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("New Report", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color(0xFF111827))
                Text("Help protect your local water source.", fontSize = 13.sp, color = Gray500)
            }
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Blue50),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Blue600, modifier = Modifier.size(22.dp))
            }
        }

        Spacer(Modifier.height(24.dp))

        // Stream name
        FormLabel("Stream / Source Name")
        OutlinedTextField(
            value = streamName,
            onValueChange = { streamName = it },
            placeholder = { Text("e.g. Kali River near junction", color = Gray500) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = fieldColors,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        Spacer(Modifier.height(16.dp))

        // Clarity + Flow row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(Modifier.weight(1f)) {
                FormLabelWithIcon("Water Clarity", Icons.Default.WaterDrop, Blue600)
                ExposedDropdownMenuBox(
                    expanded = clarityExpanded,
                    onExpandedChange = { clarityExpanded = it }
                ) {
                    OutlinedTextField(
                        value = clarityOptions.first { it.first == clarityRating }.second,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = clarityExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = fieldColors,
                        singleLine = true
                    )
                    ExposedDropdownMenu(
                        expanded = clarityExpanded,
                        onDismissRequest = { clarityExpanded = false }
                    ) {
                        clarityOptions.forEach { (value, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = { clarityRating = value; clarityExpanded = false }
                            )
                        }
                    }
                }
            }

            Column(Modifier.weight(1f)) {
                FormLabelWithIcon("Flow Speed", Icons.Default.Waves, Blue600)
                ExposedDropdownMenuBox(
                    expanded = flowExpanded,
                    onExpandedChange = { flowExpanded = it }
                ) {
                    OutlinedTextField(
                        value = flowOptions.first { it.first == flowCondition }.second,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = flowExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = fieldColors,
                        singleLine = true
                    )
                    ExposedDropdownMenu(
                        expanded = flowExpanded,
                        onDismissRequest = { flowExpanded = false }
                    ) {
                        flowOptions.forEach { (value, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = { flowCondition = value; flowExpanded = false }
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Smell
        FormLabelWithIcon("Noticeable Smell (Optional)", Icons.Default.WindPower, Gray500)
        OutlinedTextField(
            value = smell,
            onValueChange = { smell = it },
            placeholder = { Text("e.g. Foul, Chemical, None", color = Gray500) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = fieldColors,
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        // Pollution
        FormLabelWithIcon("Pollution Details", Icons.Default.Warning, Color(0xFFF59E0B))
        OutlinedTextField(
            value = pollutionDetails,
            onValueChange = { pollutionDetails = it },
            placeholder = { Text("Plastic waste, factory discharge, oil spills...", color = Gray500) },
            modifier = Modifier.fillMaxWidth().height(100.dp),
            shape = RoundedCornerShape(10.dp),
            colors = fieldColors,
            maxLines = 4
        )

        Spacer(Modifier.height(16.dp))

        // Anonymous toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .clickable { isAnonymous = !isAnonymous }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (isAnonymous) Blue600 else White)
                    .border(1.dp, if (isAnonymous) Blue600 else Gray200, RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (isAnonymous) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = White, modifier = Modifier.size(14.dp))
                }
            }
            Spacer(Modifier.width(10.dp))
            Text("Submit anonymously", fontSize = 14.sp, color = Gray600)
        }

        Spacer(Modifier.height(20.dp))

        if (!hasLocation) {
            Text(
                "Fetching location...",
                color = Color(0xFFF59E0B),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        errorMessage?.let {
            Text(it, color = Color(0xFFEF4444), fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
        }

        Button(
            onClick = {
                onSubmit(
                    ReportFormData(
                        streamName = streamName,
                        clarityRating = clarityRating,
                        flowCondition = flowCondition,
                        smell = smell,
                        pollutionDetails = pollutionDetails,
                        isAnonymous = isAnonymous
                    )
                )
            },
            enabled = !isSubmitting && hasLocation,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue600,
                disabledContainerColor = Gray100
            )
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Blue600, strokeWidth = 2.dp)
                Spacer(Modifier.width(10.dp))
                Text("Analyzing & Submitting...", color = Gray500, fontWeight = FontWeight.SemiBold)
            } else {
                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Submit Report", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun FormLabel(text: String) {
    Text(
        text,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = Gray700,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun FormLabelWithIcon(text: String, icon: ImageVector, iconTint: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 6.dp)
    ) {
        Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(14.dp))
        Spacer(Modifier.width(4.dp))
        Text(text, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Gray700)
    }
}
