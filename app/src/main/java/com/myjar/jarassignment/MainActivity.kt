package com.myjar.jarassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.myjar.jarassignment.ui.composables.AppNavigation
import com.myjar.jarassignment.ui.theme.JarAssignmentTheme
import com.myjar.jarassignment.ui.vm.JarViewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<JarViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init(this)
        enableEdgeToEdge()
        setContent {
            LaunchedEffect(Unit) {
                viewModel.fetchData()
            }
            JarAssignmentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel,
                    )
                }
            }
        }
    }
}