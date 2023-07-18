package com.kostyarazboynik.todoapp.domain.models

/**
 * Data State
 *
 * @author Kovalev Konstantin
 *
 */
sealed class DataState<out T> {
    object Initial : DataState<Nothing>()
    data class Result<T>(val data: T) : DataState<T>()
    data class Exception(val cause: Throwable): DataState<Nothing>()
}
