package com.example.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.core.R.color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.auth.R
import androidx.compose.ui.unit.dp
import com.example.core.ui.BodyTextInputGrayFullWidth
import com.example.core.ui.BodyTextRegularBlackDeep

@Composable
fun PasswordInputField(
    password: String,
    isPasswordVisible: Boolean,
    onPasswordChanged: (String) -> Unit,
    onToggleVisibility: () -> Unit
) {
    val visibilityIcon = if (isPasswordVisible) {
        R.drawable.ic_hide_password
    } else {
        R.drawable.ic_open_password
    }

    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        BasicTextField(
            value = password,
            onValueChange = onPasswordChanged,
            singleLine = true,
            textStyle = BodyTextRegularBlackDeep,
            cursorBrush = SolidColor(Color.Black),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState -> isFocused = focusState.isFocused },
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (password.isEmpty()) {
                            Text(
                                text = stringResource(R.string.hint_input_password),
                                style = BodyTextInputGrayFullWidth
                            )
                        }
                        innerTextField()
                    }

                    Box(
                        modifier = Modifier.size(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = onToggleVisibility) {
                            Icon(
                                painter = painterResource(id = visibilityIcon),
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    if (isFocused) {
                        colorResource(id = color.green)
                    } else {
                        colorResource(id = color.grey)
                    }
                )
        )
    }
}



