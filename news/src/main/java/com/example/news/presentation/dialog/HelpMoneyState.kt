package com.example.news.presentation.dialog

const val MIN_AMOUNT = 1
const val MAX_AMOUNT = 9_999_999
const val LEADING_ZERO = "0"

data class HelpMoneyState(
    val newsId: Int = 0,
    val newsTitle: String = "",
    val selectedAmount: Int? = null,
    val inputText: String = "",
    val isValid: Boolean = false
){
    fun withCustomAmount(input: String): HelpMoneyState {

        val cleanedText = input.filter { character -> character.isDigit() }
        val amountValue = cleanedText.toIntOrNull()
        val hasLeadingZero = cleanedText.length > 1 && cleanedText.startsWith(LEADING_ZERO)
        val isValidAmount =
            amountValue != null && amountValue in MIN_AMOUNT..MAX_AMOUNT && !hasLeadingZero

        val isCustomInput = cleanedText.isNotBlank()

        return this.copy(
            inputText = cleanedText,
            selectedAmount = if (isCustomInput) {
                null
            } else {
                selectedAmount
            },
            isValid = if (isCustomInput) {
                isValidAmount
            } else {
                selectedAmount != null
            }
        )
    }
}
