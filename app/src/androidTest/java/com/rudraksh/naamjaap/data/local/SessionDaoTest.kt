package com.rudraksh.naamjaap.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rudraksh.naamjaap.data.local.dao.SessionDao
import com.rudraksh.naamjaap.data.local.entity.SessionEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SessionDaoTest {

    private lateinit var database: NaamJaapDatabase
    private lateinit var sessionDao: SessionDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, NaamJaapDatabase::class.java).build()
        sessionDao = database.sessionDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndGetAllSessions() = runBlocking {
        val session = SessionEntity(
            targetCount = 108,
            completedCount = 108,
            mantra = "Om Namah Shivaya",
            durationMillis = 60000L,
            timestamp = 1000L,
            isCompleted = true
        )

        sessionDao.insertSession(session)

        val sessions = sessionDao.getAllSessions().first()
        assertTrue(sessions.isNotEmpty())
        assertEquals(108, sessions[0].targetCount)
        assertEquals("Om Namah Shivaya", sessions[0].mantra)
    }
}
