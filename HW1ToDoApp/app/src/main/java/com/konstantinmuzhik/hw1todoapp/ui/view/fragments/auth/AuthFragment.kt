package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.konstantinmuzhik.hw1todoapp.App
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.ioc.auth.AuthFragmentComponent
import com.konstantinmuzhik.hw1todoapp.ioc.auth.AuthFragmentViewComponent
import com.konstantinmuzhik.hw1todoapp.ioc.auth.AuthFragmentYandexComponent
import com.konstantinmuzhik.hw1todoapp.ui.ViewModelFactory
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import javax.inject.Inject

class AuthFragment : Fragment() {

    private lateinit var fragmentComponent: AuthFragmentComponent
    private var fragmentViewComponent: AuthFragmentViewComponent? = null
    private var fragmentYandexComponent: AuthFragmentYandexComponent? = null

    private lateinit var sdk: YandexAuthSdk

    @Inject
    lateinit var sharedPreferences: SharedPreferencesAppSettings

    private val viewModel: ToDoItemViewModel by viewModels{
        ViewModelFactory{
            (requireActivity().application as App).getAppComponent().toDoItemViewModel()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity().application as App).getAppComponent().inject(this)

        fragmentComponent = AuthFragmentComponent(fragment = this)
        sdk = YandexAuthSdk(requireContext(), YandexAuthOptions(requireContext(), true))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_auth, container, false)

        fragmentViewComponent = AuthFragmentViewComponent(
            view,
            navController = findNavController(),
            sharedPreferences,
            sdk,
            fragmentComponent,
            viewModel
        ).apply {
            authViewController.setUpViews()
        }

        return view
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        fragmentYandexComponent = AuthFragmentYandexComponent(
            context,
            requestCode,
            resultCode,
            data,
            navController = findNavController(),
            sharedPreferences,
            sdk,
            viewModel
        ).apply {
            authYandexController.setUpYandexResult()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
