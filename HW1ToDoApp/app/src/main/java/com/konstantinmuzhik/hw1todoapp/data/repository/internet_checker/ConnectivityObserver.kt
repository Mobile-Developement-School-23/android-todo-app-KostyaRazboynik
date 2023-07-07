package com.konstantinmuzhik.hw1todoapp.data.repository.internet_checker

import kotlinx.coroutines.flow.Flow

/**
 * Interface for Connectivity Observer
 *
 * @author Kovalev Konstantin
 *
 */
interface ConnectivityObserver {

    fun observe(): Flow<Status>

    enum class Status {
        Available, Unavailable, Losing, Lost
    }
}