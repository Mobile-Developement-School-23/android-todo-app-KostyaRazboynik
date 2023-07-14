package com.konstantinmuzhik.hw1todoapp.domain.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.konstantinmuzhik.hw1todoapp.App
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.domain.repository.Repository
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
    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as App).appComponent.inject(this)

        val item = Gson().fromJson(intent.getStringExtra("item"), ToDoItem::class.java)
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(item.id.hashCode())
        try {
            coroutineScope.launch(Dispatchers.IO) {
                if (item.deadline != null)
                    repository.updateToDoItem(item.copy(deadline = Date(item.deadline!!.time + ONE_DAY_IN_MILLIS)))
            }
        } catch (_: Exception) {
        }
    }

    companion object {
        const val ONE_DAY_IN_MILLIS = 86400000

    }
}