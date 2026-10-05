package com.example.executivelink.data.local

import androidx.room.TypeConverter
import com.example.executivelink.data.model.ApprovalStatus
import com.example.executivelink.data.model.ApprovalType
import com.example.executivelink.data.model.EventStatus
import com.example.executivelink.data.model.MessageCategory
import com.example.executivelink.data.model.MessagePriority
import com.example.executivelink.data.model.MessageStatus
import com.example.executivelink.data.model.UserRole

class Converters {

    @TypeConverter
    fun fromUserRole(value: UserRole?): String? = value?.name

    @TypeConverter
    fun toUserRole(value: String?): UserRole? = value?.let { enumValueOf<UserRole>(it) }

    @TypeConverter
    fun fromMessagePriority(value: MessagePriority?): String? = value?.name

    @TypeConverter
    fun toMessagePriority(value: String?): MessagePriority? = value?.let { enumValueOf<MessagePriority>(it) }

    @TypeConverter
    fun fromMessageStatus(value: MessageStatus?): String? = value?.name

    @TypeConverter
    fun toMessageStatus(value: String?): MessageStatus? = value?.let { enumValueOf<MessageStatus>(it) }

    @TypeConverter
    fun fromMessageCategory(value: MessageCategory?): String? = value?.name

    @TypeConverter
    fun toMessageCategory(value: String?): MessageCategory? = value?.let { enumValueOf<MessageCategory>(it) }

    @TypeConverter
    fun fromApprovalStatus(value: ApprovalStatus?): String? = value?.name

    @TypeConverter
    fun toApprovalStatus(value: String?): ApprovalStatus? = value?.let { enumValueOf<ApprovalStatus>(it) }

    @TypeConverter
    fun fromApprovalType(value: ApprovalType?): String? = value?.name

    @TypeConverter
    fun toApprovalType(value: String?): ApprovalType? = value?.let { enumValueOf<ApprovalType>(it) }

    @TypeConverter
    fun fromEventStatus(value: EventStatus?): String? = value?.name

    @TypeConverter
    fun toEventStatus(value: String?): EventStatus? = value?.let { enumValueOf<EventStatus>(it) }
}
