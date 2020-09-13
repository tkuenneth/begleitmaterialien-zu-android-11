package com.thomaskuenneth.androidbuch.halloandroidcompose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.input.TextFieldValue
import androidx.ui.tooling.preview.Preview

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Page()
            }
        }
    }
}

@Composable
fun Page() {
    val context = ContextAmbient.current
    val ersterKlick = remember { mutableStateOf(true) }
    val name = remember { mutableStateOf(TextFieldValue("")) }
    Column(
        horizontalGravity = Alignment.CenterHorizontally
    ) {
        if (ersterKlick.value) {
            Text(
                context.getString(R.string.willkommen)
            )
            TextField(
                value = name.value,
                placeholder = { Text(context.getString(R.string.vorname_nachname)) },
                onValueChange = {
                    name.value = it
                },
                label = { Text(context.getString(R.string.ihr_name)) }
            )
            Button(onClick = {
                ersterKlick.value = false
            }) {
                Text(text = context.getString(R.string.weiter))
            }
        } else {
            Text(
                context.getString(R.string.hallo, "")
            )
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MaterialTheme {
        Page()
    }
}