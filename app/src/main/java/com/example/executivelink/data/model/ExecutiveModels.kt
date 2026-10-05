package com.example.executivelink.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole(val displayName: String, val roleTag: String) {
    CEO_MD("CEO / Managing Director", "CEO"),
    PRIME_ASSISTANT("Prime Assistant", "PA")
}

data class UserProfile(
    val id: String,
    val name: String,
    val role: UserRole,
    val title: String,
    val email: String,
    val initials: String
)

enum class MessagePriority(val label: String) {
    URGENT("Urgent SLA"),
    IMPORTANT("Important"),
    ROUTINE("Routine")
}

enum class MessageStatus(val label: String) {
    SENT("Sent"),
    DELIVERED("Delivered"),
    READ("Read"),
    ACKNOWLEDGED("Acknowledged"),
    ACTIONED("Actioned")
}

enum class MessageCategory(val label: String) {
    GENERAL("General Directive"),
    APPROVAL_REQ("Approval Request"),
    CALENDAR_SYNC("Calendar Coordination"),
    URGENT_BRIEF("Urgent Brief")
}

enum class ApprovalStatus(val label: String) {
    PENDING("Pending CEO Review"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    CLARIFICATION("Clarification Needed"),
    DEFERRED("Deferred")
}

enum class ApprovalType(val label: String) {
    EXPENDITURE("Budget / Expenditure"),
    CONTRACT("Contract / Agreement"),
    SCHEDULE_EXCEPTION("Schedule Exception"),
    STRATEGY_HR("Personnel / Strategy")
}

enum class EventStatus(val label: String) {
    CONFIRMED("Confirmed"),
    PENDING_CONFIRMATION("Pending CEO"),
    RESCHEDULED("Rescheduled"),
    CANCELLED("Cancelled")
}

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val senderId: String,
    val senderName: String,
    val senderRole: UserRole,
    val recipientRole: UserRole,
    val content: String,
    val priority: MessagePriority,
    val category: MessageCategory,
    val status: MessageStatus,
    val timestamp: Long,
    val deadlineTimestamp: Long? = null,
    val acknowledgedAt: Long? = null,
    val acknowledgedBy: String? = null,
    val linkedApprovalId: String? = null,
    val linkedEventId: String? = null
)

@Entity(tableName = "approvals")
data class ApprovalEntity(
    @PrimaryKey val id: String,
    val title: String,
    val details: String,
    val type: ApprovalType,
    val requestedBy: String,
    val requestedAt: Long,
    val deadlineTimestamp: Long,
    val financialImpact: String? = null,
    val status: ApprovalStatus,
    val decisionAt: Long? = null,
    val decisionBy: String? = null,
    val decisionNotes: String? = null
)

@Entity(tableName = "calendar_events")
data class CalendarEventEntity(
    @PrimaryKey val id: String,
    val title: String,
    val startTime: Long,
    val endTime: Long,
    val location: String,
    val isVirtual: Boolean,
    val meetingLink: String? = null,
    val purpose: String,
    val attendees: String,
    val status: EventStatus,
    val createdBy: String,
    val lastModifiedAt: Long,
    val lastModifiedNote: String? = null
)

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey val id: String,
    val actionType: String,
    val performedBy: String,
    val actorRole: UserRole,
    val targetType: String,
    val targetId: String,
    val summary: String,
    val timestamp: Long,
    val hashSignature: String
)

@Entity(tableName = "daily_briefings")
data class DailyBriefingEntity(
    @PrimaryKey val id: String,
    val briefingDate: String,
    val compiledAt: Long,
    val summaryHeadline: String,
    val keyPriorities: String,
    val meetingsCount: Int,
    val pendingApprovalsCount: Int,
    val urgentIssuesCount: Int,
    val reviewedByExec: Boolean,
    val reviewedAt: Long? = null
)
