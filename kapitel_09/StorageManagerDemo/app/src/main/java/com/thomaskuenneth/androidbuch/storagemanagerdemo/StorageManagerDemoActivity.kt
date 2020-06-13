package com.thomaskuenneth.androidbuch.storagemanagerdemo

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import kotlinx.android.synthetic.main.activity_main.*

class StorageManagerDemoActivity : AppCompatActivity() {

    private val requestCode = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv.text = ""
        getSystemService(StorageManager::class.java).let {
            for (volume in it.storageVolumes) {
                if (tv.text.isNotEmpty()) tv.append("\n")
                tv.append("${volume.getDescription(this)}\n")
                tv.append(" --> state: ${volume.state}\n")
                tv.append(" --> isPrimary: ${volume.isPrimary}\n")
                tv.append(" --> isRemovable: ${volume.isRemovable}\n")
                tv.append(" --> isEmulated: ${volume.isEmulated}\n")
                if (volume.isPrimary) {
                    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                        volume.createOpenDocumentTreeIntent()
                    else
                        volume.createAccessIntent(Environment.DIRECTORY_DOWNLOADS)
                    startActivityForResult(intent, requestCode)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == this.requestCode &&
                resultCode == Activity.RESULT_OK &&
                data != null) {
            val dir = DocumentFile.fromTreeUri(this,
                    data.data!!)
            tv.append("\n${dir?.uri.toString()}\n")
            for (file in dir?.listFiles() ?: emptyArray()) {
                tv.append(" --> ${file.name}\n")
            }
        }
    }
}