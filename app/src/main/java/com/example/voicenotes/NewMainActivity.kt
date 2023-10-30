package com.example.voicenotes

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class NewMainActivity : AppCompatActivity() {

    // on below line we are creating a variable.
    lateinit var languageLV: ListView
    lateinit var addBtn: Button
    lateinit var itemEdt: EditText
    lateinit var lngList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_activity_main)

        // on below line we are initializing our variables.
        languageLV = findViewById(R.id.idLVLanguages)
        addBtn = findViewById(R.id.idBtnAdd)
        itemEdt = findViewById(R.id.idEdtItemName)
        lngList = ArrayList()

        // on below line we are adding items to our list
        lngList.add("C++")
        lngList.add("Python")

        // on below line we are initializing adapter for our list view.
        val adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
            this@NewMainActivity,
            android.R.layout.simple_list_item_1,
            lngList as List<String?>
        )

        // on below line we are setting adapter for our list view.
        languageLV.adapter = adapter

        // on below line we are adding click listener for our button.
        addBtn.setOnClickListener {

            // on below line we are getting text from edit text
            val item = itemEdt.text.toString()

            // on below line we are checking if item is not empty
            if (item.isNotEmpty()) {
                // on below line we are adding item to our list.
                lngList.add(item)

                // on below line we are notifying adapter
                // that data in list is updated to update our list view.
                adapter.notifyDataSetChanged()
            }
        }

    }
}