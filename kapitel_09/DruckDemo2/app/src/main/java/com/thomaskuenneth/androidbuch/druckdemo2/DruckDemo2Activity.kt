package com.thomaskuenneth.androidbuch.druckdemo2

import android.os.Bundle
import android.print.PrintManager
import androidx.appcompat.app.AppCompatActivity

class DruckDemo2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSystemService(PrintManager::class.java)?.let {
            val jobName = "${getString(R.string.app_name)} Document"
            it.print(
                jobName,
                DemoPrintDocumentAdapter(this), null
            )
        }
    }
}