package com.example.voicenotes

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.Gravity
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView.Orientation
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var mainLayout: LinearLayout // Reference to your main vertical LinearLayout
    private val REQUEST_CODE_VOICE_ACTIVITY = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLayout = findViewById(R.id.fragment_container)

        val buttonClick = findViewById<Button>(R.id.button_click)
        buttonClick.setOnClickListener {
            val intent = Intent(this, VoiceActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_VOICE_ACTIVITY)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_VOICE_ACTIVITY && resultCode == RESULT_OK) {
            val newItemText = data?.getStringExtra("new_item_text")
            val noteItemContent = LinearLayout(this)
            noteItemContent.orientation = LinearLayout.HORIZONTAL
            noteItemContent.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            // Create a button on the left
            val leftButton = Button(this)
            leftButton.text = "▶️";
            leftButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0f
            )
            leftButton.gravity = Gravity.START
            leftButton.setBackgroundColor(Color.WHITE)

            // Create a button on the right
            val rightButton = Button(this)
            rightButton.text = "❌"
            rightButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0f
            )
            rightButton.gravity = Gravity.END
            rightButton.setBackgroundColor(Color.WHITE)

            // Create the title in the middle
            val titleTextView = TextView(this)
            titleTextView.text = newItemText
            titleTextView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            titleTextView.setTextColor(Color.BLACK)
            titleTextView.gravity = Gravity.CENTER

            // Add the views to the horizontal layout
            noteItemContent.addView(leftButton)
            noteItemContent.addView(titleTextView)
            noteItemContent.addView(rightButton)
            // Add the horizontal layout to the vertical layout
            mainLayout.addView(noteItemContent)
        }
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
                input.filters =
                    arrayOf(InputFilter.LengthFilter(40)) // Максимальная длина 40 символов
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
                builder.create().show()

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