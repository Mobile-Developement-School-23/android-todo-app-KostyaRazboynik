package com.konstantinmuzhik.hw1todoapp.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.data.viewmodel.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentAuthBinding
import com.konstantinmuzhik.hw1todoapp.factory
import com.konstantinmuzhik.hw1todoapp.utils.Constants.TOKEN_API
import com.konstantinmuzhik.hw1todoapp.utils.localeLazy
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk

class AuthFragment : Fragment() {

    private lateinit var binding: FragmentAuthBinding

    private val REQUEST_LOGIN_SDK = 1

    private lateinit var sdk: YandexAuthSdk

    private val sharedPreferences: SharedPreferencesAppSettings by localeLazy()

    private val viewModel: ToDoItemViewModel by activityViewModels { factory() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentAuthBinding.inflate(layoutInflater)
        sdk = YandexAuthSdk(requireContext(), YandexAuthOptions(requireContext(), true))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginOptionsBuilder = YandexAuthLoginOptions.Builder()
        val intent = sdk.createLoginIntent(loginOptionsBuilder.build())


        binding.yandexAuthButton.setOnClickListener {
            startActivityForResult(intent, REQUEST_LOGIN_SDK)
        }

        binding.logInButton.setOnClickListener {
            if (sharedPreferences.getCurrentToken() != TOKEN_API) {
                viewModel.deleteAllToDoItems()
                sharedPreferences.setCurrentToken("Bearer $TOKEN_API")
                sharedPreferences.putRevisionId(0)
            }
            findNavController().navigate(R.id.action_fragmentAuth_to_listFragment)
        }

    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == REQUEST_LOGIN_SDK) {
            try {
                val yandexAuthToken = sdk.extractToken(resultCode, data)
                if (yandexAuthToken != null) {
                    val curToken = yandexAuthToken.value
                    if (curToken != sharedPreferences.getCurrentToken()) {
                        viewModel.deleteAllToDoItems()
                        sharedPreferences.setCurrentToken("OAuth ${yandexAuthToken.value}")
                        sharedPreferences.putRevisionId(0)
                    }
                    sharedPreferences.setCurrentToken("OAuth ${yandexAuthToken.value}")
                    findNavController().navigate(R.id.action_fragmentAuth_to_listFragment)
                }
            } catch (e: YandexAuthException) {
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
