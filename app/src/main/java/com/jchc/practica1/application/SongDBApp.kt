package com.jchc.practica1.application

import android.app.Application
import com.jchc.practica1.data.SongRepository
import com.jchc.practica1.data.db.SongDatabase

class SongDBApp:Application() {

    private val database by lazy{
        SongDatabase.getDatabase(this@SongDBApp)
    }

    val repository by lazy{
        SongRepository(database.songDao())
    }
}