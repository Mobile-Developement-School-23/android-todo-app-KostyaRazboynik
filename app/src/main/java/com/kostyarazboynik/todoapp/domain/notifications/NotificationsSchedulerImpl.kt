package com.kostyarazboynik.todoapp.domain.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.kostyarazboynik.todoapp.data.datasource.SharedPreferencesAppSettings
import com.kostyarazboynik.todoapp.data.models.ToDoItem
import com.kostyarazboynik.todoapp.domain.notifications.sheduler.NotificationsScheduler
import com.kostyarazboynik.todoapp.utils.Constants.GLOBAL_INTENT_STRING_EXTRA_NAME
import javax.inject.Inject


class NotificationsSchedulerImpl @Inject constructor(
    private val context: Context,
    private val sharedPreferences: SharedPreferencesAppSettings,
) : NotificationsScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    companion object {
        const val ONE_HOUR_IN_MILLIS = 3600000L
        const val ONE_DAY_IN_MILLIS = 86400000L
        const val INTENT_STRING_EXTRA_NAME = GLOBAL_INTENT_STRING_EXTRA_NAME
    }

    override fun schedule(item: ToDoItem) {
        if (item.deadline != null
            && item.deadline!!.time >= System.currentTimeMillis() + ONE_HOUR_IN_MILLIS
            && !item.done
            && sharedPreferences.getNotificationPermission()
        ) {
            val intent = Intent(context, NotificationsReceiver::class.java).apply {
                putExtra(INTENT_STRING_EXTRA_NAME, item.id)
            }
            setAlarmManager(intent, item)
            sharedPreferences.addNotification(item.id)
        } else cancel(item.id)
    }

    private fun setAlarmManager(intent: Intent, toDoItem: ToDoItem) =
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            getNotificationTime(toDoItem),
            PendingIntent.getBroadcast(
                context,
                toDoItem.id.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )

    private fun getNotificationTime(toDoItem: ToDoItem): Long {
        val deadline = toDoItem.deadline!!.time
        return when (sharedPreferences.getNotificationOption()) {
            NotificationMode.HOUR -> deadline - ONE_HOUR_IN_MILLIS
            NotificationMode.DAY -> deadline - ONE_DAY_IN_MILLIS
            NotificationMode.DEADLINE -> deadline
        }
    }

    override fun cancel(id: String) {
        try {
            alarmManager.cancel(
                PendingIntent.getBroadcast(
                    context,
                    id.hashCode(),
                    Intent(context, NotificationsReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            sharedPreferences.removeNotification(id)
        } catch (_: Exception) {
        }
    }

    override fun cancelAll() {
        val notifications = sharedPreferences.getNotificationsId().split(" ")
        for (notification in notifications) cancel(notification)
    }
}