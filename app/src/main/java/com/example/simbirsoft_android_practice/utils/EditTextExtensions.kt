package com.example.simbirsoft_android_practice.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun EditText.textChangesFlow(): Flow<CharSequence> = callbackFlow {
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(
            text: CharSequence?,
            start: Int,
            count: Int,
            after: Int,
        ) = Unit

        override fun onTextChanged(
            text: CharSequence?,
            start: Int,
            before: Int,
            count: Int,
        ) {
            if (text != null) {
                trySend(text)
            }
        }

        override fun afterTextChanged(text: Editable?) = Unit
    }

    addTextChangedListener(textWatcher)
    trySend(text)

    awaitClose { removeTextChangedListener(textWatcher) }
}
