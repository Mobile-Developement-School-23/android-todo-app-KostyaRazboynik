package com.konstantinmuzhik.hw1todoapp.data.datasource

import android.content.Context
import com.konstantinmuzhik.hw1todoapp.utils.Constants.SHARED_PREFERENCES_DEVICE_TAG
import com.konstantinmuzhik.hw1todoapp.utils.Constants.SHARED_PREFERENCES_NAME
import com.konstantinmuzhik.hw1todoapp.utils.Constants.SHARED_PREFERENCES_NO_TOKEN
import com.konstantinmuzhik.hw1todoapp.utils.Constants.SHARED_PREFERENCES_REVISION_TAG
import com.konstantinmuzhik.hw1todoapp.utils.Constants.SHARED_PREFERENCES_THEME_TAG
import com.konstantinmuzhik.hw1todoapp.utils.Constants.SHARED_PREFERENCES_TOKEN
import com.konstantinmuzhik.hw1todoapp.utils.Constants.TOKEN_API
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


import java.util.UUID
import javax.inject.Inject

/**
 * Shared Preferences App Settings
 *
 * @author Konstantin Kovalev
 *
 */
class SharedPreferencesAppSettings @Inject constructor(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    init {
        if (!sharedPreferences.contains(SHARED_PREFERENCES_DEVICE_TAG))
            editor.putString(SHARED_PREFERENCES_DEVICE_TAG, UUID.randomUUID().toString()).apply()

        if (!sharedPreferences.contains(SHARED_PREFERENCES_TOKEN))
            setCurrentToken(SHARED_PREFERENCES_NO_TOKEN)
    }

    fun setCurrentToken(token: String) =
        editor.putString(SHARED_PREFERENCES_TOKEN, token).apply()


    fun getCurrentToken(): String =
        sharedPreferences.getString(SHARED_PREFERENCES_TOKEN, null) ?: "Bearer $TOKEN_API"


    fun getDeviceId(): String =
        sharedPreferences.getString(SHARED_PREFERENCES_DEVICE_TAG, null) ?: "0d"

    fun putRevisionId(revision: Int) =
        editor.putInt(SHARED_PREFERENCES_REVISION_TAG, revision).apply()


    fun getRevisionId(): Int = sharedPreferences.getInt(SHARED_PREFERENCES_REVISION_TAG, 1)

    fun getTheme() =
        sharedPreferences.getInt(SHARED_PREFERENCES_THEME_TAG, 2)

    fun setTheme(theme: Int) =
        sharedPreferences.edit().putInt(SHARED_PREFERENCES_THEME_TAG, theme).apply()

}