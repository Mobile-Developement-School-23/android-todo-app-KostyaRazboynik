package com.konstantinmuzhik.hw1todoapp.data.datasource


import android.content.Context
import com.konstantinmuzhik.hw1todoapp.utils.Constants.SHARED_PREFERENCES_NO_TOKEN
import com.konstantinmuzhik.hw1todoapp.utils.Constants.SHOW_NOTIFICATIONS_KEY
import java.util.UUID
import javax.inject.Inject

/**
 * Shared Preferences App Settings
 *
 * @author Konstantin Kovalev
 *
 */
// TODO add threads
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

        if (!sharedPreferences.contains(SHARED_PREFERENCES_THEME_TAG))
            setNotificationPermission(false)

        if (!sharedPreferences.contains(SHARED_PREFERENCES_THEME_TAG))
            setTheme(2)
    }

    fun setCurrentToken(token: String) =
        editor.putString(SHARED_PREFERENCES_TOKEN, token).apply()

    fun getCurrentToken(): String = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN, null)!!

    fun getDeviceId(): String =
        sharedPreferences.getString(SHARED_PREFERENCES_DEVICE_TAG, null) ?: "0d"

    fun putRevisionId(revision: Int) =
        editor.putInt(SHARED_PREFERENCES_REVISION_TAG, revision).apply()

    fun getRevisionId(): Int = sharedPreferences.getInt(SHARED_PREFERENCES_REVISION_TAG, 1)

    fun getTheme(): Int = sharedPreferences.getInt(SHARED_PREFERENCES_THEME_TAG, 2)

    fun setTheme(theme: Int) =
        sharedPreferences.edit().putInt(SHARED_PREFERENCES_THEME_TAG, theme).apply()

    fun getNotificationPermission(): Boolean =
        sharedPreferences.getBoolean(SHOW_NOTIFICATIONS_KEY, true)

    fun setNotificationPermission(permission: Boolean) =
        editor.putBoolean(SHOW_NOTIFICATIONS_KEY, permission).apply()


    fun addNotification(id: String) =
        editor.putString("notifications", getNotificationsId() + " $id").apply()

    fun getNotificationsId(): String =
        sharedPreferences.getString("notifications", "").toString()

    fun removeNotification(id: String) {
        val arr = ArrayList(getNotificationsId().split(" "))
        if (arr.contains(id)) arr.remove(id)
        editor.putString("notifications", arr.fold("") { previous, next -> "$previous $next" })
            .apply()
    }

    companion object {
        const val SHARED_PREFERENCES_NAME = "settings"
        const val SHARED_PREFERENCES_REVISION_TAG = "current_revision"
        const val SHARED_PREFERENCES_DEVICE_TAG = "current_device_id"
        const val SHARED_PREFERENCES_TOKEN = "current_token"
        const val SHARED_PREFERENCES_THEME_TAG = "theme"
    }
}