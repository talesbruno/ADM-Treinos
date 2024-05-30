package com.example.teste_tecnico_lealapps.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teste_tecnico_lealapps.domain.model.Training
import com.example.teste_tecnico_lealapps.presentation.viewmodels.SignInViewModel
import com.example.teste_tecnico_lealapps.presentation.viewmodels.TrainingViewModel
import com.example.teste_tecnico_lealapps.utils.Result
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToDetailScreen: (Training) -> Unit,
    onNavigateToCreateTrainingScreen: () -> Unit,
    onNavigateToEditTrainingScreen: (Training) -> Unit,
    trainingViewModel: TrainingViewModel,
    signInViewModel: SignInViewModel,
    email: String
) {
    trainingViewModel.getAllTraining()
    val training by trainingViewModel.training.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                logout = { signInViewModel.logout() },
                email = email,
                closeDrawer = {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }
            )
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Meus Treinos",
                            color = MaterialTheme.colorScheme.surface
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth(),
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }, content = {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu icon",
                                tint = MaterialTheme.colorScheme.surface
                            )
                        })
                    },
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text("Treino") },
                    icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                    onClick = {
                        onNavigateToCreateTrainingScreen()
                    }
                )
            }
        ) {
            when (training) {
                is Result.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Erro ao carregar exercícios")
                    }
                }

                is Result.Initial -> {

                }

                is Result.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is Result.Success -> {
                    val trainingList = training.data ?: emptyList()
                    LazyColumn(
                        modifier = Modifier.padding(it)
                    ) {
                        items(items = trainingList) { training ->
                            TrainingCard(
                                training = training,
                                onNavigateToDetailScreen = onNavigateToDetailScreen,
                                onNavigateToEditTrainingScreen = onNavigateToEditTrainingScreen,
                                trainingViewModel
                            )
                        }
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingCard(
    training: Training,
    onNavigateToDetailScreen: (Training) -> Unit,
    onNavigateToEditTrainingScreen: (Training) -> Unit,
    trainingViewModel: TrainingViewModel,
) {
    Card(
        onClick = {
            onNavigateToDetailScreen(training)
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        content = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = training.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                Divider()
                Text(
                    text = training.description,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(16.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Text(
                        text = training.date,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { onNavigateToEditTrainingScreen(training) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { trainingViewModel.deleteTraining(training) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}


@Composable
fun AppDrawer(
    modifier: Modifier = Modifier,
    closeDrawer: () -> Unit,
    logout: () -> Unit,
    email: String
) {
    ModalDrawerSheet(modifier = Modifier) {
        DrawerHeader(
            modifier,
            email
        )
        Spacer(modifier = Modifier.padding(5.dp))
        NavigationDrawerItem(
            label = {
                Text(
                    text = "Sair",
                    style = MaterialTheme.typography.labelSmall
                )
            },
            selected = false,
            onClick = {
                logout()
                closeDrawer()
            },
            shape = MaterialTheme.shapes.small,
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null
                )
            }
        )
    }
}


@Composable
fun DrawerHeader(
    modifier: Modifier,
    email: String
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary)
            .padding(15.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = email,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

