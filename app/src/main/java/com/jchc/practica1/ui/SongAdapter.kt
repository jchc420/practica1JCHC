package com.jchc.practica1.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jchc.practica1.data.db.model.SongEntity
import com.jchc.practica1.databinding.SongElementBinding

class SongAdapter(private val onSongClicked: (SongEntity) -> Unit): RecyclerView.Adapter<SongViewHolder>(){

    private var songs: List<SongEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = SongElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun getItemCount(): Int = songs.size

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {

        val song = songs[position]
        holder.bind(song)

        holder.itemView.setOnClickListener{
            //aquí va el click a cada elemento
            onSongClicked(song)
        }
    }

    //para actualizar los datos del recyclerview
    fun updateList(list: List<SongEntity>){
        songs = list
        notifyDataSetChanged()
    }


}