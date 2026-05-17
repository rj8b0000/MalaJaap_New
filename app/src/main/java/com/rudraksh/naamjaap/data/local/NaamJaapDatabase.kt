package com.rudraksh.naamjaap.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rudraksh.naamjaap.data.local.dao.SessionDao
import com.rudraksh.naamjaap.data.local.entity.SessionEntity

@Database(entities = [SessionEntity::class], version = 1, exportSchema = false)
abstract class NaamJaapDatabase : RoomDatabase() {
    abstract val sessionDao: SessionDao
    
    companion object {
        const val DATABASE_NAME = "naamjaap_db"
    }
}
