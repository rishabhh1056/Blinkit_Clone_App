package com.example.blinkitcloneapp.viewModel

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import com.example.blinkitcloneapp.Utils
import com.example.blinkitcloneapp.View.MainActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

class AuthViewModel :  ViewModel()  {


    private val _verificationId = MutableStateFlow<String?>(null)

    private val _otpSent = MutableStateFlow<Boolean>(false)
    val otpSent = _otpSent

    private val _isSignedIn = MutableStateFlow<Boolean>(false)
    val isSignedIn = _isSignedIn


    fun sendOTP(userNumber: String, activity: Activity)
    {
       val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(e: FirebaseException) {

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {

                _verificationId.value = verificationId
                otpSent.value = true

            }
        }

        val options = PhoneAuthOptions.newBuilder(Utils.getFirebaseInstance())
            .setPhoneNumber("+91$userNumber") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signInWithPhoneAuthCredential(otp: String, userNumber: String) {
        val credential = PhoneAuthProvider.getCredential(_verificationId.value.toString(), otp)

        Utils.getFirebaseInstance()
            .signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    _isSignedIn.value = true
                    Log.e("TAG", "....success.....", )
                } else {

                    Log.e("TAG", "......failed otp......  ", )
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Log.e("TAG", "signInWithPhoneAuthCredential:  failed", )
                    }
                    // Update UI
                }
            }
    }

}