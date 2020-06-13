package com.thomaskuenneth.androidbuch.storagemanagerdemo

import android.content.*
import android.os.storage.StorageVolume
import android.widget.Toast

class MediaMountedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null &&
            Intent.ACTION_MEDIA_MOUNTED == intent.action
        ) {
            val volume = intent.getParcelableExtra<StorageVolume>(
                StorageVolume.EXTRA_STORAGE_VOLUME
            )
            if (volume != null) {
                Toast.makeText(
                    context, volume.getDescription(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}