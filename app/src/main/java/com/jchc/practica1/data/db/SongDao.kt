package com.jchc.practica1.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jchc.practica1.data.db.model.SongEntity
import com.jchc.practica1.util.Constants

@Dao
interface SongDao {
    //Create
    @Insert
    suspend fun insertSong(song: SongEntity)
    @Insert
    suspend fun insertSong(songs: List<SongEntity>)

    //Read
    @Query("SELECT * FROM ${Constants.DATABASE_SONG_TABLE}")
    suspend fun getAllSongs():List<SongEntity>

    //Update
    @Update
    suspend fun updateSong(song: SongEntity)

    //Delete
    @Delete
    suspend fun deleteSong(song: SongEntity)
}