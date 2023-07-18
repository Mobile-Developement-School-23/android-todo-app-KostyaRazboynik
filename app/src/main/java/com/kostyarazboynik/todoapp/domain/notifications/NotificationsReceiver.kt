package com.kostyarazboynik.todoapp.domain.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.kostyarazboynik.todoapp.App
import com.kostyarazboynik.todoapp.R
import com.kostyarazboynik.todoapp.data.models.ToDoItem
import com.kostyarazboynik.todoapp.domain.repository.Repository
import com.kostyarazboynik.todoapp.utils.Constants.GLOBAL_INTENT_STRING_EXTRA_NAME
import com.kostyarazboynik.todoapp.utils.Mappers.getString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class NotificationsReceiver : BroadcastReceiver() {

    @Inject
    lateinit var scheduler: NotificationsSchedulerImpl

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var coroutineScope: CoroutineScope

    private companion object {
        const val CHANNEL_ID = "deadlines"
        const val CHANNEL_NAME = "Deadline notification"
        const val INTENT_STRING_EXTRA_NAME = GLOBAL_INTENT_STRING_EXTRA_NAME
    }

    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as App).appComponent.inject(this)
        try {
            val id: String = intent.getStringExtra(INTENT_STRING_EXTRA_NAME)!!

            coroutineScope.launch(Dispatchers.IO) {
                val toDoItem = repository.getToDoItemById(id)
                scheduler.cancel(toDoItem.id)
                createManager(context).notify(
                    toDoItem.id.hashCode(),
                    buildNotification(context, toDoItem)
                )
            }
        } catch (_: Exception) {
        }
    }

    private fun buildNotification(context: Context, toDoItem: ToDoItem): Notification =
        NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_yandex_logo)
            .setContentTitle(
                context.getString(
                    R.string.hour_notification,
                    toDoItem.importance.getString(context)
                )
            )
            .setContentText(toDoItem.title)
            .setAutoCancel(true)
            .setContentIntent(deepLinkIntent(context, toDoItem.id))
            .addAction(
                NotificationCompat.Action(
                    R.drawable.ic_timer, context.getString(R.string.postpone),
                    postponeIntent(context, toDoItem.id)
                )
            )
            .build()

    private fun deepLinkIntent(context: Context, id: String): PendingIntent =
        NavDeepLinkBuilder(context)
            .setGraph(R.navigation.my_nav)
            .setDestination(R.id.updateFragment, bundleOf(INTENT_STRING_EXTRA_NAME to id))
            .createPendingIntent()

    private fun postponeIntent(context: Context, id: String): PendingIntent =
        PendingIntent.getBroadcast(
            context, id.hashCode(),
            Intent(context, NotificationPostponeReceiver::class.java).apply {
                putExtra(INTENT_STRING_EXTRA_NAME, id)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    private fun createManager(context: Context): NotificationManager {
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
        )
        return manager
    }
}