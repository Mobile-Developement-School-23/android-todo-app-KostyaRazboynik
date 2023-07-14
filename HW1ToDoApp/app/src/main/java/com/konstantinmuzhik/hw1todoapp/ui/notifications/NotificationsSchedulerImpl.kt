package com.konstantinmuzhik.hw1todoapp.ui.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.konstantinmuzhik.hw1todoapp.data.datasource.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.domain.sheduler.NotificationsScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class NotificationsSchedulerImpl @Inject constructor(
    private val context: Context,
    private val sharedPreferencesHelper: SharedPreferencesAppSettings
) : NotificationsScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: ToDoItem) {
        if (item.deadline != null
            && item.deadline!!.time >= System.currentTimeMillis()+3600000
            && !item.done
            && sharedPreferencesHelper.getNotificationPermission()) {
            val intent = Intent(context, NotificationsReceiver::class.java).apply {
                putExtra("item", item.toString())
            }
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                //item.deadline!!.time-3600000,
                System.currentTimeMillis()+10000,
                PendingIntent.getBroadcast(
                    context,
                    item.id.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            sharedPreferencesHelper.addNotification(item.id)

        }else{
            cancel(item.id)
        }
    }

    override fun cancel(id:String) {
        try {
            alarmManager.cancel(
                PendingIntent.getBroadcast(
                    context,
                    id.hashCode(),
                    Intent(context, NotificationsReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            sharedPreferencesHelper.removeNotification(id)
        }catch (err:Exception){
            Log.d("1", err.message.toString())
        }
    }


    override fun cancelAll() {
        Log.d("1", sharedPreferencesHelper.getNotificationsId())
        val notifications = sharedPreferencesHelper.getNotificationsId().split(" ")

        for(notification in notifications){
            cancel(notification)
        }
    }
}