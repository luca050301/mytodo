package com.example.mytodo.ui.todocreateedit

import android.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TodoCreateEditScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    viewModel: TodoCreateEditViewModel = hiltViewModel(),
) {
    val todo by viewModel.todo.collectAsState()
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = todo.date
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    )
    LaunchedEffect(todo.date) {
        if (viewModel.isEdit) {
            dateState.selectedDateMillis = todo.date
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        }
    }
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
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
                    Text(text = viewModel.screenTitle,
                        style = MaterialTheme.typography.titleLarge)
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
        ) {
            TextField(
                value = todo.title,
                onValueChange = {
                    viewModel.updateTodo(todo.copy(title = it))
                },
                label = { Text("Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            TextField(
                value = todo.description,
                onValueChange = {
                    viewModel.updateTodo(todo.copy(description = it))
                }, label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            TextField(
                value = todo.label,
                onValueChange = {
                    viewModel.updateTodo(todo.copy(label = it))
                }, label = { Text("Label") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clickable(onClick = {
                        showDialog = true
                    }),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu_my_calendar),
                    contentDescription = "Select Date",
                )
                Text(
                    text = dateState.selectedDateMillis?.let {
                        LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(it),
                            ZoneId.systemDefault()
                        ).toString()
                    } ?: "Select Date",
                    textAlign = TextAlign.Center
                )
            }
            if (showDialog) {
                DatePickerDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.updateTodo(
                                    todo.copy(
                                        date = LocalDateTime.ofInstant(
                                            Instant.ofEpochMilli(dateState.selectedDateMillis!!),
                                            ZoneId.systemDefault()
                                        )
                                    )
                                )

                                showDialog = false
                            }
                        ) {
                            Text(text = "OK")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showDialog = false }
                        ) {
                            Text(text = "Cancel")
                        }
                    }
                ) {
                    DatePicker(
                        state = dateState,
                        showModeToggle = true
                    )
                }
            }
            Row {
                Button(onClick = onNavigateBack) {
                    Text(text = "Cancel")
                }
                Button(onClick = { viewModel.save(onNavigateBack) }) {
                    Text(text = "Save")
                }
            }
        }
    }
}