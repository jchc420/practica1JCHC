package com.jchc.practica1.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jchc.practica1.data.db.model.SongEntity
import com.jchc.practica1.util.Constants


@Database(
    entities = [SongEntity::class],
    version = 1,
    exportSchema = true //por defecto es true
)
abstract class SongDatabase: RoomDatabase() {
    //Aquí va el DAO
    abstract fun songDao():SongDao

    companion object{
        @Volatile
        private var INSTANCE: SongDatabase? = null
        fun getDatabase(context: Context): SongDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SongDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}