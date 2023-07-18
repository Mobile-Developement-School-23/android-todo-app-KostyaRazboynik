package com.kostyarazboynik.todoapp.domain.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kostyarazboynik.todoapp.App
import com.kostyarazboynik.todoapp.domain.repository.Repository
import com.kostyarazboynik.todoapp.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import javax.inject.Inject

class NotificationPostponeReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var coroutineScope: CoroutineScope

    companion object {
        const val ONE_DAY_IN_MILLIS = 86400000
        const val INTENT_STRING_EXTRA_NAME = Constants.GLOBAL_INTENT_STRING_EXTRA_NAME
    }

    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as App).appComponent.inject(this)

        try {
            val id: String = intent.getStringExtra(INTENT_STRING_EXTRA_NAME)!!
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.cancel(id.hashCode())

            coroutineScope.launch(Dispatchers.IO) {
                val item = repository.getToDoItemById(id)
                if (item.deadline != null)
                    repository.updateToDoItem(item.copy(deadline = Date(item.deadline!!.time + ONE_DAY_IN_MILLIS)))
            }
        } catch (_: Exception) {
        }
    }
}