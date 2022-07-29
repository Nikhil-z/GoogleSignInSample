/**
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlesignin.ui


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.googlesignin.AppConstants
import com.googlesignin.R
import com.googlesignin.databinding.ActivitySignInBinding
import com.googlesignin.utils.showSnack
import timber.log.Timber


class SignInActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySignInBinding
    private lateinit var mSignInClient: GoogleSignInClient
    private lateinit var mFirebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_sign_in)
        mBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.signInButton.setSize(SignInButton.SIZE_WIDE)
        mFirebaseAuth = FirebaseAuth.getInstance()


        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(R.string.default_web_client_id))
                .requestIdToken(AppConstants.REQUEST_ID_TOKEN)
                .requestServerAuthCode(AppConstants.REQUEST_ID_TOKEN)
                .requestEmail()
                .build()

        mSignInClient = GoogleSignIn.getClient(this, gso)




        mBinding.signInButton.setOnClickListener { signIn() }


    }

    private fun gotoHome() {
        val bundle = ActivityOptionsCompat.makeCustomAnimation(
            this@SignInActivity,
            android.R.anim.fade_in, android.R.anim.fade_out
        ).toBundle()
        startActivity(Intent(this@SignInActivity, MainActivity::class.java), bundle)
        finish()
    }

    private fun signIn() {
        val signInIntent = mSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }


    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        val account = task.getResult(ApiException::class.java)
                        firebaseAuthWithGoogle(account)
                    } catch (e: ApiException) {
                        Log.d(TAG, "Google sign in failed", e)
                        Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Timber.d("registerForActivityResult intent returned NULL")
                }

            }
        }


    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)


        mFirebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener(this) { authResult: AuthResult? ->
                showSnack("Login in success")
                gotoHome()
            }
            .addOnFailureListener(this) { e: Exception? ->
                Toast.makeText(
                    this@SignInActivity, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    companion object {
        private const val TAG = "SignInActivity"
        private const val RC_SIGN_IN = 1234
    }
}