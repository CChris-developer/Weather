package com.example.myapplication.utils

sealed class State {
    object Success : State()
    data class Error(val errorMessage: String?) : State()
    data class NoData(val noDataMessage: String?) : State()
}