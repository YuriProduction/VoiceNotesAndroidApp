package com.example.voicenotes

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
                //TODO - Реализовать логику добавления в ФС аудиозаписи
            }
        }
    }

}