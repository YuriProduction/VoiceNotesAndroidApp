package com.example.voicenotes

import android.text.InputFilter
import android.text.InputType
import android.view.Menu
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.android.material.navigation.NavigationView
import java.io.File

class FoldersManager {

    fun uploadFolders(mainActivity: MainActivity, navView: NavigationView) {
        val storageDir = mainActivity.getExternalFilesDir(null)
        val folders = storageDir?.listFiles()
        val menu: Menu = navView.menu
        folders?.forEach { folder ->
            addFolderButton(menu, 0, folder.name)
        }
        addFolderButton(menu, 1, "Добавить папку")
    }

    private fun addFolderButton(menu: Menu, orderInCategory: Int, folderName: String) {
        menu.add(Menu.NONE, Menu.NONE, orderInCategory, folderName)
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
                addFolderButton(navView.menu, 0, folderName)
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