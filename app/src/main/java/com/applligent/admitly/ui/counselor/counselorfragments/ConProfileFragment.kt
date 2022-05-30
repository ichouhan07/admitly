package com.applligent.admitly.ui.counselor.counselorfragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.applligent.admitly.R
import com.applligent.admitly.databinding.FragmentConProfileBinding
import com.applligent.admitly.ui.comman.SignInActivity
import com.applligent.admitly.ui.counselor.ConPaymentMethodActivity
import com.applligent.admitly.utils.preferences.setLoginStatus

class ConProfileFragment : Fragment() {
    lateinit var binding: FragmentConProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentConProfileBinding.inflate(inflater,container,false)
        setListener()
        return binding.root
    }

    private fun setListener() {
        binding.counselorPaymentMethod.setOnClickListener {
            startActivity(Intent(requireActivity(),ConPaymentMethodActivity::class.java))
        }

        binding.logoutBtn.setOnClickListener {
            requireActivity().setLoginStatus(false)
            startActivity(Intent(requireActivity(), SignInActivity::class.java))
            requireActivity().finish()
        }
    }

}