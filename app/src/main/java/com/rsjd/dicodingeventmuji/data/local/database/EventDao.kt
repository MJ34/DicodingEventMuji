package com.rsjd.dicodingeventmuji.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rsjd.dicodingeventmuji.data.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Query("SELECT * FROM events WHERE isActive = 1")
    fun getActiveEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE isActive = 0")
    fun getFinishedEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id = :id")
    fun getEventById(id: String): Flow<EventEntity?>

    @Query("SELECT * FROM events WHERE isActive = 1 LIMIT :limit")
    fun getLimitedActiveEvents(limit: Int): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE isActive = 0 LIMIT :limit")
    fun getLimitedFinishedEvents(limit: Int): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE name LIKE '%' || :query || '%'")
    fun searchEvents(query: String): Flow<List<EventEntity>>

    @Query("DELETE FROM events WHERE isActive = :isActive")
    suspend fun deleteEventsByType(isActive: Boolean)
}