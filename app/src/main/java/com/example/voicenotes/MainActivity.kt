package com.example.voicenotes

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
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

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Название папки"

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_root_folder -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FolderNotesFragment()).commit()
                return true
            }
            R.id.nav_folder1 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FolderNotesFragment()).commit()
                return true
            }
            R.id.nav_folder2 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FolderNotesFragment()).commit()
                return true
            }
            R.id.nav_add_folder -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Введите название папки")

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
                    intent.putExtra("folderTitle", enteredText)
                }

                // Показать диалоговое окно
                builder.
                create().show()

                return true
            }
            R.id.nav_cart -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FolderNotesFragment()).commit()
                return true
            }
        }
        return false
    }

}