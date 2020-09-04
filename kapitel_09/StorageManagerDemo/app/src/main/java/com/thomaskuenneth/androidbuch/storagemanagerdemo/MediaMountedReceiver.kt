package com.thomaskuenneth.androidbuch.storagemanagerdemo

import android.content.*
import android.os.storage.StorageVolume
import android.widget.Toast

class MediaMountedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (Intent.ACTION_MEDIA_MOUNTED == intent?.action) {
            intent.getParcelableExtra<StorageVolume>(
                StorageVolume.EXTRA_STORAGE_VOLUME
            )?.let { volume ->
                Toast.makeText(
                    context, volume.getDescription(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}