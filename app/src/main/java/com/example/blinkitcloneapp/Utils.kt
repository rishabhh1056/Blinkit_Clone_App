package com.example.blinkitcloneapp

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.blinkitcloneapp.databinding.ProgressDialogBinding
import com.google.firebase.auth.FirebaseAuth

object Utils {

    private var dialog: AlertDialog? = null

    fun showDialog(context: Context, massage: String) {

        val progress = ProgressDialogBinding.inflate(LayoutInflater.from(context))
        progress.tvMassage.text = massage
        dialog = AlertDialog.Builder(context).setView(progress.root).setCancelable(false).create()
        dialog?.show()
    }


    fun hideDialog() {
        dialog?.dismiss() ?: dialog?.cancel()
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private var getFirebaseInstance: FirebaseAuth? = null

    fun getFirebaseInstance(): FirebaseAuth {
        if (getFirebaseInstance == null) {
            getFirebaseInstance = FirebaseAuth.getInstance()
        }
        return getFirebaseInstance!!
    }
}
