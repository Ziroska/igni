package ru.igni.manager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.igni.manager.ui.IgniApp
import ru.igni.manager.ui.theme.IgniTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IgniTheme {
                IgniApp()
            }
        }
    }
}
