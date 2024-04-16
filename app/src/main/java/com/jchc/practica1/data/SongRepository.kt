package com.jchc.practica1.data

import com.jchc.practica1.data.db.SongDao
import com.jchc.practica1.data.db.model.SongEntity

class SongRepository(private val songDao: SongDao) {
    suspend fun insertSong(song:SongEntity){
        songDao.insertSong(song)
    }

    suspend fun getAllSongs():List<SongEntity>{
        return songDao.getAllSongs()
    }

    suspend fun updateSong(song: SongEntity){
        songDao.updateSong(song)
    }

    suspend fun deleteSong(song: SongEntity){
        songDao.deleteSong(song)
    }

}
