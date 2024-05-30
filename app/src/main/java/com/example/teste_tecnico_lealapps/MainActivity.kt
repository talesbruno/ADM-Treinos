package com.example.teste_tecnico_lealapps

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.teste_tecnico_lealapps.domain.model.Exercise
import com.example.teste_tecnico_lealapps.presentation.SplashScreen
import com.example.teste_tecnico_lealapps.presentation.navigation.authGraph
import com.example.teste_tecnico_lealapps.presentation.navigation.homeGraph
import com.example.teste_tecnico_lealapps.presentation.viewmodels.SignInViewModel
import com.example.teste_tecnico_lealapps.presentation.viewmodels.TrainingViewModel
import com.example.teste_tecnico_lealapps.ui.theme.TestetecnicoLealAppsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val signInViewModel by viewModels<SignInViewModel>()
    private val trainingViewModel by viewModels<TrainingViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestetecnicoLealAppsTheme {
                val navController = rememberNavController()
                val state by signInViewModel.isUserAuthenticated.collectAsStateWithLifecycle()
                LaunchedEffect(state) {
                    if (state.isAuthenticated) {
                        navController.navigate(Graph.HOME)
                    }
                }
                NavHost(navController = navController, startDestination = Graph.AUTH) {
                    authGraph(
                        signInViewModel = signInViewModel,
                        navController = navController,
                        onNavigateToHomeAccount = {
                            navController.navigate(Graph.HOME)
                        },
                        onNavigateToPlashAccount = {
                            navController.navigate(Graph.SPLASH)
                        }
                    )
                    homeGraph(
                        navController,
                        trainingViewModel,
                        signInViewModel,
                        email = state.email
                    )
                    composable(Graph.SPLASH) {
                        SplashScreen()
                    }
                }
            }
        }
    }
}

object Graph {
    const val AUTH = "auth"
    const val HOME = "main"
    const val SPLASH = "splash"
}

