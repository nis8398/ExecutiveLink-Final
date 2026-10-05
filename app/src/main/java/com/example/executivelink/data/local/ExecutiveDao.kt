package com.example.executivelink.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.executivelink.data.model.ApprovalEntity
import com.example.executivelink.data.model.AuditLogEntity
import com.example.executivelink.data.model.CalendarEventEntity
import com.example.executivelink.data.model.DailyBriefingEntity
import com.example.executivelink.data.model.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExecutiveDao {

    // --- Messages ---
    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    fun getAllMessages(): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Update
    suspend fun updateMessage(message: MessageEntity)

    @Query("SELECT * FROM messages WHERE id = :id LIMIT 1")
    suspend fun getMessageById(id: String): MessageEntity?

    // --- Approvals ---
    @Query("SELECT * FROM approvals ORDER BY requestedAt DESC")
    fun getAllApprovals(): Flow<List<ApprovalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApproval(approval: ApprovalEntity)

    @Update
    suspend fun updateApproval(approval: ApprovalEntity)

    @Query("SELECT * FROM approvals WHERE id = :id LIMIT 1")
    suspend fun getApprovalById(id: String): ApprovalEntity?

    // --- Calendar Events ---
    @Query("SELECT * FROM calendar_events ORDER BY startTime ASC")
    fun getAllCalendarEvents(): Flow<List<CalendarEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalendarEvent(event: CalendarEventEntity)

    @Update
    suspend fun updateCalendarEvent(event: CalendarEventEntity)

    @Query("SELECT * FROM calendar_events WHERE id = :id LIMIT 1")
    suspend fun getCalendarEventById(id: String): CalendarEventEntity?

    // --- Audit Logs ---
    @Query("SELECT * FROM audit_logs ORDER BY timestamp DESC")
    fun getAllAuditLogs(): Flow<List<AuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(log: AuditLogEntity)

    // --- Daily Briefing ---
    @Query("SELECT * FROM daily_briefings ORDER BY compiledAt DESC LIMIT 1")
    fun getLatestBriefing(): Flow<DailyBriefingEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBriefing(briefing: DailyBriefingEntity)

    @Update
    suspend fun updateBriefing(briefing: DailyBriefingEntity)
}
