package com.konstantinmuzhik.hw1todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //findViewById<FragmentContainerView>(R.id.fragment_container_view).findNavController().navigate(R.id.action_listFragment_to_addFragment)
    }
}