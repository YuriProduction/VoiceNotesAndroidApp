package com.example.voicenotes

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.romancha.playpause.PlayPauseView

class VoiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.voice_activity)
        val playPauseView = findViewById<PlayPauseView>(R.id.play_pause_view)
        playPauseView.setOnClickListener {
            // View clicked
            playPauseView.toggle()
            if (playPauseView.onPlaying())
            {
                //TODO - начать записывать аудиозапись
            }
            else
            {
                val builder = AlertDialog.Builder(this)
                // Установить заголовок (необязательно)
                builder.setTitle("Введите название заметки")
                // Создать поле ввода текста
                val input = EditText(this)
                input.inputType = InputType.TYPE_CLASS_TEXT
                input.filters = arrayOf(InputFilter.LengthFilter(40))
                builder.setView(input)
                // Установить кнопку "Отмена"
                builder.setNegativeButton("Отмена") { dialog, _ ->
                    dialog.cancel()
                }

                // Установить кнопку "Ок"
                builder.setPositiveButton("Ок") { dialog, _ ->
                    val enteredText = input.text.toString()
                    val newItemText = enteredText // Replace with the actual item text you want to add
                    val intent = Intent()
                    intent.putExtra("new_item_text", newItemText)
                    setResult(RESULT_OK, intent)
                    finish()
                    dialog.dismiss()
                }
                // Показать диалоговое окно
                builder.
                create().show()
            }
        }
    }

}