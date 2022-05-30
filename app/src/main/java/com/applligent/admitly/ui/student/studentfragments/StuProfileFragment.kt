package com.applligent.admitly.ui.student.studentfragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.applligent.admitly.databinding.FragmentStuProfileBinding
import com.applligent.admitly.ui.comman.SignInActivity
import com.applligent.admitly.ui.student.StuPaymentMethodActivity
import com.applligent.admitly.utils.preferences.setLoginStatus

class StuProfileFragment : Fragment() {
    lateinit var binding: FragmentStuProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStuProfileBinding.inflate(inflater,container,false)
        setListener()
        return binding.root
    }
    private fun setListener() {
        binding.logoutBtn.setOnClickListener {
            requireActivity().setLoginStatus(false)
            startActivity(Intent(requireActivity(),SignInActivity::class.java))
            requireActivity().finish()
        }
        binding.studentPaymentMethod.setOnClickListener {
            val intent = Intent(requireActivity(),StuPaymentMethodActivity::class.java)
            startActivity(intent)
        }
    }
}