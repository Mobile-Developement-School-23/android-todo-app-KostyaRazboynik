package com.kostyarazboynik.todoapp.ui.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.kostyarazboynik.todoapp.R
import com.kostyarazboynik.todoapp.appComponent
import com.kostyarazboynik.todoapp.data.datasource.SharedPreferencesAppSettings
import com.kostyarazboynik.todoapp.utils.Constants.SHARED_PREFERENCES_NO_TOKEN
import com.kostyarazboynik.todoapp.utils.Constants.WORK_MANAGER_TAG
import javax.inject.Inject

/**
 * Main Activity
 *
 * @author Kovalev Konstantin
 *
 */
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferencesAppSettings

    @Inject
    lateinit var myWorkRequest: PeriodicWorkRequest

    private lateinit var navController: NavController

    companion object {
        const val NAV_STATE = "navControllerState"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(NAV_STATE, navController.saveState())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        applicationContext.appComponent.inject(this)

        navController = getRootNavController()

        if (savedInstanceState != null)
            navController.restoreState(savedInstanceState.getBundle(NAV_STATE))
        else prepareRootNavController(isSignedIn())
    }

    override fun onDestroy() {
        super.onDestroy()
        periodicUpdate()
    }

    private fun periodicUpdate() =
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            WORK_MANAGER_TAG,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            myWorkRequest
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
        sharedPreferences.getCurrentToken() != SHARED_PREFERENCES_NO_TOKEN
}