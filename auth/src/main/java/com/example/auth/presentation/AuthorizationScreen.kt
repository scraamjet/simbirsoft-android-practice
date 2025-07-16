package com.example.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.auth.R
import com.example.core.R.color
import com.example.core.ui.BodyTextLinkGreen
import com.example.core.ui.BodyTextMediumWhiteCenter
import com.example.core.ui.BodyTextRegularBlackDeep
import com.example.core.ui.CaptionSmallBlackSoft

@Composable
fun AuthorizationScreen(
    state: AuthorizationState,
    onEvent: (AuthorizationEvent) -> Unit
) {
    val buttonCornerRadius = 4.dp

    Scaffold(
        topBar = {
            AuthorizationTopAppBar(
                onBackClick = { onEvent(AuthorizationEvent.BackClicked) }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stringResource(R.string.sign_in_through_social_networks),
                style = BodyTextRegularBlackDeep,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(32.dp))

            SocialIconsRow()

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.login_authorization),
                style = BodyTextRegularBlackDeep,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.email_title),
                style = CaptionSmallBlackSoft,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            EmailInputField(
                email = state.email,
                onEmailChanged = { emailText ->
                    onEvent(AuthorizationEvent.EmailChanged(emailText))
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.password_title),
                style = CaptionSmallBlackSoft,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            PasswordInputField(
                password = state.password,
                isPasswordVisible = state.isPasswordVisible,
                onPasswordChanged = { passwordText ->
                    onEvent(AuthorizationEvent.PasswordChanged(passwordText))
                },
                onToggleVisibility = {
                    onEvent(AuthorizationEvent.TogglePasswordVisibility)
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onEvent(AuthorizationEvent.SubmitClicked) },
                enabled = state.isFormValid,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(buttonCornerRadius),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = color.green),
                    disabledContainerColor = colorResource(id = color.grey)
                )
            ) {
                Text(
                    text = stringResource(R.string.login_button_text).uppercase(),
                    style = BodyTextMediumWhiteCenter
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.forgot_your_password),
                    style = BodyTextLinkGreen
                )

                Text(
                    text = stringResource(R.string.registration),
                    style = BodyTextLinkGreen
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthorizationScreenPreview() {
    AuthorizationScreen(
        state = AuthorizationState(),
        onEvent = {}
    )
}
