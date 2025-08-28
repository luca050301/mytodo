package com.example.mytodo.ui.todocreateedit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mytodo.ui.shared.DateInput
import com.example.mytodo.ui.shared.RecommendationsTextInput
import com.example.mytodo.ui.shared.TextInput

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TodoCreateEditScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    viewModel: TodoCreateEditViewModel = hiltViewModel(),
) {
    val todo by viewModel.todo.collectAsState()
    val labels by viewModel.labels.collectAsState()
    var showValidationFails by remember { mutableStateOf(false) }

    val titleIsEmptyValidationError by remember(showValidationFails, todo.title) {
        derivedStateOf {
            if (!showValidationFails) false
            else todo.title.isBlank()
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = remember {
                MutableInteractionSource()
            } // disable ripple effect
        ) {
            keyboardController?.hide()
            focusManager.clearFocus()
        },
        // top app bar
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ), navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
                    Text(
                        text = viewModel.screenTitle,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        },
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.then(
                Modifier
                    .fillMaxSize()
                    .safeContentPadding()
                    .padding(innerPadding)
            )
        ) { // form fields
            // title input
            TextInput(
                value = todo.title,
                onValueChange = {
                    viewModel.updateTodo(todo.copy(title = it))
                },
                label = { Text("Title") },
                isError = titleIsEmptyValidationError,
                supportingText = {
                    if (titleIsEmptyValidationError) Text("Title cannot be empty")
                }
            )
            // description input
            TextInput(
                value = todo.description,
                onValueChange = {
                    viewModel.updateTodo(todo.copy(description = it))
                }, label = { Text("Description") },
                singleLine = false
            )
            // label input with recommendation dropdown
            RecommendationsTextInput(
                value = todo.label ?: "",
                onValueChange = {
                    viewModel.updateTodo(todo.copy(label = it))
                }, onSelectOption = {
                    it?.let { id -> viewModel.updateTodo(todo.copy(label = id)) }
                },
                recommendations = labels,
                label = { Text("Label") }
            )
            // date input
            DateInput(
                todo.date,
                onSave = {
                    viewModel.updateTodo(
                        todo.copy(
                            date = it
                        )
                    )

                }
            )
            // action buttons
            Row {
                Button(onClick = onNavigateBack) {
                    Text(text = "Cancel")
                }
                Button(onClick = {
                    if (todo.title.isBlank()) {
                        showValidationFails = true
                        return@Button
                    }
                    viewModel.save(onNavigateBack)
                }) {
                    Text(text = "Save")
                }
            }
        }
    }
}