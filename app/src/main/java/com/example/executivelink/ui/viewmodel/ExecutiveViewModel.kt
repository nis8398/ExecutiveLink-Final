package com.example.executivelink.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.executivelink.data.model.ApprovalEntity
import com.example.executivelink.data.model.ApprovalStatus
import com.example.executivelink.data.model.ApprovalType
import com.example.executivelink.data.model.AuditLogEntity
import com.example.executivelink.data.model.CalendarEventEntity
import com.example.executivelink.data.model.DailyBriefingEntity
import com.example.executivelink.data.model.MessageCategory
import com.example.executivelink.data.model.MessageEntity
import com.example.executivelink.data.model.MessagePriority
import com.example.executivelink.data.model.MessageStatus
import com.example.executivelink.data.model.UserProfile
import com.example.executivelink.data.model.UserRole
import com.example.executivelink.data.repository.ExecutiveRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExecutiveViewModel(private val repository: ExecutiveRepository) : ViewModel() {

    val ceoProfile = UserProfile(
        id = "usr_ceo_01",
        name = "Sarah Jenkins",
        role = UserRole.CEO_MD,
        title = "CEO / Managing Director",
        email = "s.jenkins@executive.corp",
        initials = "SJ"
    )

    val assistantProfile = UserProfile(
        id = "usr_assistant_01",
        name = "Marcus Vance",
        role = UserRole.PRIME_ASSISTANT,
        title = "Prime Executive Assistant",
        email = "m.vance@executive.corp",
        initials = "MV"
    )

    private val _currentUser = MutableStateFlow(ceoProfile)
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    fun toggleRole() {
        if (_currentUser.value.role == UserRole.CEO_MD) {
            _currentUser.value = assistantProfile
        } else {
            _currentUser.value = ceoProfile
        }
    }

    val messages: StateFlow<List<MessageEntity>> = repository.allMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val approvals: StateFlow<List<ApprovalEntity>> = repository.allApprovals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val calendarEvents: StateFlow<List<CalendarEventEntity>> = repository.allCalendarEvents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val auditLogs: StateFlow<List<AuditLogEntity>> = repository.allAuditLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val latestBriefing: StateFlow<DailyBriefingEntity?> = repository.latestBriefing
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val urgentCount: StateFlow<Int> = combine(messages, approvals) { msgs, apps ->
        val unackedUrgent = msgs.count { it.priority == MessagePriority.URGENT && it.status != MessageStatus.ACKNOWLEDGED && it.status != MessageStatus.ACTIONED }
        val pendingApps = apps.count { it.status == ApprovalStatus.PENDING }
        unackedUrgent + pendingApps
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val pendingApprovalCount: StateFlow<Int> = approvals.combine(_currentUser) { apps, _ ->
        apps.count { it.status == ApprovalStatus.PENDING }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun sendMessage(
        content: String,
        priority: MessagePriority,
        category: MessageCategory,
        deadlineMinutes: Int? = null,
        linkedApprovalId: String? = null
    ) {
        val user = _currentUser.value
        val recipient = if (user.role == UserRole.CEO_MD) UserRole.PRIME_ASSISTANT else UserRole.CEO_MD
        viewModelScope.launch {
            repository.sendMessage(
                senderName = user.name,
                senderRole = user.role,
                recipientRole = recipient,
                content = content,
                priority = priority,
                category = category,
                deadlineMinutes = deadlineMinutes,
                linkedApprovalId = linkedApprovalId
            )
        }
    }

    fun acknowledgeMessage(messageId: String) {
        val user = _currentUser.value
        viewModelScope.launch {
            repository.acknowledgeMessage(messageId, user.name, user.role)
        }
    }

    fun submitApproval(
        title: String,
        details: String,
        type: ApprovalType,
        deadlineHours: Int,
        financialImpact: String?
    ) {
        val user = _currentUser.value
        viewModelScope.launch {
            repository.submitApproval(
                title = title,
                details = details,
                type = type,
                requestedBy = "${user.name} (${user.title})",
                deadlineHours = deadlineHours,
                financialImpact = financialImpact
            )
        }
    }

    fun decideApproval(approvalId: String, status: ApprovalStatus, notes: String?) {
        val user = _currentUser.value
        viewModelScope.launch {
            repository.decideApproval(
                approvalId = approvalId,
                status = status,
                decidedBy = "${user.name} (${user.title})",
                actorRole = user.role,
                notes = notes
            )
        }
    }

    fun createCalendarEvent(
        title: String,
        startTime: Long,
        endTime: Long,
        location: String,
        isVirtual: Boolean,
        meetingLink: String?,
        purpose: String,
        attendees: String
    ) {
        val user = _currentUser.value
        viewModelScope.launch {
            repository.createCalendarEvent(
                title = title,
                startTime = startTime,
                endTime = endTime,
                location = location,
                isVirtual = isVirtual,
                meetingLink = meetingLink,
                purpose = purpose,
                attendees = attendees,
                createdBy = user.name,
                actorRole = user.role
            )
        }
    }

    fun rescheduleCalendarEvent(
        eventId: String,
        newStartTime: Long,
        newEndTime: Long,
        reason: String
    ) {
        val user = _currentUser.value
        viewModelScope.launch {
            repository.rescheduleCalendarEvent(
                eventId = eventId,
                newStartTime = newStartTime,
                newEndTime = newEndTime,
                modifiedBy = user.name,
                actorRole = user.role,
                rescheduleReason = reason
            )
        }
    }

    fun confirmCalendarEvent(eventId: String) {
        val user = _currentUser.value
        viewModelScope.launch {
            repository.confirmCalendarEvent(
                eventId = eventId,
                confirmedBy = user.name,
                actorRole = user.role
            )
        }
    }

    fun markBriefingReviewed(briefingId: String) {
        val user = _currentUser.value
        viewModelScope.launch {
            repository.markBriefingReviewed(briefingId, user.name)
        }
    }
}

class ExecutiveViewModelFactory(private val repository: ExecutiveRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExecutiveViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExecutiveViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
