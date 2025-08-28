package com.example.mytodo.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mytodo.utils.DateUtils
import java.time.LocalDateTime

@Composable
fun TextInput(
    value: String?,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    onFocusChange: (Boolean) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    val colorUnfocused = MaterialTheme.colorScheme.surfaceVariant
    val colorFocused = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
    TextField(
        value = value ?: "",
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .onFocusChanged(
                    {
                        onFocusChange(it.isFocused)
                    }
                )

        ),
        onValueChange = onValueChange,
        label = label,
        leadingIcon = leadingIcon,
        shape = RoundedCornerShape(15),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorFocused,
            unfocusedContainerColor = colorUnfocused,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = singleLine,
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        supportingText = supportingText,
        isError = isError

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInput(
    date: LocalDateTime?,
    onSave: (LocalDateTime) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = date?.let { DateUtils.localDateTimeToMillis(it) }
    )

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(date) {
        if (date != null) {
            dateState.selectedDateMillis = DateUtils.localDateTimeToMillis(date)
        }
    }
    Row(
        modifier = modifier
            .fillMaxWidth().padding(vertical = 10.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(15))
            .padding(vertical = 10.dp)
            .clickable(
                onClick = { showDialog = true }
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_my_calendar),
            contentDescription = "Select Date",
        )
        Text(
            text = dateState.selectedDateMillis?.let {
                DateUtils.formatDate(DateUtils.millisToLocalDateTime(it))
            } ?: "Select Date",


            )
    }
    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        dateState.selectedDateMillis?.let {
                            onSave(
                                DateUtils.millisToLocalDateTime(
                                    it
                                )
                            )
                        }
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

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationsTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSelectOption: (String?) -> Unit,
    recommendations: List<String>,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    var isFocused by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = isFocused,
        onExpandedChange = { },
        modifier = modifier.fillMaxWidth()
    ) {
        TextInput(
            value = value,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryEditable, true)
                .onFocusChanged {
                    if (isFocused != it.isFocused) {
                        isFocused = it.isFocused
                    }
                },
            onValueChange = onValueChange,
            label = label,
            leadingIcon = leadingIcon,

            )
        ExposedDropdownMenu(
            expanded = isFocused && recommendations.isNotEmpty(),
            onDismissRequest = {

            }
        ) {
            recommendations.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = { onSelectOption(it) }
                )
            }
        }
    }
}
