package com.jchc.practica1.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jchc.practica1.R
import com.jchc.practica1.application.SongDBApp
import com.jchc.practica1.data.SongRepository
import com.jchc.practica1.data.db.model.SongEntity
import com.jchc.practica1.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var songs: List<SongEntity> = emptyList()
    private lateinit var repository: SongRepository

    private lateinit var songAdapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as SongDBApp).repository

        songAdapter = SongAdapter() { selectedSong ->
            //Click para actualizar o borrar algún elemento
            val dialog = SongDialog(
                newSong = false,
                song = selectedSong,
                updateUI = {
                    updateUI()
                }, message = { action ->
                    message(action)
                })
            dialog.show(supportFragmentManager, "updateDialog")
        }
        binding.rvSongs.apply{
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = songAdapter
        }
        updateUI()
    }

    private fun updateUI(){
        lifecycleScope.launch(){
            songs = repository.getAllSongs()
            binding.tvSinRegistros.visibility =
                if(songs.isEmpty()) View.VISIBLE else View.INVISIBLE
            songAdapter.updateList(songs)
        }
    }

    fun click(view: View){
        //Manejo de click de FAB (Floating action button)
        val dialog = SongDialog(
            updateUI ={
                updateUI()
            }, message = { action ->
                message(action)
            })
        dialog.show(supportFragmentManager, "insertDialog")
    }

    private fun message(text: String){
        Snackbar.make(binding.cl, text, Snackbar.LENGTH_SHORT)
            .setTextColor(getColor(R.color.white))
            .setBackgroundTint(getColor(R.color.lila))
            .show()
    }
}