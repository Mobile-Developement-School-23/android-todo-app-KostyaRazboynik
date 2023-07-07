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
import com.konstantinmuzhik.hw1todoapp.utils.Constants
import com.konstantinmuzhik.hw1todoapp.data.PeriodWorkManager
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applicationContext.appComponent.inject(this)
        setContentView(R.layout.activity_main)

        val navController = getRootNavController()
        prepareRootNavController(isSignedIn(), navController)
    }

    override fun onDestroy() {
        super.onDestroy()
        periodicUpdate()
    }

    private fun periodicUpdate() =
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            Constants.WORK_MANAGER_TAG,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            PeriodicWorkRequest.Builder(PeriodWorkManager::class.java, 8, TimeUnit.HOURS)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .addTag(Constants.WORK_MANAGER_TAG)
                .build()
        )

    private fun getRootNavController(): NavController {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        return navHost.navController
    }

    private fun prepareRootNavController(isSignedIn: Boolean, navController: NavController) {
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
        sharedPreferencesAppSettings.getCurrentToken() != Constants.SHARED_PREFERENCES_NO_TOKEN
}