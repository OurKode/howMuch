package com.example.howmuch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.howmuch.viewmodel.CalculatorViewModel
import com.example.howmuch.ui.screens.MainScreen
import com.example.howmuch.ui.theme.HowMuchTheme

/**
 * Entry point Activity of the 'HowMuch?' calculator application.
 * Binds the UI [MainScreen] within the custom [HowMuchTheme] and sets up state binding.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HowMuchTheme {
                val viewModel: CalculatorViewModel = viewModel()
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
