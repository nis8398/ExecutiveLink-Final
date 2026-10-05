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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.executivelink.data.model.ApprovalEntity
import com.example.executivelink.data.model.ApprovalStatus
import com.example.executivelink.data.model.UserProfile
import com.example.executivelink.data.model.UserRole
import com.example.executivelink.ui.components.ApprovalStatusBadge
import com.example.executivelink.ui.components.SlaCountdown
import com.example.executivelink.ui.dialogs.ApprovalDecisionDialog
import com.example.executivelink.ui.theme.ExecutiveBorder
import com.example.executivelink.ui.theme.ExecutiveCard
import com.example.executivelink.ui.theme.ExecutiveEmerald
import com.example.executivelink.ui.theme.ExecutiveGold
import com.example.executivelink.ui.theme.ExecutiveRed
import com.example.executivelink.ui.theme.TextMuted
import com.example.executivelink.ui.theme.TextPrimary
import com.example.executivelink.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ApprovalsScreen(
    approvals: List<ApprovalEntity>,
    currentUser: UserProfile,
    onDecideApproval: (approvalId: String, status: ApprovalStatus, notes: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("PENDING") }
    var activeApprovalForDecision by remember { mutableStateOf<ApprovalEntity?>(null) }

    val filteredApprovals = when (selectedFilter) {
        "PENDING" -> approvals.filter { it.status == ApprovalStatus.PENDING }
        "DECIDED" -> approvals.filter { it.status != ApprovalStatus.PENDING }
        else -> approvals
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0B1120))
    ) {
        // Filter row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                "PENDING" to "Pending Action (${approvals.count { it.status == ApprovalStatus.PENDING }})",
                "DECIDED" to "Decided History",
                "ALL" to "All (${approvals.size})"
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (filteredApprovals.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No approval requests found.",
                            color = TextMuted,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                items(filteredApprovals, key = { it.id }) { approval ->
                    ApprovalCard(
                        approval = approval,
                        currentUser = currentUser,
                        onOpenDecision = { activeApprovalForDecision = approval },
                        onQuickApprove = { onDecideApproval(approval.id, ApprovalStatus.APPROVED, null) },
                        onQuickReject = { onDecideApproval(approval.id, ApprovalStatus.REJECTED, null) }
                    )
                }
            }
        }
    }

    // Decision dialog
    activeApprovalForDecision?.let { item ->
        ApprovalDecisionDialog(
            approval = item,
            onDismiss = { activeApprovalForDecision = null },
            onDecide = { status, notes ->
                onDecideApproval(item.id, status, notes)
                activeApprovalForDecision = null
            }
        )
    }
}

@Composable
fun ApprovalCard(
    approval: ApprovalEntity,
    currentUser: UserProfile,
    onOpenDecision: () -> Unit,
    onQuickApprove: () -> Unit,
    onQuickReject: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPending = approval.status == ApprovalStatus.PENDING
    val isCeo = currentUser.role == UserRole.CEO_MD
    val dateFormat = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("approval_card_${approval.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPending && isCeo) Color(0xFF1E283A) else ExecutiveCard
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isPending) ExecutiveGold.copy(alpha = 0.5f) else ExecutiveBorder
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Category and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = approval.type.label.uppercase(),
                    color = ExecutiveGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                ApprovalStatusBadge(status = approval.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = approval.title,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            if (approval.financialImpact != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = null,
                        tint = ExecutiveEmerald,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Financial Impact: ${approval.financialImpact}",
                        color = ExecutiveEmerald,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Details
            Text(
                text = approval.details,
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Submitter & Deadline row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Requested by ${approval.requestedBy.split("(").first().trim()}",
                    color = TextMuted,
                    fontSize = 11.sp
                )

                if (isPending) {
                    SlaCountdown(deadlineTimestamp = approval.deadlineTimestamp)
                }
            }

            // Decision note if already decided
            if (approval.decisionNotes != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0F172A))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Directive (${approval.decisionBy}): \"${approval.decisionNotes}\"",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }

            // Action row for CEO
            if (isPending && isCeo) {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onOpenDecision,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ExecutiveGold),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ExecutiveGold),
                        modifier = Modifier
                            .weight(1.2f)
                            .testTag("review_decision_button_${approval.id}")
                    ) {
                        Icon(Icons.Default.Gavel, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Notes / Review", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onQuickApprove,
                        colors = ButtonDefaults.buttonColors(containerColor = ExecutiveEmerald),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("quick_approve_button_${approval.id}")
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Approve", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
