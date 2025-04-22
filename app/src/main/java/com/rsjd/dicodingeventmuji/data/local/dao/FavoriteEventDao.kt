package com.rsjd.dicodingeventmuji.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rsjd.dicodingeventmuji.data.local.entity.FavoriteEvent

@Dao
interface FavoriteEventDao {
    @Query("SELECT * FROM favorite_events")
    fun getAllFavorites(): LiveData<List<FavoriteEvent>>

    @Query("SELECT * FROM favorite_events WHERE id = :id")
    fun getFavoriteById(id: Int): LiveData<FavoriteEvent?>

    @Query("SELECT * FROM favorite_events WHERE id = :id")
    suspend fun getFavoriteByIdDirect(id: Int): FavoriteEvent?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteEvent: FavoriteEvent)

    @Delete
    suspend fun deleteFavorite(favoriteEvent: FavoriteEvent): Int

    @Query("SELECT EXISTS(SELECT * FROM favorite_events WHERE id = :id)")
    fun isFavorite(id: Int): LiveData<Boolean>
}