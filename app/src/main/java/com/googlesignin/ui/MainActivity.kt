/*
 * *
 *  * Created by Nikhil-z on 26/07/22, 2:36 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 26/07/22, 2:30 PM
 *
 */

package com.googlesignin.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.bumptech.glide.Glide
import com.googlesignin.AppConstants.REQUEST_ID_TOKEN
import com.googlesignin.databinding.ActivityMainBinding
import com.googlesignin.repository.network.Status
import com.googlesignin.repository.network.local.SecuredStorageAccess
import com.googlesignin.repository.viewmodel.MainViewModel
import com.googlesignin.utils.showSnack
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.api.services.admob.v1.AdMobScopes
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var mSignInClient: GoogleSignInClient
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mFirebaseAuth: FirebaseAuth

    private val viewModel = MainViewModel()
    private lateinit var securedStorageAccess: SecuredStorageAccess

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        securedStorageAccess = SecuredStorageAccess(this)


        // Initialize Firebase Auth and check if the user is signed in
        mFirebaseAuth = FirebaseAuth.getInstance()
        if (mFirebaseAuth.currentUser == null) {
            // Not signed in, launch the Sign In activity
            gotoLogin()
            return
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(REQUEST_ID_TOKEN)
            .requestServerAuthCode(REQUEST_ID_TOKEN)
            .requestEmail()
            .build()

        mSignInClient = GoogleSignIn.getClient(this, gso)


        setProfile()

        mBinding.signOut.setOnClickListener { signOut() }


        authInfoReceiver()


    }

    private fun authInfoReceiver() {
        viewModel.authentication.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showSnack(it.message)
                }
                Status.SUCCESS -> {
                    securedStorageAccess.setAuthInfo(it.data)
                    showSnack(it.message)
                }
                Status.ERROR -> {
                    showSnack(it.message)
                }
                else -> {

                }
            }
        }
    }


    private fun signIn() {
        val signInIntent = mSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private fun silentSignIn() {
        mSignInClient.silentSignIn().addOnCompleteListener(this) {
            try {
                val account = it.getResult(ApiException::class.java)
                viewModel.getAuthCode(account.serverAuthCode)
                showSnack("Login was a success")
            } catch (e: ApiException) {
                Timber.d("Google sign in failed", e)
                Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show()
                signOut()
            }
        }

    }


    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        val account = task.getResult(ApiException::class.java)
                        viewModel.getAuthCode(account.serverAuthCode)
                    } catch (e: ApiException) {
                        Timber.d("Google sign in failed", e)
                        Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Timber.d("registerForActivityResult intent returned NULL")
                }

            }
        }


    private fun setProfile() {
        Glide.with(this).load(userPhotoUrl).into(mBinding.profileImage)
        mBinding.profileName.text = userName

    }

    private fun signOut() {
        mFirebaseAuth.signOut()
        mSignInClient.signOut()
        securedStorageAccess.info().clear()
        gotoLogin()
    }

    private val userPhotoUrl: String?
        get() {
            val user = mFirebaseAuth.currentUser
            return if (user != null && user.photoUrl != null) {
                user.photoUrl.toString()
            } else null
        }
    private val userName: String?
        get() {
            val user = mFirebaseAuth.currentUser
            return if (user != null) {
                user.displayName
            } else ANONYMOUS
        }

    companion object {
        const val ANONYMOUS = "anonymous"
    }


    private fun gotoLogin() {
        val bundle = ActivityOptionsCompat.makeCustomAnimation(
            this,
            android.R.anim.fade_in, android.R.anim.fade_out
        ).toBundle()
        startActivity(Intent(this, SignInActivity::class.java), bundle)
        finish()
    }
}