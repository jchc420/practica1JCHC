package com.jchc.practica1.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.jchc.practica1.R
import com.jchc.practica1.application.SongDBApp
import com.jchc.practica1.data.SongRepository
import com.jchc.practica1.data.db.model.SongEntity
import com.jchc.practica1.databinding.SongDialogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class SongDialog(
    private val newSong: Boolean = true,
    private val song:SongEntity = SongEntity(
        title = "",
        genre = "",
        artist = ""
    ),
    private val updateUI:() -> Unit,
    private val message:(String) -> Unit
): DialogFragment() {

    private var _binding: SongDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private var saveButton: Button? =  null
    private lateinit var repository: SongRepository


    /*private fun getGenre (genero: String){
        val items = resources.getStringArray(R.array.genre_list)
        val arrayAdapter = ArrayAdapter(this.requireContext(), R.layout.dropdown_item, items)
        val autocompleteTV = binding.acGenre
        autocompleteTV.setAdapter(arrayAdapter)

        autocompleteTV.onItemClickListener = AdapterView.OnItemClickListener{ adapterView, view, i, l ->
            val itemSelected = adapterView.getItemAtPosition(i)
        }
    }*/

    //Aquí se crea y configura el diálogo de forma inicial
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = SongDialogBinding.inflate(requireActivity().layoutInflater)
        builder = AlertDialog.Builder(requireContext())
        repository= (requireContext().applicationContext as SongDBApp).repository

        val items = resources.getStringArray(R.array.genre_list)
        val adapter = ArrayAdapter(this.requireContext(), R.layout.dropdown_item, items)

        adapter.setDropDownViewResource(R.layout.dropdown_item)

        val autocompleteTV = binding.acGenre
        autocompleteTV.setAdapter(adapter)
        var selectedGenre: String? = null

        autocompleteTV.setOnItemClickListener { parent, view, position, id ->
            saveButton?.isEnabled = validateFields()
            selectedGenre = items[position]
        }



        binding.apply{
            binding.tietTitle.setText(song.title)
            binding.tietArtist.setText(song.artist)
            //binding.acGenre.setText(song.genre)
        }

        dialog = if(newSong)
            buildDialog(getString(R.string.save), getString(R.string.cancel), {
                //Acción de guardar
                //click del botón positivo
                song.apply{
                    title = binding.tietTitle.text.toString()
                    artist = binding.tietArtist.text.toString()
                    genre = selectedGenre?: ""

                }
                try{
                    lifecycleScope.launch {
                        val result = async{
                            repository.insertSong(song)
                        }
                        result.await()
                        withContext(Dispatchers.Main){
                            message(getString(R.string.savedSong))
                            updateUI()
                        }
                    }
                }catch(e: IOException){
                    e.printStackTrace()
                    message(getString(R.string.errorsavedSong))
                }
            }, {
                //acción de cancelar
            })
        else
            buildDialog(getString(R.string.update), getString(R.string.remove), {
                //acción de actualizar
                song.apply {
                    title = binding.tietTitle.text.toString()
                    artist = binding.tietArtist.text.toString()
                    genre = selectedGenre?: ""
                }
                try {
                    lifecycleScope.launch{
                        val result = async{
                            repository.updateSong(song)
                        }
                        result.await()
                        withContext(Dispatchers.Main){
                            message(getString(R.string.updatedSong))
                            updateUI()
                        }
                    }
                }catch(e: IOException){
                    e.printStackTrace()
                    message(getString(R.string.errorupdatedSong))
                }
            }, {
                //acción de borrar
                val context = requireContext()
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.confirm))
                    .setMessage(getString(R.string.askConfirm, song.title))
                    .setPositiveButton(R.string.accept) { _, _ ->
                        try {
                            lifecycleScope.launch{
                                val result = async {
                                    repository.deleteSong(song)
                                }
                                result.await()
                                withContext(Dispatchers.Main) {
                                    message(context.getString(R.string.removedSong))
                                    updateUI()
                                }
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                            message(getString(R.string.errorremovedSong))
                        }
                    }
                        .setNegativeButton(getString(R.string.cancel)){ dialog, _->
                            dialog.dismiss()
                        }
                    .create()
                    .show()
            })
        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //se llama después de que se muestra el diálogo en pantalla
    override fun onStart() {
        super.onStart()
        val alertDialog = dialog as AlertDialog
        saveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false

        binding.tietTitle.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }
        })

        binding.tietArtist.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }
        })
    }

    private fun validateFields(): Boolean =
        (binding.tietTitle.text.toString().isNotEmpty() &&
                binding.tietArtist.text.toString().isNotEmpty() &&
                binding.acGenre.text.toString().isNotEmpty())


    private fun buildDialog(
        btn1Text: String,
        btn2Text:String,
        positiveButton:()->Unit,
        negativeButton:()->Unit
    ): Dialog =
        builder.setView(binding.root)
            .setTitle(getString(R.string.song))
            .setPositiveButton(btn1Text){_, _ ->
                //Acción para botón positivo
                positiveButton()
            }
            .setNegativeButton(btn2Text){_,_ ->
                //Acción para botón negativo
                negativeButton()
            }
            .create()


}