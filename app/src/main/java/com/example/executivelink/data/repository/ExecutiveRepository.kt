package com.example.executivelink.data.repository

import com.example.executivelink.data.local.ExecutiveDao
import com.example.executivelink.data.model.ApprovalEntity
import com.example.executivelink.data.model.ApprovalStatus
import com.example.executivelink.data.model.ApprovalType
import com.example.executivelink.data.model.AuditLogEntity
import com.example.executivelink.data.model.CalendarEventEntity
import com.example.executivelink.data.model.DailyBriefingEntity
import com.example.executivelink.data.model.EventStatus
import com.example.executivelink.data.model.MessageCategory
import com.example.executivelink.data.model.MessageEntity
import com.example.executivelink.data.model.MessagePriority
import com.example.executivelink.data.model.MessageStatus
import com.example.executivelink.data.model.UserRole
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest
import java.util.UUID

class ExecutiveRepository(private val dao: ExecutiveDao) {

    val allMessages: Flow<List<MessageEntity>> = dao.getAllMessages()
    val allApprovals: Flow<List<ApprovalEntity>> = dao.getAllApprovals()
    val allCalendarEvents: Flow<List<CalendarEventEntity>> = dao.getAllCalendarEvents()
    val allAuditLogs: Flow<List<AuditLogEntity>> = dao.getAllAuditLogs()
    val latestBriefing: Flow<DailyBriefingEntity?> = dao.getLatestBriefing()

    private fun generateAuditHash(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(input.toByteArray())
        return "SHA256:" + hash.take(16).joinToString("") { "%02x".format(it) }
    }

    suspend fun sendMessage(
        senderName: String,
        senderRole: UserRole,
        recipientRole: UserRole,
        content: String,
        priority: MessagePriority,
        category: MessageCategory,
        deadlineMinutes: Int? = null,
        linkedApprovalId: String? = null
    ): String {
        val now = System.currentTimeMillis()
        val id = UUID.randomUUID().toString()
        val deadline = deadlineMinutes?.let { now + it * 60 * 1000L }

        val message = MessageEntity(
            id = id,
            senderId = if (senderRole == UserRole.CEO_MD) "usr_ceo" else "usr_pa",
            senderName = senderName,
            senderRole = senderRole,
            recipientRole = recipientRole,
            content = content,
            priority = priority,
            category = category,
            status = MessageStatus.SENT,
            timestamp = now,
            deadlineTimestamp = deadline,
            linkedApprovalId = linkedApprovalId
        )
        dao.insertMessage(message)

        // Record Audit Log
        val audit = AuditLogEntity(
            id = UUID.randomUUID().toString(),
            actionType = "MESSAGE_DISPATCHED",
            performedBy = senderName,
            actorRole = senderRole,
            targetType = "MESSAGE",
            targetId = id,
            summary = "Sent ${priority.label} message: \"${content.take(40)}...\"",
            timestamp = now,
            hashSignature = generateAuditHash("$id-$now-$senderName")
        )
        dao.insertAuditLog(audit)
        return id
    }

    suspend fun acknowledgeMessage(messageId: String, acknowledgedByName: String, actorRole: UserRole) {
        val msg = dao.getMessageById(messageId) ?: return
        val now = System.currentTimeMillis()
        val updated = msg.copy(
            status = MessageStatus.ACKNOWLEDGED,
            acknowledgedAt = now,
            acknowledgedBy = acknowledgedByName
        )
        dao.updateMessage(updated)

        // Record Audit Log
        val audit = AuditLogEntity(
            id = UUID.randomUUID().toString(),
            actionType = "MESSAGE_ACKNOWLEDGED",
            performedBy = acknowledgedByName,
            actorRole = actorRole,
            targetType = "MESSAGE",
            targetId = messageId,
            summary = "Acknowledged message: \"${msg.content.take(35)}...\"",
            timestamp = now,
            hashSignature = generateAuditHash("$messageId-$now-$acknowledgedByName")
        )
        dao.insertAuditLog(audit)
    }

