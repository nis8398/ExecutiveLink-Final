package com.example.executivelink.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.executivelink.R
import com.example.executivelink.data.model.UserRole
import com.example.executivelink.ui.components.RoleBadge
import com.example.executivelink.ui.dialogs.ComposeMessageDialog
import com.example.executivelink.ui.dialogs.NewApprovalDialog
import com.example.executivelink.ui.dialogs.NewEventDialog
import com.example.executivelink.ui.screens.ApprovalsScreen
import com.example.executivelink.ui.screens.AuditScreen
import com.example.executivelink.ui.screens.BriefingScreen
import com.example.executivelink.ui.screens.CalendarScreen
import com.example.executivelink.ui.screens.InboxScreen
import com.example.executivelink.ui.theme.ExecutiveBorder
import com.example.executivelink.ui.theme.ExecutiveCard
import com.example.executivelink.ui.theme.ExecutiveCyan
import com.example.executivelink.ui.theme.ExecutiveGold
import com.example.executivelink.ui.theme.ExecutiveRed
import com.example.executivelink.ui.theme.ExecutiveSurface
import com.example.executivelink.ui.theme.TextMuted
import com.example.executivelink.ui.theme.TextPrimary
import com.example.executivelink.ui.theme.TextSecondary
import com.example.executivelink.ui.viewmodel.ExecutiveViewModel

@Composable
fun ExecutiveMainScreen(
    viewModel: ExecutiveViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val approvals by viewModel.approvals.collectAsStateWithLifecycle()
    val calendarEvents by viewModel.calendarEvents.collectAsStateWithLifecycle()
    val auditLogs by viewModel.auditLogs.collectAsStateWithLifecycle()
    val latestBriefing by viewModel.latestBriefing.collectAsStateWithLifecycle()
    val urgentCount by viewModel.urgentCount.collectAsStateWithLifecycle()
    val pendingApprovalCount by viewModel.pendingApprovalCount.collectAsStateWithLifecycle()

    var currentTab by remember { mutableIntStateOf(0) }
    var showComposeDialog by remember { mutableStateOf(false) }
    var showNewApprovalDialog by remember { mutableStateOf(false) }
    var showNewEventDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFF0B1120),
        topBar = {
            ExecutiveTopBar(
                currentUser = currentUser,
                onSwitchRole = { viewModel.toggleRole() }
            )
        },
        bottomBar = {
            ExecutiveBottomBar(
                currentTab = currentTab,
                onSelectTab = { currentTab = it },
                urgentBadgeCount = urgentCount,
                pendingApprovalCount = pendingApprovalCount
            )
        },
        floatingActionButton = {
            when (currentTab) {
                0 -> {
                    FloatingActionButton(
                        onClick = { showComposeDialog = true },
                        containerColor = ExecutiveGold,
                        contentColor = Color(0xFF0B1120),
                        modifier = Modifier.testTag("fab_compose")
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Compose Directive")
                    }
                }
                1 -> {
                    FloatingActionButton(
                        onClick = { showNewApprovalDialog = true },
                        containerColor = ExecutiveGold,
                        contentColor = Color(0xFF0B1120),
                        modifier = Modifier.testTag("fab_new_approval")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "New Approval")
                    }
                }
                2 -> {
                    FloatingActionButton(
                        onClick = { showNewEventDialog = true },
                        containerColor = ExecutiveGold,
                        contentColor = Color(0xFF0B1120),
                        modifier = Modifier.testTag("fab_new_event")
                    ) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "New Event")
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                0 -> InboxScreen(
                    messages = messages,
                    currentUser = currentUser,
                    onAcknowledge = { viewModel.acknowledgeMessage(it) },
                    onQuickSend = { content, priority, cat ->
                        viewModel.sendMessage(content, priority, cat)
                    }
                )
                1 -> ApprovalsScreen(
                    approvals = approvals,
                    currentUser = currentUser,
                    onDecideApproval = { id, status, notes ->
                        viewModel.decideApproval(id, status, notes)
                    }
                )
                2 -> CalendarScreen(
                    events = calendarEvents,
                    currentUser = currentUser,
                    onRescheduleEvent = { id, start, end, reason ->
                        viewModel.rescheduleCalendarEvent(id, start, end, reason)
                    },
                    onConfirmEvent = { viewModel.confirmCalendarEvent(it) }
                )
                3 -> BriefingScreen(
                    briefing = latestBriefing,
                    currentUser = currentUser,
                    onSignOffBriefing = { viewModel.markBriefingReviewed(it) }
                )
                4 -> AuditScreen(
                    auditLogs = auditLogs
                )
            }
        }
    }

    if (showComposeDialog) {
        ComposeMessageDialog(
            onDismiss = { showComposeDialog = false },
            onSend = { content, priority, cat, deadline ->
                viewModel.sendMessage(content, priority, cat, deadline)
                showComposeDialog = false
            }
        )
    }

    if (showNewApprovalDialog) {
        NewApprovalDialog(
            onDismiss = { showNewApprovalDialog = false },
            onSubmit = { title, details, type, deadlineHours, impact ->
                viewModel.submitApproval(title, details, type, deadlineHours, impact)
                showNewApprovalDialog = false
            }
        )
    }

    if (showNewEventDialog) {
        NewEventDialog(
            onDismiss = { showNewEventDialog = false },
            onCreate = { title, start, end, loc, isVirt, link, purpose, atts ->
                viewModel.createCalendarEvent(title, start, end, loc, isVirt, link, purpose, atts)
                showNewEventDialog = false
            }
        )
    }
}

