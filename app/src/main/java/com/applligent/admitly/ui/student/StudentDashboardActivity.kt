package com.applligent.admitly.ui.student

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.applligent.admitly.R
import com.applligent.admitly.databinding.ActivityStudentDashboardBinding

class StudentDashboardActivity : AppCompatActivity() {
    lateinit var binding: ActivityStudentDashboardBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController= Navigation.findNavController(this,R.id.navigation_Dashboard)
        setupWithNavController(binding.bottomNavigationView,navController)
        binding.bottomNavigationView.itemIconTintList = null
    }
}