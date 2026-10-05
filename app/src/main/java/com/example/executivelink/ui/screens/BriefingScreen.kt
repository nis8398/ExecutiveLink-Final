package com.example.executivelink.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.executivelink.R
import com.example.executivelink.data.model.DailyBriefingEntity
import com.example.executivelink.data.model.UserProfile
import com.example.executivelink.data.model.UserRole
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
fun BriefingScreen(
    briefing: DailyBriefingEntity?,
    currentUser: UserProfile,
    onSignOffBriefing: (briefingId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0B1120))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Image Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, ExecutiveBorder, RoundedCornerShape(14.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.briefing_hero_1791204714001),
                contentDescription = "Executive Suite",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x990B1120))
                    .padding(16.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Column {
                    Text(
                        text = "EXECUTIVE DAILY BRIEFING",
                        color = ExecutiveGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = briefing?.briefingDate ?: "Monday, October 5",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Metrics Trio
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BriefingMetricCard(
                title = "Engagements",
                count = "${briefing?.meetingsCount ?: 4}",
                color = ExecutiveCyan,
                icon = Icons.Default.CalendarToday,
                modifier = Modifier.weight(1f)
            )
            BriefingMetricCard(
                title = "Approvals",
                count = "${briefing?.pendingApprovalsCount ?: 2}",
                color = ExecutiveGold,
                icon = Icons.Default.PendingActions,
                modifier = Modifier.weight(1f)
            )
            BriefingMetricCard(
                title = "Urgent SLA",
                count = "${briefing?.urgentIssuesCount ?: 1}",
                color = ExecutiveRed,
                icon = Icons.Default.PriorityHigh,
                modifier = Modifier.weight(1f)
            )
        }

        // Executive Summary Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = ExecutiveCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, ExecutiveBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Assignment,
                        contentDescription = null,
                        tint = ExecutiveGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Executive Focus & Strategic Priorities",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = briefing?.summaryHeadline
                        ?: "High-priority capital close and media briefing day. 4 scheduled engagements.",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                val priorities = briefing?.keyPriorities ?: "1. Confirm Series B indemnity revisions prior to 11:30 AM call.\n2. Review Carmel Valley Offsite deposit.\n3. Prep key message points for 3 PM Bloomberg interview."
                priorities.lines().forEach { line ->
                    if (line.isNotBlank()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 6.dp)
                                    .size(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(ExecutiveGold)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = line,
                                color = TextSecondary,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }

        // CEO Review & Sign-Off Banner
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (briefing?.reviewedByExec == true) Color(0xFF0F291E) else Color(0xFF1E283A)
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (briefing?.reviewedByExec == true) ExecutiveEmerald else ExecutiveBorder
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (briefing?.reviewedByExec == true) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = ExecutiveEmerald,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Executive Sign-Off Confirmed",
                            color = ExecutiveEmerald,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Briefing has been formally acknowledged by Executive. Prime Assistant notified.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                } else {
                    Text(
                        text = "Executive Daily Review",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Acknowledging this briefing locks morning agenda and logs the audit event.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { onSignOffBriefing(briefing?.id ?: "briefing_today") },
                        colors = ButtonDefaults.buttonColors(containerColor = ExecutiveGold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("sign_off_briefing_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF0B1120),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Acknowledge & Sign Off Briefing",
                            color = Color(0xFF0B1120),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BriefingMetricCard(
    title: String,
    count: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ExecutiveCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, ExecutiveBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = count,
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                color = TextMuted,
                fontSize = 11.sp
            )
        }
    }
}
