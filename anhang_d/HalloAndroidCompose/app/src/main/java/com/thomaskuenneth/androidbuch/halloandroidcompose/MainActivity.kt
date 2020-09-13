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
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContentView { finish() }
        }
    }
}

@Composable
fun ContentView(finish: () -> Unit) {
    val firstPage = remember { mutableStateOf(true) }
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val enabled = remember { mutableStateOf(false) }
    val height = 96.dp
    Column(
        horizontalGravity = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        if (firstPage.value) {
            GreetingText(
                stringResource(R.string.welcome)
            )
            Row(
                modifier = Modifier.preferredHeight(height)
            ) {
                OutlinedTextField(
                    value = name.value,
                    placeholder = { Text(stringResource(R.string.vorname_nachname)) },
                    onValueChange = {
                        name.value = it
                        enabled.value = name.value.text.isNotEmpty()
                    },
                    imeAction = ImeAction.Next,
                    onImeActionPerformed = { _, _ ->
                        firstPage.value = !enabled.value
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.your_name)) }
                )
            }
            MyButton(stringResource(R.string.next), enabled.value) {
                firstPage.value = false
            }
        } else {
            GreetingText(
                stringResource(R.string.hallo, name.value.text)
            )
            Spacer(modifier = Modifier.preferredHeight(height))
            MyButton(stringResource(R.string.done), true) { finish() }
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
fun MyButton(text: String, enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled
    ) {
        Text(text = text)
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MaterialTheme {
        ContentView {}
    }
}