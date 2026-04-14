package com.example.eduapp1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eduapp1.database.AppDatabase
import com.example.eduapp1.repository.GameRepository
import com.example.eduapp1.screen.GameScreen
import com.example.eduapp1.screen.LandingScreen
import com.example.eduapp1.screen.ScoreScreen
import com.example.eduapp1.screen.SettingScreen
import com.example.eduapp1.screen.TestDBScreen
import com.example.eduapp1.ui.theme.EduApp1Theme
import com.example.eduapp1.viewmodel.AppViewModel
import com.example.eduapp1.viewmodel.AppViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduApp1Theme {
                val context = LocalContext.current
                val database = remember { AppDatabase.getDatabase(context) }
                val repository = remember { GameRepository(database.appDao()) }
                val factory = remember { AppViewModelFactory(repository) }
                val viewModel: AppViewModel = viewModel(factory = factory)
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Routes.LANDING
                ) {
                    composable(Routes.LANDING) {
                        LandingScreen(navController, viewModel)
                    }
                    composable(Routes.SETTING) {
                        SettingScreen(navController, viewModel)
                    }
                    composable(Routes.GAME) {
                        GameScreen(navController, viewModel)
                    }
                    composable(Routes.SCORE) {
                        ScoreScreen(navController, viewModel)
                    }
                    composable(Routes.TESTDB) {
                        TestDBScreen(viewModel)
                    }
                }
            }
        }
    }
}
