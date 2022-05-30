package com.applligent.admitly.ui.counselor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.applligent.admitly.R
import com.applligent.admitly.databinding.ActivityCounselorDashboardBinding

class CounselorDashboardActivity : AppCompatActivity() {
    lateinit var binding: ActivityCounselorDashboardBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCounselorDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController= Navigation.findNavController(this,R.id.navigation_Dashboard2)
        NavigationUI.setupWithNavController(binding.bottomNavigationView2, navController)
        binding.bottomNavigationView2.itemIconTintList = null
    }
}