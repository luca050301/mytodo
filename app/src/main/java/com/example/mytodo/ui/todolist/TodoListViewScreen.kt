package com.example.mytodo.ui.todolist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mytodo.data.Todo

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TodoListViewScreen(
    modifier: Modifier = Modifier,
    onNavigateToCreateEdit: (Long?) -> Unit = {},
    viewModel: TodoListViewModel = hiltViewModel(),
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var currentTodoId by remember { mutableStateOf<Long?>(null) }
    val todoItems by viewModel.todoItems.collectAsState()
    val searchFilters by viewModel.searchFilters.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = "MyToDo",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .safeDrawingPadding()
                .then(modifier)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = searchFilters.query ?: "",
                        onValueChange = {
                            viewModel.setSearchFilters(
                                searchFilters.copy(query = it)
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp),
                        placeholder = {
                            Text(text = "Search")
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null
                            )
                        }

                    )
                    FilterButton(text = "Filter", onClick = {
                    })
                }
                LazyColumn {
                    items(todoItems) { todoItem ->
                        TodoCard(
                            todo = todoItem,
                            oncheckedChange = {
                                viewModel.toggleCompleted(todoItem, it)
                            },
                            onDeleteClick = {
                                currentTodoId = todoItem.id
                                showDeleteDialog = true
                            },
                            onEditClick = {
                                onNavigateToCreateEdit(todoItem.id)
                            },
                            modifier = Modifier
                                .fillMaxSize()
                        )


                    }

                }
            }
            FloatingActionButton(
                onClick = {
                    onNavigateToCreateEdit(null)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .size(64.dp)
                    .align(
                        Alignment.BottomEnd
                    )

            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Todo")
            }
            DeleteDialog(
                isActive = showDeleteDialog,
                onConfirm = {
                    currentTodoId?.let { viewModel.deleteTodoById(it) }
                },
                onDismiss = {
                    showDeleteDialog = false
                }
            )
        }

    }


}

@Composable
fun TodoCard(
    todo: Todo,
    oncheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ), modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable(
                onClick = { isExpanded = !isExpanded }
            )
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Checkbox(checked = todo.isCompleted, onCheckedChange = oncheckedChange)
                Text(
                    text = todo.title,
                    textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else null,
                    modifier = Modifier.weight(1f)
                )
                Column(
                    modifier = Modifier.weight(1f)

                ) {
                    Text(
                        text = todo.date.toLocalDate().toString(),
                    )
                    Label(text = todo.label)
                }

                Column(
                    modifier = Modifier.weight(0.5f)
                ) {
                    IconButton(
                        onClick = onEditClick
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(
                        onClick = onDeleteClick
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
            if (isExpanded) {
                Text(
                    text = todo.description,
                    modifier = Modifier
                        .padding(10.dp)
                )
            }

        }
    }
}

@Composable
fun FilterButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .width(100.dp)
            .padding(10.dp), shape = RoundedCornerShape(50),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Text(text = text)

    }
}

@Composable
fun Label(text: String, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        colors = CardDefaults.elevatedCardColors()
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(10.dp)
        )
    }
}

@Composable
fun DeleteDialog(
    isActive: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isActive) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "Delete ToDo")
            },
            text = {
                Text("Are you sure you want to delete this ToDo?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text("Cancel")
                }
            },
            modifier = modifier
        )
    }
}