package com.thomaskuenneth.androidbuch.halloandroidcompose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ColumnScope.gravity
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
// import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
// import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.ui.tooling.preview.Preview

private val HEIGHT = 96.dp
class HalloAndroidComposeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContentView { finish() }
            // SliderView { println(it) }
        }
    }
}

//@Composable
//fun SliderView(onValueChange: (Float) -> Unit) {
//    var position by remember { mutableStateOf(4f) }
//    Column(
//        modifier = Modifier.fillMaxWidth().padding(16.dp),
//    ) {
//        Slider(
//            value = position,
//            valueRange = 0f..10f,
//            onValueChange = {
//                position = it
//                onValueChange(it)
//            })
//        Text(
//            text = position.toInt().toString(),
//            modifier = Modifier.fillMaxWidth(),
//            style = TextStyle(fontSize = TextUnit.Companion.Sp(32)),
//            textAlign = TextAlign.Center
//        )
//    }
//}


@Composable
fun ContentView(finish: () -> Unit) {
    val firstPage = remember { mutableStateOf(true) }
    val name = remember { mutableStateOf("") }
    Column(
        horizontalGravity = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        if (firstPage.value) {
            FirstPage(HEIGHT, name.value) { currentName: String ->
                firstPage.value = false
                name.value = currentName
            }
        } else {
            SecondPage(HEIGHT, name.value) { finish() }
        }
    }
}

@Composable
fun FirstPage(height: Dp, initial: String, onClick: (name: String)
-> Unit) {
    val name = remember { mutableStateOf(initial) }
    val enabled = remember { mutableStateOf(false) }
    GreetingText(
        stringResource(R.string.welcome)
    )
    Box(
        modifier = Modifier.preferredHeight(height)
    ) {
        OutlinedTextField(
            value = name.value,
            placeholder = { Text(stringResource(R.string.firstname_surname)) },
            onValueChange = {
                name.value = it
                enabled.value = name.value.isNotEmpty()
            },
            imeAction = ImeAction.Next,
            onImeActionPerformed = { _, _ ->
                if (enabled.value)
                    onClick(name.value)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.your_name)) }
        )
    }
    MyButton(stringResource(R.string.next), enabled.value) {
        onClick(name.value)
    }
}

@Composable
fun SecondPage(height: Dp, name: String, onClick: () -> Unit) {
    GreetingText(
        stringResource(R.string.hallo, name)
    )
    Spacer(modifier = Modifier.preferredHeight(height))
    MyButton(stringResource(R.string.done), true) { onClick() }
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