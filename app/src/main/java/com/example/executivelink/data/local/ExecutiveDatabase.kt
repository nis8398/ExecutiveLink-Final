package com.example.executivelink.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.executivelink.data.model.ApprovalEntity
import com.example.executivelink.data.model.AuditLogEntity
import com.example.executivelink.data.model.CalendarEventEntity
import com.example.executivelink.data.model.DailyBriefingEntity
import com.example.executivelink.data.model.MessageEntity

@Database(
    entities = [
        MessageEntity::class,
        ApprovalEntity::class,
        CalendarEventEntity::class,
        AuditLogEntity::class,
        DailyBriefingEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ExecutiveDatabase : RoomDatabase() {
    abstract fun executiveDao(): ExecutiveDao

    companion object {
        @Volatile
        private var INSTANCE: ExecutiveDatabase? = null

        fun getDatabase(context: Context): ExecutiveDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExecutiveDatabase::class.java,
                    "executivelink_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
