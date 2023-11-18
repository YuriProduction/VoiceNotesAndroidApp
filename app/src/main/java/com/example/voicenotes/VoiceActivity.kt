package com.example.voicenotes

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.romancha.playpause.PlayPauseView
import java.io.File
import java.io.IOException

class VoiceActivity : AppCompatActivity() {

    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.voice_activity)
        val playPauseView = findViewById<PlayPauseView>(R.id.play_pause_view)

        mediaRecorder = MediaRecorder()
        output = Environment.getExternalStorageDirectory().absolutePath + "/recording.mp3"

        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile(output)


        playPauseView.setOnClickListener {
            // View clicked
            playPauseView.toggle()
            if (playPauseView.onPlaying()) {
                if (ContextCompat.checkSelfPermission(
                        this, android.Manifest.permission.RECORD_AUDIO
                    ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    val permissions = arrayOf(
                        android.Manifest.permission.RECORD_AUDIO,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    ActivityCompat.requestPermissions(this, permissions, 0)
                } else {
                    startRecording()
                }
            } else {
                stopRecording()
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
                    val newItemText =
                        enteredText // Replace with the actual item text you want to add
                    val intent = Intent()
                    intent.putExtra("new_item_text", newItemText)
                    setResult(RESULT_OK, intent)
                    finish()
                    val audioDir = getExternalFilesDir("Study")
                    val oldFile = File(audioDir, "record.3gp") // Указываем старое имя файла
                    val newFile =
                        File(audioDir, "$enteredText.3gp") // Создаем новый файл с новым именем
                    if (oldFile.exists()) {
                        val isSuccess = oldFile.renameTo(newFile)
                        if (isSuccess) {
                            Log.d("AudioRecording", "File renamed successfully")
                        } else {
                            Log.e("AudioRecording", "File rename failed")
                        }
                    }

                    dialog.dismiss()
                }
                // Показать диалоговое окно
                builder.create().show()
            }

        }
    }

    private fun startRecording() {
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        val fileName = "record.3gp"
        val audioDir = getExternalFilesDir("Study") // Get app-specific directory
        val audioFile = File(audioDir, fileName) // Create a temporary file

        val audioFilePath = audioFile.absolutePath // Save the file path for later use
        mediaRecorder!!.setOutputFile(audioFilePath)

        try {
            mediaRecorder!!.prepare()
            mediaRecorder!!.start()
            state = true
            // Update the UI or button here if required
        } catch (e: IOException) {
            Log.e("AudioRecording", "prepare() failed")
        }
    }


    private fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                reset()
                release()
            }
            state = false
            // Update the UI or button here if required
        } catch (e: Exception) {
            Log.e("AudioRecording", "stopRecording() failed")
        }
    }


}
