package com.BzNpXqL.vTmJrK

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.BzNpXqL.vTmJrK.core.di.ServiceLocator
import com.BzNpXqL.vTmJrK.core.theme.AppTheme
import com.BzNpXqL.vTmJrK.presentation.app.AppRoot

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ServiceLocator.init(applicationContext)
        setContent {
            AppTheme {
                AppRoot()
            }
        }
    }
}
