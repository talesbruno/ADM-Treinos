package com.example.teste_tecnico_lealapps.presentation.navigation


import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.teste_tecnico_lealapps.Graph
import com.example.teste_tecnico_lealapps.presentation.SignInScreen
import com.example.teste_tecnico_lealapps.presentation.SignUpScreen
import com.example.teste_tecnico_lealapps.presentation.viewmodels.SignInViewModel

fun NavGraphBuilder.authGraph(
    signInViewModel: SignInViewModel,
    navController: NavHostController,
    onNavigateToHomeAccount: () -> Unit,
    onNavigateToPlashAccount: () -> Unit,
) {
    navigation(route = Graph.AUTH, startDestination = AuthScreenRout.SignIn.route) {
        composable(route = AuthScreenRout.SignIn.route) {
            SignInScreen(
                signInViewModel = signInViewModel,
                onNavigateToHomeAccount = onNavigateToHomeAccount,
                onNavigateToPlashAccount = onNavigateToPlashAccount,
                onNavigateToCreateAccount = {
                    navController.navigate(AuthScreenRout.SignUp.route)
                }
            )
        }
        composable(route = AuthScreenRout.SignUp.route) {
            SignUpScreen(signInViewModel)
        }
    }
}

sealed class AuthScreenRout(val route: String) {
    object SignIn : AuthScreenRout(route = "signin")
    object SignUp : AuthScreenRout(route = "signup")
}