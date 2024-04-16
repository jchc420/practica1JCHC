package com.jchc.practica1.ui

import androidx.recyclerview.widget.RecyclerView
import com.jchc.practica1.R
import com.jchc.practica1.data.db.model.SongEntity
import com.jchc.practica1.databinding.SongElementBinding

class SongViewHolder(private val binding: SongElementBinding): RecyclerView.ViewHolder(binding.root) {

    var ivIcon = binding.ivIcon

    fun bind(song: SongEntity){
        binding.apply{
            tvTitle.text = song.title
            tvGenre.text = song.genre
            tvArtist.text = song.artist

            if (tvGenre.text == "Rock")
                ivIcon.setBackgroundResource(R.drawable.baseline_anchor_24)
            if (tvGenre.text == "Salsa")
                ivIcon.setBackgroundResource(R.drawable.baseline_heart_broken_24)
            if (tvGenre.text == "Pop")
                ivIcon.setBackgroundResource(R.drawable.baseline_headphones_24)
            if (tvGenre.text == "Rap")
                ivIcon.setBackgroundResource(R.drawable.baseline_mic_external_on_24)
        }
    }

}