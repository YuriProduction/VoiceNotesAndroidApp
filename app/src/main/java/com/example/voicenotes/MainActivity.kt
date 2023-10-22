package com.example.voicenotes

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonClick = findViewById<Button>(R.id.button_click)
        buttonClick.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            // Установить заголовок (необязательно)
            builder.setTitle("Введите название заметки")
            // Создать поле ввода текста
            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT
            input.filters = arrayOf(InputFilter.LengthFilter(40)) // Максимальная длина 40 символов
            builder.setView(input)
            // Установить кнопку "Отмена"
            builder.setNegativeButton("Отмена") { dialog, _ ->
                dialog.cancel()
            }

            // Установить кнопку "Ок"
            builder.setPositiveButton("Ок") { dialog, _ ->
                val enteredText = input.text.toString()
                dialog.dismiss()
                val intent = Intent(this, VoiceActivity::class.java)
                // Передать данные, если необходимо
                intent.putExtra("noteTitle", enteredText)
                // Запустить новую Activity
                startActivity(intent)
                // Здесь вы можете использовать введенный текст (enteredText) по вашему усмотрению
            }
            // Показать диалоговое окно
            builder.
            create().show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item -> {
                // Обработка нажатия на иконку бургера (меню)
                // Здесь вы можете открыть ваше меню или выполнить другие действия
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

}