    suspend fun submitApproval(
        title: String,
        details: String,
        type: ApprovalType,
        requestedBy: String,
        deadlineHours: Int,
        financialImpact: String?
    ): String {
        val now = System.currentTimeMillis()
        val id = UUID.randomUUID().toString()
        val deadline = now + deadlineHours * 3600 * 1000L

        val approval = ApprovalEntity(
            id = id,
            title = title,
            details = details,
            type = type,
            requestedBy = requestedBy,
            requestedAt = now,
            deadlineTimestamp = deadline,
            financialImpact = financialImpact,
            status = ApprovalStatus.PENDING
        )
        dao.insertApproval(approval)

        // Also create an urgent/important notification message linked to this approval
        sendMessage(
            senderName = requestedBy,
            senderRole = UserRole.PRIME_ASSISTANT,
            recipientRole = UserRole.CEO_MD,
            content = "New approval required: \"$title\" ($type${financialImpact?.let { " - $it" } ?: ""})",
            priority = MessagePriority.URGENT,
            category = MessageCategory.APPROVAL_REQ,
            deadlineMinutes = deadlineHours * 60,
            linkedApprovalId = id
        )

        val audit = AuditLogEntity(
            id = UUID.randomUUID().toString(),
            actionType = "APPROVAL_REQUESTED",
            performedBy = requestedBy,
            actorRole = UserRole.PRIME_ASSISTANT,
            targetType = "APPROVAL_REQUEST",
            targetId = id,
            summary = "Submitted approval request for '$title' ($type)",
            timestamp = now,
            hashSignature = generateAuditHash("$id-$now-$requestedBy")
        )
        dao.insertAuditLog(audit)
        return id
    }

    suspend fun decideApproval(
        approvalId: String,
        status: ApprovalStatus,
        decidedBy: String,
        actorRole: UserRole,
        notes: String?
    ) {
        val approval = dao.getApprovalById(approvalId) ?: return
        val now = System.currentTimeMillis()
        val updated = approval.copy(
            status = status,
            decisionAt = now,
            decisionBy = decidedBy,
            decisionNotes = notes
        )
        dao.updateApproval(updated)

        // Notify via a message
        sendMessage(
            senderName = decidedBy,
            senderRole = actorRole,
            recipientRole = UserRole.PRIME_ASSISTANT,
            content = "Decision on '$approval.title': ${status.label}. ${notes?.let { "Note: $it" } ?: ""}",
            priority = MessagePriority.IMPORTANT,
            category = MessageCategory.APPROVAL_REQ,
            linkedApprovalId = approvalId
        )

        val audit = AuditLogEntity(
            id = UUID.randomUUID().toString(),
            actionType = "APPROVAL_DECISION",
            performedBy = decidedBy,
            actorRole = actorRole,
            targetType = "APPROVAL_REQUEST",
            targetId = approvalId,
            summary = "Decided: ${status.label} on '${approval.title}' (${notes ?: "No extra comment"})",
            timestamp = now,
            hashSignature = generateAuditHash("$approvalId-$now-$status")
        )
        dao.insertAuditLog(audit)
    }

    suspend fun createCalendarEvent(
        title: String,
        startTime: Long,
        endTime: Long,
        location: String,
        isVirtual: Boolean,
        meetingLink: String?,
        purpose: String,
        attendees: String,
        createdBy: String,
        actorRole: UserRole
    ): String {
        val now = System.currentTimeMillis()
        val id = UUID.randomUUID().toString()

        val event = CalendarEventEntity(
            id = id,
            title = title,
            startTime = startTime,
            endTime = endTime,
            location = location,
            isVirtual = isVirtual,
            meetingLink = meetingLink,
            purpose = purpose,
            attendees = attendees,
            status = EventStatus.CONFIRMED,
            createdBy = createdBy,
            lastModifiedAt = now,
            lastModifiedNote = "Initial appointment scheduled"
        )
        dao.insertCalendarEvent(event)

        val audit = AuditLogEntity(
            id = UUID.randomUUID().toString(),
            actionType = "CALENDAR_EVENT_CREATED",
            performedBy = createdBy,
            actorRole = actorRole,
            targetType = "CALENDAR_EVENT",
            targetId = id,
            summary = "Scheduled event: '$title' at $location",
            timestamp = now,
            hashSignature = generateAuditHash("$id-$now-$createdBy")
        )
        dao.insertAuditLog(audit)
        return id
    }

