package com.thomaskuenneth.androidbuch.dialogdemo

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class DialogDemoActivity : AppCompatActivity(),
    DatePickerDialog.OnDateSetListener,
    DialogInterface.OnClickListener {

    private lateinit var datePickerFragment: DatePickerFragment
    private lateinit var alertFragment: AlertFragment
    private lateinit var textview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textview = findViewById(R.id.textview)
        // DatePicker
        datePickerFragment = DatePickerFragment()
        val buttonDatePicker = findViewById<Button>(R.id.button_datepicker)
        buttonDatePicker.setOnClickListener {
            datePickerFragment.show(supportFragmentManager,
                DatePickerFragment.TAG)
        }
        // Alert
        alertFragment = AlertFragment()
        val buttonAlert = findViewById<Button>(R.id.button_alert)
        buttonAlert.setOnClickListener {
            alertFragment.show(supportFragmentManager,
                AlertFragment.TAG)
        }
    }

    override fun onDateSet(view: DatePicker?,
                           year: Int, monthOfYear: Int, dayOfMonth: Int) {
        textview.text = getString(R.string.button_datepicker)
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        textview.text = getString(R.string.button_alert)
    }
}