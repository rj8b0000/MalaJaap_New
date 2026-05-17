package com.rudraksh.naamjaap.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session_history")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val targetCount: Int,
    val completedCount: Int,
    val mantra: String?,
    val durationMillis: Long,
    val timestamp: Long,
    val isCompleted: Boolean
)