    suspend fun rescheduleCalendarEvent(
        eventId: String,
        newStartTime: Long,
        newEndTime: Long,
        modifiedBy: String,
        actorRole: UserRole,
        rescheduleReason: String
    ) {
        val event = dao.getCalendarEventById(eventId) ?: return
        val now = System.currentTimeMillis()
        val updated = event.copy(
            startTime = newStartTime,
            endTime = newEndTime,
            status = EventStatus.RESCHEDULED,
            lastModifiedAt = now,
            lastModifiedNote = "Rescheduled: $rescheduleReason"
        )
        dao.updateCalendarEvent(updated)

        // Notify other party
        val recipient = if (actorRole == UserRole.CEO_MD) UserRole.PRIME_ASSISTANT else UserRole.CEO_MD
        sendMessage(
            senderName = modifiedBy,
            senderRole = actorRole,
            recipientRole = recipient,
            content = "Calendar Event Rescheduled: '${event.title}' moved. Reason: $rescheduleReason",
            priority = MessagePriority.URGENT,
            category = MessageCategory.CALENDAR_SYNC
        )

        val audit = AuditLogEntity(
            id = UUID.randomUUID().toString(),
            actionType = "CALENDAR_EVENT_RESCHEDULED",
            performedBy = modifiedBy,
            actorRole = actorRole,
            targetType = "CALENDAR_EVENT",
            targetId = eventId,
            summary = "Rescheduled '${event.title}'. Reason: $rescheduleReason",
            timestamp = now,
            hashSignature = generateAuditHash("$eventId-$now-$modifiedBy")
        )
        dao.insertAuditLog(audit)
    }

    suspend fun confirmCalendarEvent(eventId: String, confirmedBy: String, actorRole: UserRole) {
        val event = dao.getCalendarEventById(eventId) ?: return
        val now = System.currentTimeMillis()
        val updated = event.copy(
            status = EventStatus.CONFIRMED,
            lastModifiedAt = now,
            lastModifiedNote = "Confirmed by $confirmedBy"
        )
        dao.updateCalendarEvent(updated)

        val audit = AuditLogEntity(
            id = UUID.randomUUID().toString(),
            actionType = "CALENDAR_EVENT_CONFIRMED",
            performedBy = confirmedBy,
            actorRole = actorRole,
            targetType = "CALENDAR_EVENT",
            targetId = eventId,
            summary = "Confirmed event: '${event.title}'",
            timestamp = now,
            hashSignature = generateAuditHash("$eventId-$now-$confirmedBy")
        )
        dao.insertAuditLog(audit)
    }

    suspend fun markBriefingReviewed(briefingId: String, reviewedBy: String) {
        val now = System.currentTimeMillis()
        val current = dao.getLatestBriefing()
        val briefing = DailyBriefingEntity(
            id = briefingId,
            briefingDate = "Monday, October 5",
            compiledAt = now - 60 * 60 * 1000L,
            summaryHeadline = "High-priority capital close and media briefing day. 4 scheduled engagements.",
            keyPriorities = "1. Confirm Series B indemnity revisions prior to 11:30 AM call.\n2. Review Carmel Valley Offsite deposit.\n3. Prep key message points for 3 PM Bloomberg interview.",
            meetingsCount = 4,
            pendingApprovalsCount = 2,
            urgentIssuesCount = 1,
            reviewedByExec = true,
            reviewedAt = now
        )
        dao.insertBriefing(briefing)

        val audit = AuditLogEntity(
            id = UUID.randomUUID().toString(),
            actionType = "DAILY_BRIEFING_REVIEWED",
            performedBy = reviewedBy,
            actorRole = UserRole.CEO_MD,
            targetType = "BRIEFING",
            targetId = briefingId,
            summary = "Executive completed Daily Briefing review & sign-off",
            timestamp = now,
            hashSignature = generateAuditHash("$briefingId-$now-$reviewedBy")
        )
        dao.insertAuditLog(audit)
    }
}
