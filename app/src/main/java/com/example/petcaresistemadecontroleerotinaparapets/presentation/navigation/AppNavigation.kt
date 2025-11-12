package com.example.petcaresistemadecontroleerotinaparapets.presentation.navigation

// ✅ --- IMPORTS ADICIONADOS ---
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
// --- FIM DA ADIÇÃO ---

// ... (seus imports de ViewModels e Telas existentes) ...
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.AuthViewModel
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.EventoViewModel
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.PetViewModel
import com.example.petcaresistemadecontroleerotinaparapets.presentation.screens.*


@Composable
fun AppNavigation() {
    // ... (O resto do arquivo está correto e não precisa de mudanças) ...
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val petViewModel: PetViewModel = hiltViewModel()
    val eventoViewModel: EventoViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = ScreenRoutes.Login.route) {

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

        // --- TELA DE CADASTRO (NOVA) ---
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
                onSettingsClick = {
                    navController.navigate(ScreenRoutes.Settings.route)
                }
            )
        }

        // --- Tela Adicionar Pet ---
        composable(ScreenRoutes.AddPet.route) {
            AddPetScreen(
                petViewModel = petViewModel,
                authViewModel = authViewModel,
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
                eventoViewModel = eventoViewModel,
                authViewModel = authViewModel,
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

        // --- TELA DE RELATÓRIOS (NOVA) ---
        composable(
            route = ScreenRoutes.Reports.route,
            arguments = listOf(navArgument("petId") { type = NavType.StringType })
        ) { backStackEntry ->
            ReportsScreen(
                petId = backStackEntry.arguments?.getString("petId"),
                navController = navController,
                petViewModel = petViewModel,
                eventoViewModel = eventoViewModel
            )
        }
    }
}
// ... (O objeto ScreenRoutes permanece o mesmo) ...
object ScreenRoutes {
    object Login { val route = "login_screen" }
    object SignUp { val route = "signup_screen" }
    object MyPets { val route = "my_pets_screen" }
    object AddPet { val route = "add_pet_screen" }
    object PetDetail { val route = "pet_detail_screen/{petId}" }
    object AddEvent { val route = "add_event_screen/{petId}" }
    object Reminders { val route = "reminders_screen" }
    object Settings { val route = "settings_screen" }
    object Reports { val route = "reports_screen/{petId}" }

    // Funções auxiliares para navegação com argumentos
    fun petDetail(petId: String) = "pet_detail_screen/$petId"
    fun addEvent(petId: String) = "add_event_screen/$petId"
    fun reports(petId: String) = "reports_screen/$petId"
}