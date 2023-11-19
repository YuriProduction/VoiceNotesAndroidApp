package com.example.voicenotes

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import java.io.File


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mainLayout: LinearLayout
    private lateinit var navView: NavigationView
    private var foldersManager: FoldersManager = FoldersManager()
    private val REQUEST_CODE_VOICE_ACTIVITY = 1
    private var currentFolderName = "DefaultFolder"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLayout = findViewById(R.id.fragment_container)

        navView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)
        navView.menu.clear()

        foldersManager.uploadFolders(this, navView)

        val buttonClick = findViewById<Button>(R.id.button_click)
        buttonClick.setOnClickListener {
            val intent = Intent(this, VoiceActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_VOICE_ACTIVITY)
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = currentFolderName

        drawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun addNoteInMainLayout(noteName: String?) {
        val noteItemContent = LinearLayout(this)
        noteItemContent.orientation = LinearLayout.HORIZONTAL
        noteItemContent.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // Create a button on the left
        val leftButton = Button(this)
        leftButton.text = "▶️"
        leftButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f
        )
        leftButton.gravity = Gravity.START
        leftButton.setBackgroundColor(Color.WHITE)

        // Create a button on the right
        val rightButton = Button(this)
        rightButton.text = "❌"
        rightButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f
        )
        rightButton.gravity = Gravity.END
        rightButton.setBackgroundColor(Color.WHITE)

        // Create the title in the middle
        val titleTextView = TextView(this)
        titleTextView.text = noteName
        titleTextView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        )
        titleTextView.setTextColor(Color.BLACK)
        titleTextView.gravity = Gravity.CENTER
        rightButton.setOnClickListener {
            val folderPath = getExternalFilesDir("Study") //конкретная папка
            val fileName = titleTextView.text.toString()
            val file = File(folderPath, "$fileName.3gp")
            if (file.exists()) {
                file.delete()
                println("Файл $fileName.3gp успешно удален из папки $folderPath")
            } else {
                println("Файл $fileName.3gp не найден в папке $folderPath")
            }
            mainLayout.removeView(noteItemContent)
        }
        leftButton.setOnClickListener {
            if (leftButton.text.toString() == "▶️") {
                if (isStopped) {
                    resumeAudio()
                } else {
                    playAudio(
                        titleTextView.text.toString(),
                        getExternalFilesDir("Study"),
                        leftButton
                    )
                }
                leftButton.text = "⏸️"
            } else {
                isStopped = true
                stopAudio()
                leftButton.text = "▶️"
            }
        }

        // Add the views to the horizontal layout
        noteItemContent.addView(leftButton)
        noteItemContent.addView(titleTextView)
        noteItemContent.addView(rightButton)
        // Add the horizontal layout to the vertical layout
        mainLayout.addView(noteItemContent)
    }

    private var isStopped = false

    private lateinit var mediaPlayer: MediaPlayer

    // Функция для остановки воспроизведения аудио
    private fun stopAudio() {
        mediaPlayer?.apply {
            if (isPlaying) {
                pause() // Приостанавливаем воспроизведение
            }
        }
    }

    // Функция для продолжения прослушивания аудио
    private fun resumeAudio() {
        mediaPlayer?.apply {
            if (!isPlaying) {
                start() // Возобновляем воспроизведение
            }
        }
    }

    private fun playAudio(audioFileName: String, audioDir: File?, button: Button) {
        val audioFilePath = File(audioDir, "$audioFileName.3gp").absolutePath

        mediaPlayer = MediaPlayer()

        try {
            mediaPlayer.setDataSource(audioFilePath) // Устанавливаем источник аудиоданных из файла
            mediaPlayer.prepare() // Подготавливаем медиаплеер
            mediaPlayer.start() // Запускаем воспроизведение
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Обрабатываем окончание воспроизведения
        mediaPlayer.setOnCompletionListener {
            // Освобождаем ресурсы медиаплеера после окончания воспроизведения
            mediaPlayer.release()
            button.text = "▶️"
            isStopped = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //надо создать связь между заметками и папками, куда они добавляются
        //Навреное добавлять в mainLayout не оч хорошо, так как в текущей реализации
        //он общий для всех папок
        if (requestCode == REQUEST_CODE_VOICE_ACTIVITY && resultCode == RESULT_OK) {
            val noteName: String? = data?.getStringExtra("new_item_text")
            addNoteInMainLayout(noteName)
        }
    }

    private fun reloadMainLayout() {
        mainLayout.removeAllViews()
        val currentFolderDir = getExternalFilesDir(currentFolderName)

        val files = currentFolderDir?.listFiles()
        files?.forEach { file ->
            addNoteInMainLayout(file.name)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.title == "Добавить папку") {
            foldersManager.handleNewFolderButton(this, navView)
        } else {
            currentFolderName = item.title.toString()
            supportActionBar?.title = currentFolderName
            reloadMainLayout()
            drawerLayout.closeDrawers()
        }
        return false
    }
}
