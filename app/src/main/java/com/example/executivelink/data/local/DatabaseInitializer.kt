package com.example.executivelink.data.local

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
import java.util.UUID

object DatabaseInitializer {

    suspend fun populateInitialDataIfEmpty(dao: ExecutiveDao) {
        val now = System.currentTimeMillis()
        val oneHour = 3600 * 1000L
        val oneDay = 24 * 3600 * 1000L

        // Prepopulate Messages
        val msg1Id = UUID.randomUUID().toString()
        val msg2Id = UUID.randomUUID().toString()
        val msg3Id = UUID.randomUUID().toString()

        val sampleMessages = listOf(
            MessageEntity(
                id = msg1Id,
                senderId = "usr_assistant_01",
                senderName = "Marcus Vance",
                senderRole = UserRole.PRIME_ASSISTANT,
                recipientRole = UserRole.CEO_MD,
                content = "URGENT: General Counsel has revised the Series B Term Sheet (Section 4 indemnity). Please acknowledge review before the 11:30 AM partner call.",
                priority = MessagePriority.URGENT,
                category = MessageCategory.APPROVAL_REQ,
                status = MessageStatus.DELIVERED,
                timestamp = now - 20 * 60 * 1000L,
                deadlineTimestamp = now + 40 * 60 * 1000L,
                linkedApprovalId = "appr_series_b_01"
            ),
            MessageEntity(
                id = msg2Id,
                senderId = "usr_assistant_01",
                senderName = "Marcus Vance",
                senderRole = UserRole.PRIME_ASSISTANT,
                recipientRole = UserRole.CEO_MD,
                content = "Carmel Valley Ranch venue contract ready in Approvals tab. Deposit required today to lock exclusive executive pavilion.",
                priority = MessagePriority.IMPORTANT,
                category = MessageCategory.APPROVAL_REQ,
                status = MessageStatus.READ,
                timestamp = now - 90 * 60 * 1000L,
                deadlineTimestamp = now + 5 * oneHour,
                linkedApprovalId = "appr_retreat_02"
            ),
            MessageEntity(
                id = msg3Id,
                senderId = "usr_ceo_01",
                senderName = "Sarah Jenkins",
                senderRole = UserRole.CEO_MD,
                recipientRole = UserRole.PRIME_ASSISTANT,
                content = "Thank you Marcus. Ensure the Bloomberg briefing pack includes our latest sustainability and ARR metrics.",
                priority = MessagePriority.ROUTINE,
                category = MessageCategory.GENERAL,
                status = MessageStatus.ACKNOWLEDGED,
                timestamp = now - 180 * 60 * 1000L,
                acknowledgedAt = now - 170 * 60 * 1000L,
                acknowledgedBy = "Marcus Vance"
            )
        )

        for (m in sampleMessages) {
            dao.insertMessage(m)
        }

        // Prepopulate Approvals
        val sampleApprovals = listOf(
            ApprovalEntity(
                id = "appr_series_b_01",
                title = "Series B Expansion Term Sheet Execution",
                details = "Final closing authorization for $4.2M tranche with lead syndicate. Requires CEO formal electronic signature.",
                type = ApprovalType.CONTRACT,
                requestedBy = "Marcus Vance (Prime Assistant)",
                requestedAt = now - 35 * 60 * 1000L,
                deadlineTimestamp = now + 65 * 60 * 1000L,
                financialImpact = "$4,200,000",
                status = ApprovalStatus.PENDING
            ),
            ApprovalEntity(
                id = "appr_retreat_02",
                title = "Annual Executive Strategic Offsite Deposit",
                details = "Pavilion booking, executive catering and audio-visual staging for 18 leadership members at Carmel Valley.",
                type = ApprovalType.EXPENDITURE,
                requestedBy = "Marcus Vance (Prime Assistant)",
                requestedAt = now - 100 * 60 * 1000L,
                deadlineTimestamp = now + 8 * oneHour,
                financialImpact = "$18,500.00",
                status = ApprovalStatus.PENDING
            ),
            ApprovalEntity(
                id = "appr_board_03",
                title = "Board of Directors Q4 Strategy Reschedule",
                details = "Move start time by 45 minutes (to 3:30 PM EST) to accommodate European director flight buffer.",
                type = ApprovalType.SCHEDULE_EXCEPTION,
                requestedBy = "Marcus Vance (Prime Assistant)",
                requestedAt = now - 240 * 60 * 1000L,
                deadlineTimestamp = now + 24 * oneHour,
                financialImpact = "N/A - Schedule Shift",
                status = ApprovalStatus.APPROVED,
                decisionAt = now - 120 * 60 * 1000L,
                decisionBy = "Sarah Jenkins (CEO)",
                decisionNotes = "Approved as proposed. Ensure dial-in bridge is updated."
            )
        )

        for (a in sampleApprovals) {
            dao.insertApproval(a)
        }

        // Prepopulate Calendar Events (Today)
        val sampleEvents = listOf(
            CalendarEventEntity(
                id = "evt_standup_01",
                title = "Executive Leadership Team Standup",
                startTime = now + 30 * 60 * 1000L,
                endTime = now + 90 * 60 * 1000L,
                location = "Penthouse Boardroom A",
                isVirtual = false,
                meetingLink = "https://meet.executive.corp/elt-standup",
                purpose = "Weekly operational triage and cross-functional dependency tracking.",
                attendees = "Sarah Jenkins (CEO), VP Eng, VP Sales, CFO, GC",
                status = EventStatus.CONFIRMED,
                createdBy = "Marcus Vance",
                lastModifiedAt = now - 2 * oneHour,
                lastModifiedNote = "Room AV setup verified"
            ),
            CalendarEventEntity(
                id = "evt_series_b_02",
                title = "Series B Investor Partner Alignment",
                startTime = now + 2 * oneHour + 30 * 60 * 1000L,
                endTime = now + 3 * oneHour + 30 * 60 * 1000L,
                location = "Confidential Telepresence Suite",
                isVirtual = true,
                meetingLink = "https://meet.executive.corp/series-b-partner",
                purpose = "Syndicate terms alignment and board observer seat confirmation.",
                attendees = "Sarah Jenkins (CEO), General Partner (Apex Capital), CFO",
                status = EventStatus.CONFIRMED,
                createdBy = "Marcus Vance",
                lastModifiedAt = now - 45 * 60 * 1000L,
                lastModifiedNote = "Briefing pack delivered"
            ),
            CalendarEventEntity(
                id = "evt_bloomberg_03",
                title = "Bloomberg Media Exclusive Interview",
                startTime = now + 5 * oneHour,
                endTime = now + 6 * oneHour,
                location = "Executive Studio Suite 4",
                isVirtual = false,
                meetingLink = null,
                purpose = "On-the-record interview on enterprise growth and AI governance.",
                attendees = "Sarah Jenkins (CEO), Chief Communications Officer, Anchor",
                status = EventStatus.CONFIRMED,
                createdBy = "Marcus Vance",
                lastModifiedAt = now - 3 * oneHour,
                lastModifiedNote = "Talking points approved"
            ),
            CalendarEventEntity(
                id = "evt_1on1_04",
                title = "1:1 Strategic Review: VP of Product",
                startTime = now + 7 * oneHour,
                endTime = now + 8 * oneHour,
                location = "Observation Deck Terrace",
                isVirtual = false,
                meetingLink = null,
                purpose = "H1 Roadmap milestones and strategic tier-1 customer deliverables.",
                attendees = "Sarah Jenkins (CEO), David Cole (VP Product)",
                status = EventStatus.PENDING_CONFIRMATION,
                createdBy = "Marcus Vance",
                lastModifiedAt = now - 30 * 60 * 1000L,
                lastModifiedNote = "Requested time confirmation from CEO"
            )
        )

        for (e in sampleEvents) {
            dao.insertCalendarEvent(e)
        }

        // Prepopulate Daily Briefing
        val briefing = DailyBriefingEntity(
            id = "briefing_today",
            briefingDate = "Monday, October 5",
            compiledAt = now - 60 * 60 * 1000L,
            summaryHeadline = "High-priority capital close and media briefing day. 4 scheduled engagements.",
            keyPriorities = "1. Confirm Series B indemnity revisions prior to 11:30 AM call.\n2. Review Carmel Valley Offsite deposit.\n3. Prep key message points for 3 PM Bloomberg interview.",
            meetingsCount = 4,
            pendingApprovalsCount = 2,
            urgentIssuesCount = 1,
            reviewedByExec = false
        )
        dao.insertBriefing(briefing)

        // Prepopulate Audit Logs
        val sampleAudit = listOf(
            AuditLogEntity(
                id = "aud_01",
                actionType = "APPROVAL_DECISION",
                performedBy = "Sarah Jenkins (CEO)",
                actorRole = UserRole.CEO_MD,
                targetType = "APPROVAL_REQUEST",
                targetId = "appr_board_03",
                summary = "Approved Board Meeting reschedule to 3:30 PM EST",
                timestamp = now - 120 * 60 * 1000L,
                hashSignature = "SHA256:7f83b1657ff1fc53b92dc18148a1d65dfc2d4b1fa3d677284addd200126d9069"
            ),
            AuditLogEntity(
                id = "aud_02",
                actionType = "MESSAGE_ACKNOWLEDGED",
                performedBy = "Marcus Vance (Prime Assistant)",
                actorRole = UserRole.PRIME_ASSISTANT,
                targetType = "MESSAGE",
                targetId = msg3Id,
                summary = "Acknowledged Bloomberg briefing pack instructions",
                timestamp = now - 170 * 60 * 1000L,
                hashSignature = "SHA256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
            ),
            AuditLogEntity(
                id = "aud_03",
                actionType = "DAILY_BRIEFING_COMPILED",
                performedBy = "System Notification Worker",
                actorRole = UserRole.PRIME_ASSISTANT,
                targetType = "BRIEFING",
                targetId = "briefing_today",
                summary = "Compiled Executive Daily Briefing for Oct 5 with 4 meetings and 2 approvals",
                timestamp = now - 60 * 60 * 1000L,
                hashSignature = "SHA256:ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb"
            )
        )

        for (log in sampleAudit) {
            dao.insertAuditLog(log)
        }
    }
}
