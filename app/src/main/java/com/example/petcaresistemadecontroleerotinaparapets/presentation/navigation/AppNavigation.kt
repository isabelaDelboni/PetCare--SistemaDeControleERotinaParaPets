package com.example.petcaresistemadecontroleerotinaparapets.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.petcaresistemadecontroleerotinaparapets.presentation.screens.*
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.AuthViewModel
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.EventoViewModel
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.PetViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val petViewModel: PetViewModel = hiltViewModel()
    val eventoViewModel: EventoViewModel = hiltViewModel()

    // Lógica de Login Persistente
    val isLoggedIn = authViewModel.getCurrentUser() != null

    val startDestination = if (isLoggedIn) {
        ScreenRoutes.MyPets.route
    } else {
        ScreenRoutes.Login.route
    }

    NavHost(navController = navController, startDestination = startDestination) {

        // --- Tela de Login ---
        composable(ScreenRoutes.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(ScreenRoutes.MyPets.route) {
                        popUpTo(ScreenRoutes.Login.route) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate(ScreenRoutes.SignUp.route)
                }
            )
        }

        // --- Tela de Cadastro ---
        composable(ScreenRoutes.SignUp.route) {
            SignUpScreen(
                authViewModel = authViewModel,
                onSignUpSuccess = {
                    navController.navigate(ScreenRoutes.MyPets.route) {
                        popUpTo(ScreenRoutes.Login.route) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // --- Tela Meus Pets ---
        composable(ScreenRoutes.MyPets.route) {
            MyPetsScreen(
                petViewModel = petViewModel,
                authViewModel = authViewModel,
                onPetClick = { petId ->
                    navController.navigate(ScreenRoutes.petDetail(petId))
                },
                onAddPetClick = {
                    navController.navigate(ScreenRoutes.AddPet.route)
                },
                onEditPetClick = { petId ->
                    navController.navigate(ScreenRoutes.editPet(petId))
                },
                onSettingsClick = {
                    navController.navigate(ScreenRoutes.Settings.route)
                }
            )
        }

        // --- Tela Adicionar Pet (CORRIGIDA) ---
        composable(ScreenRoutes.AddPet.route) {
            AddPetScreen(
                viewModel = petViewModel, // Mudou de petViewModel para viewModel
                onBackClick = { navController.popBackStack() }, // Adicionado
                onPetSaved = {
                    navController.popBackStack()
                }
                // authViewModel e petId foram removidos da AddPetScreen atual
            )
        }

        // --- Tela Editar Pet ---
        composable(
            route = ScreenRoutes.EditPet.route,
            arguments = listOf(navArgument("petId") { type = NavType.StringType })
        ) { backStackEntry ->
            // NOTA: A AddPetScreen nova (com foto) ainda não tem a lógica de carregar edição.
            // Estamos usando assim para não dar erro de compilação, mas ela vai abrir vazia por enquanto.
            AddPetScreen(
                viewModel = petViewModel,
                onBackClick = { navController.popBackStack() },
                onPetSaved = {
                    navController.popBackStack()
                }
            )
        }

        // --- Tela Detalhes do Pet ---
        composable(
            route = ScreenRoutes.PetDetail.route,
            arguments = listOf(navArgument("petId") { type = NavType.StringType })
        ) { backStackEntry ->
            PetDetailScreen(
                petId = backStackEntry.arguments?.getString("petId"),
                navController = navController,
                petViewModel = petViewModel,
                eventoViewModel = eventoViewModel,
                onAddEventClick = {
                    val petId = backStackEntry.arguments?.getString("petId")
                    if (petId != null) {
                        navController.navigate(ScreenRoutes.addEvent(petId))
                    }
                },
                onEditEventClick = { eventoId ->
                    navController.navigate(ScreenRoutes.editEvent(eventoId))
                }
            )
        }

        // --- Tela Adicionar Evento ---
        composable(
            route = ScreenRoutes.AddEvent.route,
            arguments = listOf(navArgument("petId") { type = NavType.StringType })
        ) { backStackEntry ->
            AddEventScreen(
                petId = backStackEntry.arguments?.getString("petId"),
                eventoId = null, // MODO DE ADIÇÃO
                eventoViewModel = eventoViewModel,
                onEventSaved = {
                    navController.popBackStack()
                }
            )
        }

        // --- Tela Editar Evento ---
        composable(
            route = ScreenRoutes.EditEvent.route,
            arguments = listOf(navArgument("eventoId") { type = NavType.StringType })
        ) { backStackEntry ->
            AddEventScreen(
                petId = null,
                eventoId = backStackEntry.arguments?.getString("eventoId"), // MODO DE EDIÇÃO
                eventoViewModel = eventoViewModel,
                onEventSaved = {
                    navController.popBackStack()
                }
            )
        }

        // --- Tela de Lembretes ---
        composable(ScreenRoutes.Reminders.route) {
            RemindersScreen(
                navController = navController
            )
        }

        // --- Tela de Configurações ---
        composable(ScreenRoutes.Settings.route) {
            SettingsScreen(
                navController = navController,
                authViewModel = authViewModel,
                onLogout = {
                    navController.navigate(ScreenRoutes.Login.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            )
        }
    }
}

// ... (resto do AppNavigation) ...

// Objeto de Rotas (ATUALIZADO)
object ScreenRoutes {
    object Login { val route = "login_screen" }
    object SignUp { val route = "signup_screen" }
    object MyPets { val route = "my_pets_screen" }
    object AddPet { val route = "add_pet_screen" }
    object EditPet { val route = "edit_pet_screen/{petId}" }
    object PetDetail { val route = "pet_detail_screen/{petId}" }
    object AddEvent { val route = "add_event_screen/{petId}" }
    object EditEvent { val route = "edit_event_screen/{eventoId}" }
    object Reminders { val route = "reminders_screen" }
    object Settings { val route = "settings_screen" }

    // --- LINHA ADICIONADA ---
    object Reports { val route = "reports_screen/{petId}" }

    // Funções auxiliares
    fun petDetail(petId: String) = "pet_detail_screen/$petId"
    fun editPet(petId: String) = "edit_pet_screen/$petId"
    fun addEvent(petId: String) = "add_event_screen/$petId"
    fun editEvent(eventoId: String) = "edit_event_screen/$eventoId"

    // --- FUNÇÃO ADICIONADA ---
    fun reports(petId: String) = "reports_screen/$petId"
}