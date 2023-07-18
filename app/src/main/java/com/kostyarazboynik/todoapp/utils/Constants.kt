package com.kostyarazboynik.todoapp.utils

/**
 * Constants
 *
 * @author Kovalev Konstantin
 *
 */
object Constants {

    // back end
    const val BASE_URL = "https://beta.mrdekk.ru/todobackend/"
    const val TOKEN_API = "innervate"
    const val RETROFIT_TIMEOUT : Long = 10

    // yandex api && auth
    const val YANDEX_BASE_URL = "https://login.yandex.ru"

    const val REQUEST_LOGIN_SDK_CODE = -1

    // work manager
    const val WORK_MANAGER_TAG = "update_data"
    const val REPEAT_INTERVAL: Long = 8

    // data base
    const val DATABASE_NAME = "new_database"

    // token
    const val SHARED_PREFERENCES_NO_TOKEN = "no_token"

    // notification
    const val GLOBAL_INTENT_STRING_EXTRA_NAME = "id"
    const val NOTIFICATIONS_PERMISSION_KEY = "notification_permission"

    // theme
    const val THEME_NIGHT_NO = 0
    const val THEME_NIGHT_YES = 1
    const val THEME_FOLLOW_SYSTEM = 2


}