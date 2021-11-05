package io.github.collins993.budgettracker2.models


data class Income(
    var date: String? = "",
    var amount: String? = "",
    var category: String? = "",
    var account: String? = "",
    var note: String? = "",
    var uid: String? = ""
)
