package com.konstantinmuzhik.hw1todoapp.ui.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.appComponent
import com.konstantinmuzhik.hw1todoapp.data.datasource.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.data.PeriodWorkManager
import com.konstantinmuzhik.hw1todoapp.utils.Constants.REPEAT_INTERVAL
import com.konstantinmuzhik.hw1todoapp.utils.Constants.SHARED_PREFERENCES_NO_TOKEN
import com.konstantinmuzhik.hw1todoapp.utils.Constants.WORK_MANAGER_TAG
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Main Activity
 *
 * @author Kovalev Konstantin
 *
 */
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferencesAppSettings: SharedPreferencesAppSettings

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        applicationContext.appComponent.inject(this)

        navController = getRootNavController()

        prepareRootNavController(isSignedIn())
    }

    override fun onDestroy() {
        super.onDestroy()
        periodicUpdate()
    }

    private fun periodicUpdate() =
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            WORK_MANAGER_TAG,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            PeriodicWorkRequest.Builder(
                PeriodWorkManager::class.java,
                REPEAT_INTERVAL,
                TimeUnit.HOURS
            )
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .addTag(WORK_MANAGER_TAG)
                .build()
        )

    private fun getRootNavController(): NavController =
        (supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment).navController

    private fun prepareRootNavController(isSignedIn: Boolean) {
        val graph = navController.navInflater.inflate(getMainNavigationGraphId())
        graph.setStartDestination(
            if (isSignedIn) getMainDestination()
            else getSignInDestination()
        )
        navController.graph = graph
    }

    private fun getMainNavigationGraphId(): Int = R.navigation.my_nav

    private fun getMainDestination(): Int = R.id.listFragment

    private fun getSignInDestination(): Int = R.id.fragmentAuth

    private fun isSignedIn(): Boolean =
        sharedPreferencesAppSettings.getCurrentToken() != SHARED_PREFERENCES_NO_TOKEN
}