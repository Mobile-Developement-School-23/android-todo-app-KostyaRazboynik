package com.konstantinmuzhik.hw1todoapp.domain.models

sealed class LoadingState<out T> {
    data class Success<out T>(val data: T): LoadingState<T>()
    data class Loading<out T>(val isLoading: Boolean): LoadingState<T>()
    data class Error<T>(val nameError: String): LoadingState<T>()
}