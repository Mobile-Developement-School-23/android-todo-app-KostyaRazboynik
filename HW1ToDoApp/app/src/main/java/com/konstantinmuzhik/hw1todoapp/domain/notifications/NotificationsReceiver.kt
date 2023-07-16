package com.konstantinmuzhik.hw1todoapp.domain.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.google.gson.Gson
import com.konstantinmuzhik.hw1todoapp.App
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class NotificationsReceiver : BroadcastReceiver() {

    @Inject
    lateinit var scheduler: NotificationsSchedulerImpl
    @Inject
    lateinit var coroutineScope: CoroutineScope
    private companion object {
        const val CHANNEL_ID = "deadlines"
        const val CHANNEL_NAME = "Deadline notification"
    }


    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as App).appComponent.inject(this)
        try {

            val gson = Gson()
            val item = gson.fromJson(intent.getStringExtra("item"), ToDoItem::class.java)
            coroutineScope.launch(Dispatchers.IO) {

                val manager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    manager.createNotificationChannel(
                        NotificationChannel(
                            CHANNEL_ID,
                            CHANNEL_NAME,
                            NotificationManager.IMPORTANCE_HIGH
                        )
                    )
                }

                val notification: Notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_yandex_logo)
                    .setContentTitle(context.getString(R.string.hour_notification))
                    .setContentText(item.title)
                    .setAutoCancel(true)
                    .setContentIntent(deepLinkIntent(context, item.id))
                    .addAction(
                        NotificationCompat.Action(
                            R.drawable.ic_delay, context.getString(R.string.postpone),
                            postponeIntent(context, item)
                        )
                    )
                    .build()
                scheduler.cancel(item.id)
                manager.notify(item.id.hashCode(), notification)
            }
        } catch (_: Exception) {
        }
    }

    private fun deepLinkIntent(context: Context, id: String): PendingIntent =
        NavDeepLinkBuilder(context)
            .setGraph(R.navigation.my_nav)
            .setDestination(R.id.updateFragment, bundleOf("id" to id))
            .createPendingIntent()

    private fun postponeIntent(context: Context, item: ToDoItem): PendingIntent =
        PendingIntent.getBroadcast(
            context, item.id.hashCode(),
            Intent(context, NotificationPostponeReceiver::class.java).apply {
                putExtra("item", item.toString())
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
}