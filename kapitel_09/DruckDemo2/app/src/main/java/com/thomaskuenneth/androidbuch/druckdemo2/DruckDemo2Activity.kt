package com.thomaskuenneth.androidbuch.druckdemo2

import android.os.Bundle
import android.print.PrintManager
import androidx.appcompat.app.AppCompatActivity

class DruckDemo2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val printManager = getSystemService(PrintManager::class.java)
        if (printManager != null) {
            val jobName = getString(R.string.app_name) + " Document"
            printManager.print(
                jobName,
                DemoPrintDocumentAdapter(this), null
            )
        }
    }
}