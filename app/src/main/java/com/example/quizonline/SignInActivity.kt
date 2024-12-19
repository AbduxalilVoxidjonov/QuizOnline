package com.example.quizonline

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizonline.databinding.ActivityAuthBinding
import com.example.quizonline.network.ApiClient
import com.example.quizonline.network.ApiServise
import com.example.quizonline.network.model.Token
import com.example.quizonline.network.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignInActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAuthBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        if (getSharedPreferences("app_prefs", MODE_PRIVATE).getString(
                "access_token",
                null
            ) != null
        ) {
            val intent = Intent(this, MyActivity::class.java)
            startActivity(intent)
        } else {
            binding.apply {
                loginButton.setOnClickListener {
                    binding.loginProgress.visibility = android.view.View.VISIBLE
                    if (loginUsername.text.toString().isNotEmpty() && loginPassword.text.toString()
                            .isNotEmpty()
                    ) {

                        getUsers(loginUsername.text.toString(), loginPassword.text.toString())
                        binding.loginProgress.visibility = android.view.View.GONE
                    } else {
                        binding.loginProgress.visibility = android.view.View.GONE
                        Toast.makeText(
                            this@SignInActivity,
                            "Please fill all the fields",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }
    }

    fun getUsers(user: String, password: String) {
        binding.loginUsername.text.clear()
        binding.loginPassword.text.clear()
        binding.loginProgress.visibility = android.view.View.VISIBLE

        val loginRequest = User(user, password)

        val loginService = ApiClient.getRetrofit().create(ApiServise::class.java)
        val call = loginService.loginUser(loginRequest)

        call.enqueue(object : Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                if (response.isSuccessful) {
                    // Handle success
                    val loginResponse = response.body()
                    loginResponse?.let {
                        val accessToken = it.accessToken
                        // Store the access token
                        saveAccessToken(accessToken)
                        binding.loginProgress.visibility = android.view.View.GONE

                        val intent = Intent(this@SignInActivity, MyActivity::class.java)
                        intent.putExtra("access_token", accessToken)
                        startActivity(intent)

                        Log.d("Login", "Access Token: $accessToken")
                    }
                } else {
                    // Handle failure
                    Log.e("Login", "Login failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
                // Handle network failure
                binding.loginProgress.visibility = android.view.View.GONE
                Log.e("Login", "Error: ${t.message}")
            }
        })
    }

    private fun saveAccessToken(accessToken: String) {
        // saved date in shared preferences
        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("access_token", accessToken)
        editor.putString("last_login", System.currentTimeMillis().toString())
        editor.apply()
    }
}