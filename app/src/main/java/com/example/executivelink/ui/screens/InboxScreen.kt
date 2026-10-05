package com.example.executivelink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.executivelink.data.model.MessageCategory
import com.example.executivelink.data.model.MessageEntity
import com.example.executivelink.data.model.MessagePriority
import com.example.executivelink.data.model.MessageStatus
import com.example.executivelink.data.model.UserProfile
import com.example.executivelink.data.model.UserRole
import com.example.executivelink.ui.components.PriorityBadge
import com.example.executivelink.ui.components.RoleBadge
import com.example.executivelink.ui.components.SlaCountdown
import com.example.executivelink.ui.components.StatusBadge
import com.example.executivelink.ui.theme.ExecutiveBorder
import com.example.executivelink.ui.theme.ExecutiveCard
import com.example.executivelink.ui.theme.ExecutiveCyan
import com.example.executivelink.ui.theme.ExecutiveEmerald
import com.example.executivelink.ui.theme.ExecutiveGold
import com.example.executivelink.ui.theme.TextMuted
import com.example.executivelink.ui.theme.TextPrimary
import com.example.executivelink.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun InboxScreen(
    messages: List<MessageEntity>,
    currentUser: UserProfile,
    onAcknowledge: (messageId: String) -> Unit,
    onQuickSend: (content: String, priority: MessagePriority, category: MessageCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("ALL") }
    var quickInput by remember { mutableStateOf("") }

    val filteredMessages = when (selectedFilter) {
        "URGENT" -> messages.filter { it.priority == MessagePriority.URGENT }
        "UNACKED" -> messages.filter { it.status != MessageStatus.ACKNOWLEDGED && it.status != MessageStatus.ACTIONED }
        "DIRECTIVES" -> messages.filter { it.category == MessageCategory.GENERAL || it.category == MessageCategory.URGENT_BRIEF }
        else -> messages
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0B1120))
    ) {
        // Filter bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf(
                "ALL" to "All (${messages.size})",
                "URGENT" to "Urgent SLA",
                "UNACKED" to "Pending Ack",
                "DIRECTIVES" to "Directives"
            ).forEach { (key, label) ->
                val isSelected = selectedFilter == key
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) ExecutiveGold.copy(alpha = 0.2f) else ExecutiveCard)
                        .border(
                            1.dp,
                            if (isSelected) ExecutiveGold else ExecutiveBorder,
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { selectedFilter = key }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) ExecutiveGold else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        // Messages list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (filteredMessages.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No messages matching selected filter.",
                            color = TextMuted,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                items(filteredMessages, key = { it.id }) { message ->
                    MessageCard(
                        message = message,
                        currentUser = currentUser,
                        onAcknowledge = { onAcknowledge(message.id) }
                    )
                }
            }
        }

        // Quick Reply Bar at bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ExecutiveCard)
                .border(1.dp, ExecutiveBorder)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = quickInput,
                    onValueChange = { quickInput = it },
                    placeholder = {
                        Text(
                            if (currentUser.role == UserRole.CEO_MD) "Quick directive to Assistant..." else "Quick update to Executive...",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("quick_reply_input"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ExecutiveGold,
                        unfocusedBorderColor = ExecutiveBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (quickInput.isNotBlank()) {
                            onQuickSend(
                                quickInput.trim(),
                                MessagePriority.IMPORTANT,
                                MessageCategory.GENERAL
                            )
                            quickInput = ""
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(ExecutiveGold)
                        .testTag("quick_send_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color(0xFF0B1120),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MessageCard(
    message: MessageEntity,
    currentUser: UserProfile,
    onAcknowledge: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isFromMe = message.senderRole == currentUser.role
    val needsMyAck = !isFromMe && message.status != MessageStatus.ACKNOWLEDGED && message.status != MessageStatus.ACTIONED
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val formattedTime = timeFormat.format(Date(message.timestamp))

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("message_card_${message.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (message.priority == MessagePriority.URGENT && needsMyAck)
                Color(0xFF2A1518) else ExecutiveCard
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (message.priority == MessagePriority.URGENT && needsMyAck)
                ExecutiveCyan.copy(alpha = 0.5f) else ExecutiveBorder
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Sender, Role, Time, Priority
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isFromMe) "You (${message.senderName})" else message.senderName,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    RoleBadge(role = message.senderRole)
                }
                Text(
                    text = formattedTime,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Priority and SLA Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PriorityBadge(priority = message.priority)
                if (message.deadlineTimestamp != null) {
                    SlaCountdown(deadlineTimestamp = message.deadlineTimestamp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Body
            Text(
                text = message.content,
                color = TextPrimary,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Footer: Status and Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = message.status)

                if (needsMyAck) {
                    Button(
                        onClick = onAcknowledge,
                        colors = ButtonDefaults.buttonColors(containerColor = ExecutiveEmerald),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("ack_button_${message.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Acknowledge",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else if (message.acknowledgedAt != null) {
                    Text(
                        text = "Ack by ${message.acknowledgedBy ?: "recipient"}",
                        color = ExecutiveEmerald,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
