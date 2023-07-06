package com.konstantinmuzhik.hw1todoapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.konstantinmuzhik.hw1todoapp.App
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.utils.Constants
import com.konstantinmuzhik.hw1todoapp.utils.PeriodWorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferencesAppSettings: SharedPreferencesAppSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (this.application as App).getAppComponent().inject(this)

        val navController = getRootNavController()
        prepareRootNavController(true, navController)
    }

    override fun onDestroy() {
        super.onDestroy()
        periodicUpdate()
    }

    private fun periodicUpdate() {
        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


        val myWorkRequest = PeriodicWorkRequest.Builder(
            PeriodWorkManager::class.java,
            8,
            TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .addTag(Constants.WORK_MANAGER_TAG)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            Constants.WORK_MANAGER_TAG,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            myWorkRequest
        )
    }

    private fun getRootNavController(): NavController =
        (supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment)
            .navController

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
        sharedPreferencesAppSettings.getCurrentToken() != Constants.NO_TOKEN
}