package com.example.executivelink.ui.dialogs

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.executivelink.data.model.ApprovalEntity
import com.example.executivelink.data.model.ApprovalStatus
import com.example.executivelink.data.model.ApprovalType
import com.example.executivelink.data.model.CalendarEventEntity
import com.example.executivelink.data.model.MessageCategory
import com.example.executivelink.data.model.MessagePriority
import com.example.executivelink.ui.theme.ExecutiveBorder
import com.example.executivelink.ui.theme.ExecutiveCard
import com.example.executivelink.ui.theme.ExecutiveCyan
import com.example.executivelink.ui.theme.ExecutiveEmerald
import com.example.executivelink.ui.theme.ExecutiveGold
import com.example.executivelink.ui.theme.ExecutiveRed
import com.example.executivelink.ui.theme.TextMuted
import com.example.executivelink.ui.theme.TextPrimary
import com.example.executivelink.ui.theme.TextSecondary

@Composable
fun ComposeMessageDialog(
    onDismiss: () -> Unit,
    onSend: (content: String, priority: MessagePriority, category: MessageCategory, deadlineMinutes: Int?) -> Unit
) {
    var content by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(MessagePriority.URGENT) }
    var selectedCategory by remember { mutableStateOf(MessageCategory.GENERAL) }
    var deadlineMinutes by remember { mutableIntStateOf(30) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ExecutiveCard),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, ExecutiveBorder, RoundedCornerShape(16.dp))
                .testTag("compose_message_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Compose Directive",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Priority Level", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MessagePriority.values().forEach { priority ->
                        val isSelected = selectedPriority == priority
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) ExecutiveGold.copy(alpha = 0.2f) else Color(0xFF0F172A))
                                .border(
                                    1.dp,
                                    if (isSelected) ExecutiveGold else ExecutiveBorder,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedPriority = priority }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = priority.label,
                                color = if (isSelected) ExecutiveGold else TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                if (selectedPriority == MessagePriority.URGENT) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Response SLA Deadline", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(15 to "15m", 30 to "30m", 60 to "1h", 120 to "2h").forEach { (mins, label) ->
                            val isSel = deadlineMinutes == mins
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) ExecutiveRed.copy(alpha = 0.2f) else Color(0xFF0F172A))
                                    .border(
                                        1.dp,
                                        if (isSel) ExecutiveRed else ExecutiveBorder,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { deadlineMinutes = mins }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSel) ExecutiveRed else TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("Category", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(
                        MessageCategory.GENERAL to "Directive",
                        MessageCategory.APPROVAL_REQ to "Approval",
                        MessageCategory.CALENDAR_SYNC to "Calendar",
                        MessageCategory.URGENT_BRIEF to "Brief"
                    ).forEach { (cat, label) ->
                        val isSel = selectedCategory == cat
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) ExecutiveCyan.copy(alpha = 0.2f) else Color(0xFF0F172A))
                                .border(
                                    1.dp,
                                    if (isSel) ExecutiveCyan else ExecutiveBorder,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedCategory = cat }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = if (isSel) ExecutiveCyan else TextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Message & Action Context") },
                    placeholder = { Text("Enter executive directive or coordination instruction...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .testTag("message_input_field"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ExecutiveGold,
                        unfocusedBorderColor = ExecutiveBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    OutlinedButton(onClick = onDismiss) {
                        Text("Cancel", color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (content.isNotBlank()) {
                                onSend(
                                    content.trim(),
                                    selectedPriority,
                                    selectedCategory,
                                    if (selectedPriority == MessagePriority.URGENT) deadlineMinutes else null
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ExecutiveGold),
                        enabled = content.isNotBlank(),
                        modifier = Modifier.testTag("send_message_button")
                    ) {
                        Text("Dispatch Message", color = Color(0xFF0B1120), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun NewApprovalDialog(
    onDismiss: () -> Unit,
    onSubmit: (title: String, details: String, type: ApprovalType, deadlineHours: Int, financialImpact: String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var financialImpact by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(ApprovalType.CONTRACT) }
    var deadlineHours by remember { mutableIntStateOf(4) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ExecutiveCard),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, ExecutiveBorder, RoundedCornerShape(16.dp))
                .testTag("new_approval_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Submit Approval Request",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Decision Title") },
                    placeholder = { Text("e.g. Q4 Infrastructure Cloud Expansion") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ExecutiveGold,
                        unfocusedBorderColor = ExecutiveBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text("Category", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    ApprovalType.values().forEach { type ->
                        val isSel = selectedType == type
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) ExecutiveGold.copy(alpha = 0.2f) else Color(0xFF0F172A))
                                .border(
                                    1.dp,
                                    if (isSel) ExecutiveGold else ExecutiveBorder,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedType = type }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = type.label.split("/").first().trim(),
                                color = if (isSel) ExecutiveGold else TextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = financialImpact,
                    onValueChange = { financialImpact = it },
                    label = { Text("Financial / Resource Impact (Optional)") },
                    placeholder = { Text("e.g. $45,000 or N/A") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ExecutiveGold,
                        unfocusedBorderColor = ExecutiveBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = details,
                    onValueChange = { details = it },
                    label = { Text("Full Context & Justification") },
                    placeholder = { Text("Provide background, risks, and deadline justification...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ExecutiveGold,
                        unfocusedBorderColor = ExecutiveBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    OutlinedButton(onClick = onDismiss) {
                        Text("Cancel", color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank() && details.isNotBlank()) {
                                onSubmit(
                                    title.trim(),
                                    details.trim(),
                                    selectedType,
                                    deadlineHours,
                                    financialImpact.trim().ifBlank { null }
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ExecutiveGold),
                        enabled = title.isNotBlank() && details.isNotBlank(),
                        modifier = Modifier.testTag("submit_approval_button")
                    ) {
                        Text("Route to CEO", color = Color(0xFF0B1120), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ApprovalDecisionDialog(
    approval: ApprovalEntity,
    onDismiss: () -> Unit,
    onDecide: (status: ApprovalStatus, notes: String?) -> Unit
) {
    var notes by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ExecutiveCard),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, ExecutiveBorder, RoundedCornerShape(16.dp))
                .testTag("approval_decision_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Executive Action",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = approval.title,
                    fontWeight = FontWeight.Bold,
                    color = ExecutiveGold,
                    fontSize = 15.sp
                )
                if (approval.financialImpact != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Impact: ${approval.financialImpact}",
                        color = ExecutiveEmerald,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = approval.details,
                    color = TextSecondary,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Executive Directive / Conditions (Optional)") },
                    placeholder = { Text("e.g. Approved subject to legal sign-off on section 8...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ExecutiveGold,
                        unfocusedBorderColor = ExecutiveBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onDecide(ApprovalStatus.REJECTED, notes.ifBlank { null }) },
                        colors = ButtonDefaults.buttonColors(containerColor = ExecutiveRed),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("reject_approval_button")
                    ) {
                        Text("Reject", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onDecide(ApprovalStatus.CLARIFICATION, notes.ifBlank { "Clarification needed" }) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Clarify", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = { onDecide(ApprovalStatus.APPROVED, notes.ifBlank { null }) },
                        colors = ButtonDefaults.buttonColors(containerColor = ExecutiveEmerald),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("approve_approval_button")
                    ) {
                        Text("Approve", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun NewEventDialog(
    onDismiss: () -> Unit,
    onCreate: (title: String, startTime: Long, endTime: Long, location: String, isVirtual: Boolean, meetingLink: String?, purpose: String, attendees: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var isVirtual by remember { mutableStateOf(false) }
    var meetingLink by remember { mutableStateOf("") }
    var purpose by remember { mutableStateOf("") }
    var attendees by remember { mutableStateOf("") }

    val now = System.currentTimeMillis()
    val startTime = now + 60 * 60 * 1000L
    val endTime = startTime + 45 * 60 * 1000L

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ExecutiveCard),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, ExecutiveBorder, RoundedCornerShape(16.dp))
                .testTag("new_event_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Schedule Appointment",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Meeting Title") },
                    placeholder = { Text("e.g. Audit Committee Review") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ExecutiveGold,
                        unfocusedBorderColor = ExecutiveBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location / Suite") },
                    placeholder = { Text("e.g. Boardroom B or Virtual") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ExecutiveGold,
                        unfocusedBorderColor = ExecutiveBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = attendees,
                    onValueChange = { attendees = it },
                    label = { Text("Attendees") },
                    placeholder = { Text("e.g. Sarah Jenkins (CEO), General Counsel") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ExecutiveGold,
                        unfocusedBorderColor = ExecutiveBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = purpose,
                    onValueChange = { purpose = it },
                    label = { Text("Objective / Agenda Notes") },
                    placeholder = { Text("Key objectives, required decisions...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ExecutiveGold,
                        unfocusedBorderColor = ExecutiveBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    OutlinedButton(onClick = onDismiss) {
                        Text("Cancel", color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onCreate(
                                    title.trim(),
                                    startTime,
                                    endTime,
                                    location.ifBlank { "Executive Office" },
                                    isVirtual,
                                    if (isVirtual) meetingLink.ifBlank { null } else null,
                                    purpose.ifBlank { "Standard Briefing" },
                                    attendees.ifBlank { "Executive Team" }
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ExecutiveGold),
                        enabled = title.isNotBlank(),
                        modifier = Modifier.testTag("create_event_submit_button")
                    ) {
                        Text("Add to Agenda", color = Color(0xFF0B1120), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun RescheduleEventDialog(
    event: CalendarEventEntity,
    onDismiss: () -> Unit,
    onReschedule: (newStartTime: Long, newEndTime: Long, reason: String) -> Unit
) {
    var reason by remember { mutableStateOf("") }
    var offsetMinutes by remember { mutableIntStateOf(30) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ExecutiveCard),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, ExecutiveBorder, RoundedCornerShape(16.dp))
                .testTag("reschedule_event_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Reschedule Appointment",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = event.title,
                    fontWeight = FontWeight.Bold,
                    color = ExecutiveGold,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(14.dp))
                Text("Time Shift", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(15 to "+15m", 30 to "+30m", 60 to "+1h", 120 to "+2h").forEach { (mins, label) ->
                        val isSel = offsetMinutes == mins
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) ExecutiveCyan.copy(alpha = 0.2f) else Color(0xFF0F172A))
                                .border(
                                    1.dp,
                                    if (isSel) ExecutiveCyan else ExecutiveBorder,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { offsetMinutes = mins }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = if (isSel) ExecutiveCyan else TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Reason for Rescheduling") },
                    placeholder = { Text("e.g. Conflict with urgent investor briefing...") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ExecutiveGold,
                        unfocusedBorderColor = ExecutiveBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    OutlinedButton(onClick = onDismiss) {
                        Text("Cancel", color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val newStart = event.startTime + offsetMinutes * 60 * 1000L
                            val duration = event.endTime - event.startTime
                            val newEnd = newStart + duration
                            onReschedule(newStart, newEnd, reason.ifBlank { "Schedule optimization" })
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ExecutiveGold),
                        modifier = Modifier.testTag("confirm_reschedule_button")
                    ) {
                        Text("Apply & Notify", color = Color(0xFF0B1120), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
