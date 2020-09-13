package com.thomaskuenneth.androidbuch.halloandroidcompose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ColumnScope.gravity
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContentView()
        }
    }
}

@Composable
fun ContentView() {
    val context = ContextAmbient.current
    val ersterKlick = remember { mutableStateOf(true) }
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val height = 96.dp
    Column(
        horizontalGravity = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        if (ersterKlick.value) {
            GreetingText(
                context.getString(R.string.willkommen)
            )
            Row(
                modifier = Modifier.preferredHeight(height)
            ) {
                OutlinedTextField(
                    value = name.value,
                    placeholder = { Text(context.getString(R.string.vorname_nachname)) },
                    onValueChange = {
                        name.value = it
                    },
                    imeAction = ImeAction.Next,
                    onImeActionPerformed = { _, _ ->
                        ersterKlick.value = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(context.getString(R.string.ihr_name)) }
                )
            }
            MyButton(context.getString(R.string.weiter)) {
                ersterKlick.value = false
            }
        } else {
            GreetingText(
                context.getString(R.string.hallo, name.value.text)
            )
            Spacer(modifier = Modifier.preferredHeight(height))
            MyButton(context.getString(R.string.fertig)) {}
        }
    }
}

@Composable
fun GreetingText(text: String) {
    Text(
        text = text,
        modifier = Modifier.gravity(align = Alignment.Start)
            .preferredHeight(48.dp)
    )
}

@Composable
fun MyButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick
    ) {
        Text(text = text)
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MaterialTheme {
        ContentView()
    }
}