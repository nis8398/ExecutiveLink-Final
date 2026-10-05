package com.example.executivelink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.executivelink.ui.ExecutiveMainScreen
import com.example.executivelink.ui.theme.ExecutiveLinkTheme
import com.example.executivelink.ui.viewmodel.ExecutiveViewModel
import com.example.executivelink.ui.viewmodel.ExecutiveViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: ExecutiveViewModel by viewModels {
        ExecutiveViewModelFactory((application as ExecutiveLinkApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExecutiveLinkTheme {
                ExecutiveMainScreen(viewModel = viewModel)
            }
        }
    }
}
