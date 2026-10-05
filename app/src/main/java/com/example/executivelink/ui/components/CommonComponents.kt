package com.example.executivelink.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.executivelink.data.model.ApprovalStatus
import com.example.executivelink.data.model.EventStatus
import com.example.executivelink.data.model.MessagePriority
import com.example.executivelink.data.model.MessageStatus
import com.example.executivelink.data.model.UserRole
import com.example.executivelink.ui.theme.ExecutiveBorder
import com.example.executivelink.ui.theme.ExecutiveCyan
import com.example.executivelink.ui.theme.ExecutiveEmerald
import com.example.executivelink.ui.theme.ExecutiveGold
import com.example.executivelink.ui.theme.ExecutiveRed
import com.example.executivelink.ui.theme.StatusApprovedBg
import com.example.executivelink.ui.theme.StatusApprovedText
import com.example.executivelink.ui.theme.StatusPendingBg
import com.example.executivelink.ui.theme.StatusPendingText
import com.example.executivelink.ui.theme.StatusUrgentBg
import com.example.executivelink.ui.theme.StatusUrgentText
import com.example.executivelink.ui.theme.TextMuted
import com.example.executivelink.ui.theme.TextPrimary
import com.example.executivelink.ui.theme.TextSecondary

@Composable
fun RoleBadge(
    role: UserRole,
    modifier: Modifier = Modifier
) {
    val (bg, text, label) = when (role) {
        UserRole.CEO_MD -> Triple(
            Color(0xFF3B2807),
            ExecutiveGold,
            "CEO / MD"
        )
        UserRole.PRIME_ASSISTANT -> Triple(
            Color(0xFF0C2A40),
            ExecutiveCyan,
            "PRIME ASSISTANT"
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .border(1.dp, text.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun PriorityBadge(
    priority: MessagePriority,
    modifier: Modifier = Modifier
) {
    val (bg, text, icon) = when (priority) {
        MessagePriority.URGENT -> Triple(StatusUrgentBg, StatusUrgentText, Icons.Default.Warning)
        MessagePriority.IMPORTANT -> Triple(StatusPendingBg, StatusPendingText, Icons.Default.Pending)
        MessagePriority.ROUTINE -> Triple(Color(0xFF1E293B), TextSecondary, Icons.Default.AccessTime)
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .border(1.dp, text.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = text,
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = priority.label.uppercase(),
            color = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun StatusBadge(
    status: MessageStatus,
    modifier: Modifier = Modifier
) {
    val (color, label) = when (status) {
        MessageStatus.SENT -> Pair(TextMuted, "Sent")
        MessageStatus.DELIVERED -> Pair(ExecutiveCyan, "Delivered")
        MessageStatus.READ -> Pair(ExecutiveCyan, "Read")
        MessageStatus.ACKNOWLEDGED -> Pair(ExecutiveEmerald, "Acknowledged")
        MessageStatus.ACTIONED -> Pair(ExecutiveGold, "Actioned")
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ApprovalStatusBadge(
    status: ApprovalStatus,
    modifier: Modifier = Modifier
) {
    val (bg, text, icon) = when (status) {
        ApprovalStatus.PENDING -> Triple(StatusPendingBg, StatusPendingText, Icons.Default.Pending)
        ApprovalStatus.APPROVED -> Triple(StatusApprovedBg, StatusApprovedText, Icons.Default.CheckCircle)
        ApprovalStatus.REJECTED -> Triple(StatusUrgentBg, StatusUrgentText, Icons.Default.Error)
        ApprovalStatus.CLARIFICATION -> Triple(Color(0x2638BDF8), ExecutiveCyan, Icons.Default.Pending)
        ApprovalStatus.DEFERRED -> Triple(Color(0x26A855F7), Color(0xFFC084FC), Icons.Default.AccessTime)
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .border(1.dp, text.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = text,
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = status.label,
            color = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun EventStatusBadge(
    status: EventStatus,
    modifier: Modifier = Modifier
) {
    val (bg, text) = when (status) {
        EventStatus.CONFIRMED -> Pair(StatusApprovedBg, StatusApprovedText)
        EventStatus.PENDING_CONFIRMATION -> Pair(StatusPendingBg, StatusPendingText)
        EventStatus.RESCHEDULED -> Pair(Color(0x2638BDF8), ExecutiveCyan)
        EventStatus.CANCELLED -> Pair(StatusUrgentBg, StatusUrgentText)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = status.label,
            color = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SlaCountdown(
    deadlineTimestamp: Long?,
    modifier: Modifier = Modifier
) {
    if (deadlineTimestamp == null) return
    val remaining = deadlineTimestamp - System.currentTimeMillis()
    val isOverdue = remaining <= 0
    val minutes = kotlin.math.abs(remaining) / (60 * 1000)
    val hours = minutes / 60

    val (bg, text, textLabel) = if (isOverdue) {
        Triple(StatusUrgentBg, StatusUrgentText, "OVERDUE (${minutes}m)")
    } else if (minutes < 60) {
        Triple(StatusUrgentBg, StatusUrgentText, "${minutes}m SLA REMAINING")
    } else {
        Triple(StatusPendingBg, StatusPendingText, "${hours}h ${minutes % 60}m SLA")
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bg)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = null,
            tint = text,
            modifier = Modifier.size(11.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = textLabel,
            color = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
