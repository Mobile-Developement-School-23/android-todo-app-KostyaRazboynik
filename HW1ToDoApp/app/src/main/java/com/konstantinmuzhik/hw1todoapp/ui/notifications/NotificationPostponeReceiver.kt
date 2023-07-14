package com.konstantinmuzhik.hw1todoapp.ui.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
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

        val gson = Gson()
        val item = gson.fromJson(intent.getStringExtra("item"), ToDoItem::class.java)
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(item.id.hashCode())
        try {
            coroutineScope.launch(Dispatchers.IO) {
                if(item.deadline != null){
                    repository.updateToDoItem(item.copy(deadline = Date(item.deadline!!.time+86400000)))
                }
            }
        }catch (err:Exception){
            Log.d("1", err.message.toString())
        }
    }
}