package com.kostyarazboynik.todoapp.data.datasource


import android.content.Context
import com.kostyarazboynik.todoapp.utils.Constants.NOTIFICATIONS_PERMISSION_KEY
import com.kostyarazboynik.todoapp.utils.Constants.SHARED_PREFERENCES_NO_TOKEN
import com.kostyarazboynik.todoapp.utils.Constants.THEME_FOLLOW_SYSTEM
import com.kostyarazboynik.todoapp.utils.Constants.TOKEN_API
import java.util.UUID
import javax.inject.Inject

/**
 * Shared Preferences App Settings
 *
 * @author Konstantin Kovalev
 *
 */

class SharedPreferencesAppSettings @Inject constructor(
    context: Context,
) {
    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    init {
        if (!sharedPreferences.contains(SHARED_PREFERENCES_DEVICE_TAG))
            editor.putString(SHARED_PREFERENCES_DEVICE_TAG, UUID.randomUUID().toString())
                .apply()

        if (!sharedPreferences.contains(SHARED_PREFERENCES_TOKEN))
            setCurrentToken(SHARED_PREFERENCES_NO_TOKEN)

        if (!sharedPreferences.contains(NOTIFICATIONS_PERMISSION_KEY))
            setNotificationPermission(false)

        if (!sharedPreferences.contains(SHARED_PREFERENCES_THEME_TAG))
            setTheme(THEME_FOLLOW_SYSTEM)

        if(!sharedPreferences.contains(SHARED_PREFERENCES_NOTIFICATION_TAG))
            editor.putString(SHARED_PREFERENCES_NOTIFICATION_TAG, "hey")

    }

    fun setCurrentToken(token: String) =
        editor.putString(SHARED_PREFERENCES_TOKEN, token).apply()

    fun getCurrentToken(): String =
        sharedPreferences.getString(SHARED_PREFERENCES_TOKEN, null) ?: TOKEN_API

    fun getDeviceId(): String =
        sharedPreferences.getString(SHARED_PREFERENCES_DEVICE_TAG, null) ?: "0d"

    fun putRevisionId(revision: Int) =
        editor.putInt(SHARED_PREFERENCES_REVISION_TAG, revision).apply()

    fun getRevisionId(): Int = sharedPreferences.getInt(SHARED_PREFERENCES_REVISION_TAG, 1)

    fun getTheme(): Int = sharedPreferences.getInt(SHARED_PREFERENCES_THEME_TAG, THEME_FOLLOW_SYSTEM)

    fun setTheme(theme: Int) =
        sharedPreferences.edit().putInt(SHARED_PREFERENCES_THEME_TAG, theme).apply()

    fun getNotificationPermission(): Boolean =
        sharedPreferences.getBoolean(NOTIFICATIONS_PERMISSION_KEY, true)

    fun setNotificationPermission(permission: Boolean) =
        editor.putBoolean(NOTIFICATIONS_PERMISSION_KEY, permission).apply()

    fun getFullCurrentToken(): String = getCurrentToken().toFullToken()

    private fun String.toFullToken() =
        if (this == TOKEN_API) "Bearer $TOKEN_API"
        else "OAuth $this"

    fun addNotification(id: String) =
        editor.putString(SHARED_PREFERENCES_NOTIFICATION_TAG, getNotificationsId() + " $id").apply()

    fun getNotificationsId(): String =
        sharedPreferences.getString(SHARED_PREFERENCES_NOTIFICATION_TAG, "").toString()

    fun removeNotification(id: String) {
        val arr = ArrayList(getNotificationsId().split(" "))
        if (arr.contains(id)) arr.remove(id)
        editor.putString(
            SHARED_PREFERENCES_NOTIFICATION_TAG,
            arr.fold("") { previous, next -> "$previous $next" })
            .apply()
    }

    companion object {
        const val SHARED_PREFERENCES_NAME = "settings"
        const val SHARED_PREFERENCES_REVISION_TAG = "current_revision"
        const val SHARED_PREFERENCES_DEVICE_TAG = "current_device_id"
        const val SHARED_PREFERENCES_TOKEN = "current_token"
        const val SHARED_PREFERENCES_THEME_TAG = "theme"
        const val SHARED_PREFERENCES_NOTIFICATION_TAG = "notifications"
    }
}