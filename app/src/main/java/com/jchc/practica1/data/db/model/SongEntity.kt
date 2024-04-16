package com.jchc.practica1.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jchc.practica1.util.Constants

@Entity(tableName = Constants.DATABASE_SONG_TABLE)
data class SongEntity(
    @PrimaryKey(autoGenerate = true) //Para que lo haga llave primaria y se autogenere
    @ColumnInfo(name = "song_id")
    val id: Long = 0,
    @ColumnInfo(name="song_title")
    var title: String,
    @ColumnInfo(name="song_genre", defaultValue = "Desconocido")
    var genre: String,
    @ColumnInfo(name="song_artist")
    var artist: String
)