@Composable
fun ExecutiveTopBar(
    currentUser: com.example.executivelink.data.model.UserProfile,
    onSwitchRole: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ExecutiveSurface)
            .border(1.dp, ExecutiveBorder)
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("executive_top_bar")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Monogram & Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1E293B))
                        .border(1.dp, ExecutiveGold.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.executivelink_icon_1791204693553),
                        contentDescription = "ExecutiveLink Logo",
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "ExecutiveLink",
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.25).sp
                    )
                    Text(
                        text = "CEO & Prime Assistant Workspace",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            // Role Toggle Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF1E293B))
                    .border(1.dp, ExecutiveBorder, RoundedCornerShape(20.dp))
                    .clickable { onSwitchRole() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .testTag("switch_role_toggle")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = currentUser.name,
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        RoleBadge(role = currentUser.role)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Switch Perspective",
                        tint = ExecutiveGold,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ExecutiveBottomBar(
    currentTab: Int,
    onSelectTab: (Int) -> Unit,
    urgentBadgeCount: Int,
    pendingApprovalCount: Int
) {
    NavigationBar(
        containerColor = ExecutiveSurface,
        contentColor = TextSecondary,
        tonalElevation = 8.dp,
        modifier = Modifier
            .border(1.dp, ExecutiveBorder)
            .testTag("bottom_nav_bar")
    ) {
        // Tab 0: Inbox
        NavigationBarItem(
            selected = currentTab == 0,
            onClick = { onSelectTab(0) },
            icon = {
                if (urgentBadgeCount > 0) {
                    BadgedBox(
                        badge = {
                            Badge(containerColor = ExecutiveRed) {
                                Text(urgentBadgeCount.toString(), color = Color.White)
                            }
                        }
                    ) {
                        Icon(Icons.Default.Inbox, contentDescription = "Inbox")
                    }
                } else {
                    Icon(Icons.Default.Inbox, contentDescription = "Inbox")
                }
            },
            label = { Text("Inbox", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF0B1120),
                indicatorColor = ExecutiveGold,
                selectedTextColor = ExecutiveGold,
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary
            ),
            modifier = Modifier.testTag("nav_tab_inbox")
        )

        // Tab 1: Approvals
        NavigationBarItem(
            selected = currentTab == 1,
            onClick = { onSelectTab(1) },
            icon = {
                if (pendingApprovalCount > 0) {
                    BadgedBox(
                        badge = {
                            Badge(containerColor = ExecutiveGold) {
                                Text(pendingApprovalCount.toString(), color = Color(0xFF0B1120), fontWeight = FontWeight.Bold)
                            }
                        }
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Approvals")
                    }
                } else {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Approvals")
                }
            },
            label = { Text("Approvals", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF0B1120),
                indicatorColor = ExecutiveGold,
                selectedTextColor = ExecutiveGold,
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary
            ),
            modifier = Modifier.testTag("nav_tab_approvals")
        )

        // Tab 2: Calendar
        NavigationBarItem(
            selected = currentTab == 2,
            onClick = { onSelectTab(2) },
            icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Calendar") },
            label = { Text("Calendar", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF0B1120),
                indicatorColor = ExecutiveGold,
                selectedTextColor = ExecutiveGold,
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary
            ),
            modifier = Modifier.testTag("nav_tab_calendar")
        )

        // Tab 3: Briefing
        NavigationBarItem(
            selected = currentTab == 3,
            onClick = { onSelectTab(3) },
            icon = { Icon(Icons.Default.Assignment, contentDescription = "Briefing") },
            label = { Text("Briefing", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF0B1120),
                indicatorColor = ExecutiveGold,
                selectedTextColor = ExecutiveGold,
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary
            ),
            modifier = Modifier.testTag("nav_tab_briefing")
        )

        // Tab 4: Audit
        NavigationBarItem(
            selected = currentTab == 4,
            onClick = { onSelectTab(4) },
            icon = { Icon(Icons.Default.HistoryEdu, contentDescription = "Audit") },
            label = { Text("Audit", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF0B1120),
                indicatorColor = ExecutiveGold,
                selectedTextColor = ExecutiveGold,
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary
            ),
            modifier = Modifier.testTag("nav_tab_audit")
        )
    }
}
