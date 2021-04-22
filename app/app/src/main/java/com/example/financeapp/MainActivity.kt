package com.example.financeapp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.financeapp.model.LoginRequest
import com.example.financeapp.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferences = applicationContext.getSharedPreferences(getString(R.string.shared_pref_name), Context.MODE_PRIVATE)

        val token = preferences.getString(getString(R.string.shared_pref_token), null)

        if(token != null) {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        findViewById<Button>(R.id.login_button).setOnClickListener {
            val email = findViewById<EditText>(R.id.login_email).text.toString()
            val password = findViewById<EditText>(R.id.login_password).text.toString()

            if(email != "" && password != "") {
                this.loginUser(email, password)
            }
        }

        findViewById<Button>(R.id.login_register_button).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {
        val progress = ProgressDialog(this)
        progress.setTitle("Loading...")
        progress.show()

        val call = WebClient().UserService().loginUser(LoginRequest(email, password))
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                progress.dismiss()
                response.body()?.let {
                    val login: LoginResponse = it
                    with (preferences.edit()) {
                        putString(getString(R.string.shared_pref_username), login.user.name)
                        putString(getString(R.string.shared_pref_token), login.token)
                        putInt(getString(R.string.shared_pref_userid), login.user.id)
                        commit()
                    }
                    startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable?) {
                Log.e("api:error", t?.message as String)
                progress.dismiss()
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Error!")
                    .setMessage("Error when do login.")
                    .show()
            }
        })
    }
}