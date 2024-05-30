package com.example.teste_tecnico_lealapps.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.teste_tecnico_lealapps.Graph
import com.example.teste_tecnico_lealapps.domain.model.Exercise
import com.example.teste_tecnico_lealapps.domain.model.ExerciseDto
import com.example.teste_tecnico_lealapps.domain.model.Training
import com.example.teste_tecnico_lealapps.presentation.CreateExerciseScreen
import com.example.teste_tecnico_lealapps.presentation.CreateTrainingScreen
import com.example.teste_tecnico_lealapps.presentation.EditTrainingScreen
import com.example.teste_tecnico_lealapps.presentation.HomeScreen
import com.example.teste_tecnico_lealapps.presentation.TrainingScreen
import com.example.teste_tecnico_lealapps.presentation.viewmodels.SignInViewModel
import com.example.teste_tecnico_lealapps.presentation.viewmodels.TrainingViewModel

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.homeGraph(
    navHostController: NavHostController,
    trainingViewModel: TrainingViewModel,
    signInViewModel: SignInViewModel,
    email: String
) {
    navigation(route = Graph.HOME, startDestination = HomeScreensRoute.Home.route) {
        composable(route = HomeScreensRoute.Home.route) {
            HomeScreen(
                onNavigateToDetailScreen = {
                    navHostController.navigate(HomeScreensRoute.DetailTrainingScreen.createRoute(it.uuid.toString()))
                },
                onNavigateToCreateTrainingScreen = {
                    navHostController.navigate(HomeScreensRoute.CreateTraining.route)
                },
                onNavigateToEditTrainingScreen = {
                    navHostController.navigate(HomeScreensRoute.UpdateTraining.createRoute(it.uuid.toString()))
                },
                trainingViewModel,
                signInViewModel,
                email
            )
        }
        composable(
            route = HomeScreensRoute.DetailTrainingScreen.route,
            arguments = listOf(navArgument("trainingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val trainingId = backStackEntry.arguments?.getString("trainingId")
            requireNotNull(trainingId)
            TrainingScreen(
                trainingId,
                onNavigateToCreateExerciseScreen = {
                    navHostController.navigate(HomeScreensRoute.CreateExercise.createRoute(it))
                },
                trainingViewModel
            )
        }
        composable(route = HomeScreensRoute.CreateTraining.route) {
            CreateTrainingScreen(
                trainingViewModel,
                { navHostController.popBackStack() }
            )
        }
        composable(
            route = HomeScreensRoute.CreateExercise.route,
            arguments = listOf(navArgument("trainingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val trainingId = backStackEntry.arguments?.getString("trainingId")
            requireNotNull(trainingId)
            CreateExerciseScreen(
                trainingUuid = trainingId,
                trainingViewModel,
                { navHostController.popBackStack() }
            )
        }
        composable(
            route = HomeScreensRoute.UpdateTraining.route,
            arguments = listOf(navArgument("trainingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val trainingId = backStackEntry.arguments?.getString("trainingId")
            requireNotNull(trainingId)
            EditTrainingScreen(trainingUuid = trainingId, trainingViewModel = trainingViewModel)
        }
    }
}

sealed class HomeScreensRoute(val route: String) {
    object Home : HomeScreensRoute(route = "home")
    object CreateTraining : HomeScreensRoute(route = "createTraining")
    object CreateExercise : HomeScreensRoute(route = "createTraining/{trainingId}") {
        fun createRoute(trainingId: String) = "createTraining/$trainingId"
    }

    object UpdateTraining : HomeScreensRoute(route = "updateTraining/{trainingId}") {
        fun createRoute(trainingId: String) = "updateTraining/$trainingId"
    }

    object DetailTrainingScreen : HomeScreensRoute(route = "detailTraining/{trainingId}") {
        fun createRoute(trainingId: String) = "detailTraining/$trainingId"
    }

}