package com.example.blinkitcloneapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.blinkitcloneapp.databinding.FragmentOTPBinding
import com.example.blinkitcloneapp.databinding.FragmentSinginBinding
import com.example.blinkitcloneapp.viewModel.AuthViewModel
import kotlinx.coroutines.launch


class OTPFragment : Fragment() {
    private lateinit var binding: FragmentOTPBinding
    private lateinit var userNumber :String
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOTPBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment

        getUserNummber()
        sentotp()
        LoginBtnClick()
        castumazingEntringOTP()

        binding.backbtn.setOnClickListener {
            findNavController().navigate(R.id.action_OTPFragment_to_singinFragment)
        }

        return binding.root
    }

    private fun sentotp() {
        Utils.showDialog(requireContext(), "Sending OTP")

        viewModel.apply {
            sendOTP(userNumber, requireActivity())

            lifecycleScope.launch {
                otpSent.collect {
                    if (it) {
                        Utils.hideDialog()
                        Utils.showToast(requireContext(), "OTP Sent")
//                        findNavController().navigate(R.id.action_OTPFragment_to_enterOTPFragment)
                    }
                }
            }
        }
    }

    private fun LoginBtnClick() {
        binding.otpVerifyBtn.setOnClickListener {
             Utils.showDialog(requireContext(), "Singin you...")
            val editText = arrayOf(binding.otp1,binding.otp2,binding.otp3,binding.otp4,binding.otp5,binding.otp6)
            val otp = editText.joinToString(""){it.text.toString()}

            if (otp.length < 6 || otp.isEmpty())
            {
                Utils.hideDialog()
                Utils.showToast(requireContext(), "Enter Valid OTP")
            }
            else{
                 editText.forEach {it.text.clear() ; it.clearFocus()}
                verifyOtp(otp)
              }
        }
    }

    private fun verifyOtp(otp: String) {
     viewModel.signInWithPhoneAuthCredential(otp, userNumber)

        lifecycleScope.launch {
            viewModel.isSignedIn.collect{
                if (it) {
                    Utils.hideDialog()
                    Utils.showToast(requireContext(), "Login Success")
                   // findNavController().navigate(R.id.action_OTPFragment_to_enterOTPFragment)
                }
            }
        }
    }

    private fun castumazingEntringOTP() {
        val editText = arrayOf(binding.otp1,binding.otp2,binding.otp3,binding.otp4,binding.otp5,binding.otp6)

        for (i in editText.indices)
        {
            editText[i].addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1)
                    {
                        if (i < editText.size-1)
                        {
                            editText[i+1].requestFocus()
                        }
                        else if (s.length == 0)
                        {
                            if (i > 0){
                                editText[i-1].requestFocus()
                            }
                        }
                    }
                    else if(s?.length == 0)
                    {
                        if (i > 0)
                        {
                            editText[i-1].requestFocus()
                        }
                    }
                }

            })
        }
    }

    fun getUserNummber()
    {
        val bundle = arguments?.getString("number").toString()

        userNumber= bundle
        binding.number.text = userNumber
    }

}


