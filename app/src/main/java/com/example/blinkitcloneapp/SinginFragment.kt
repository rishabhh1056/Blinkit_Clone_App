package com.example.blinkitcloneapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.blinkitcloneapp.databinding.FragmentSinginBinding


class SinginFragment : Fragment() {

    private lateinit var binding: FragmentSinginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSinginBinding.inflate(inflater, container, false)


        phNumberTextWatcher()

        binding.continueBtn.setOnClickListener {
            if (binding.phNumber.text.isEmpty()) {
                Toast.makeText(
                    this@SinginFragment.requireContext(),
                    "enter your number",
                    Toast.LENGTH_SHORT
                ).show()
            } else  {
                val bundel = Bundle()
                bundel.putString("number", binding.phNumber.text.toString())
                findNavController().navigate(R.id.action_singinFragment_to_OTPFragment, bundel)
            }
        }
        return binding.root
    }

    private fun phNumberTextWatcher() {
        binding.phNumber.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 10) {
                    binding.continueBtn.isEnabled = true
                } else {
                    binding.continueBtn.isEnabled = false
                }
            }

        })
    }


}