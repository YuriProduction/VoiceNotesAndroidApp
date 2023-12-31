package com.example.voicenotes

import android.annotation.SuppressLint
import android.text.InputFilter
import android.text.InputType
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.android.material.navigation.NavigationView
import java.io.File

class FoldersManager {

    @SuppressLint("ResourceType")
    fun uploadFolders(mainActivity: MainActivity, navView: NavigationView) {
        val storageDir = mainActivity.getExternalFilesDir(null)
        val folders = storageDir?.listFiles()
        val menu: Menu = navView.menu
        folders?.forEach { folder ->
            addFolderButton(menu, folder.name)
        }

        addNewFolderButton(menu, mainActivity, navView)
    }

    private fun addNewFolderButton(menu: Menu, mainActivity: MainActivity, navView: NavigationView) {
        // Кнопка добавления папки
        val button = Button(mainActivity)
        button.text = "+"
        button.textSize = 20F
        button.width = 1000
        button.setBackgroundColor(0xFF4CAF50.toInt())
        button.setOnClickListener { handleNewFolderButton(mainActivity, navView) }
        menu.add(Menu.NONE, 1, 1, "").actionView = button
    }

    private fun addFolderButton(menu: Menu, folderName: String) {
        menu.add(Menu.NONE, Menu.NONE, 0, folderName)
    }

    fun handleNewFolderButton(mainActivity: MainActivity, navView: NavigationView) {
        val builderCreateFolder = AlertDialog.Builder(mainActivity)
        builderCreateFolder.setTitle("Введите название папки")

        // Создать поле ввода текста
        val input = EditText(mainActivity)
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.filters =
            arrayOf(InputFilter.LengthFilter(40)) // Максимальная длина 40 символов
        builderCreateFolder.setView(input)

        // Установить кнопку "Отмена"
        builderCreateFolder.setNegativeButton("Отмена") { dialogCreateFolder, _ ->
            dialogCreateFolder.cancel()
        }

        // Установить кнопку "Ок" без закрытия окна
        builderCreateFolder.setPositiveButton("Ок", null)

        // Показать диалоговое окно
        val dialogCreateFolder = builderCreateFolder.create()
        dialogCreateFolder.show()

        // Получить ссылку на кнопку "Ок" и установить слушатель
        val okButton = dialogCreateFolder.getButton(AlertDialog.BUTTON_POSITIVE)
        okButton.setOnClickListener {
            val folderName = input.text.toString()
            if (createFolder(mainActivity, folderName)) {
                dialogCreateFolder.dismiss()
                addFolderButton(navView.menu, folderName)
            } else {
                startDialogExistFolder(mainActivity)
            }
        }
    }

    private fun createFolder(mainActivity: MainActivity, folderName: String): Boolean {
        val storageDir = mainActivity.getExternalFilesDir(null)
        val newFolder = File(storageDir, folderName)
        return !newFolder.exists() && newFolder.mkdirs();
    }

    private fun startDialogExistFolder(mainActivity: MainActivity) {
        val builderExistFolder = AlertDialog.Builder(mainActivity)
        builderExistFolder.setTitle("Папка с таким названием уже существует!")
        builderExistFolder.setNeutralButton("Ок") { dialogExistFolder, _ ->
            dialogExistFolder.dismiss()
        }
        builderExistFolder.create().show()
    }
}