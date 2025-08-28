package com.example.mytodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mytodo.ui.theme.MyToDoTheme
import com.example.mytodo.ui.todocreateedit.TodoCreateEditScreen
import com.example.mytodo.ui.todolist.TodoListViewScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyToDoTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = ListScreen
                ) {
                    composable<ListScreen> {
                        TodoListViewScreen(
                            onNavigateToCreateEdit = { id: Long? ->
                                navController.navigate(CreateEditScreen(id))
                            }
                        )
                    }
                    composable<CreateEditScreen> { backStackEntry ->
                        TodoCreateEditScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }

    }
}

@Serializable
object ListScreen

@Serializable
data class CreateEditScreen(val todoId: Long? = null)



