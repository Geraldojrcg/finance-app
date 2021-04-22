package com.example.financeapp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.financeapp.model.UserRequest
import com.example.financeapp.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "Register"

        preferences = applicationContext.getSharedPreferences(R.string.shared_pref_name.toString(), Context.MODE_PRIVATE)

        findViewById<Button>(R.id.register_button).setOnClickListener {
            val name = findViewById<EditText>(R.id.register_name).text.toString()
            val email = findViewById<EditText>(R.id.register_name).text.toString()
            val password = findViewById<EditText>(R.id.register_name).text.toString()

            if(name != "" && email != "" && password != "") {
                this.registerUser(name, email, password)
            }
        }

    }

    private fun registerUser(name: String, email: String, password: String) {
        val progress = ProgressDialog(this)
        progress.setTitle("Loading...")
        progress.show()

        val call = WebClient().UserService().createUser(UserRequest(name, email, password))
        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                progress.dismiss()
                response.body()?.let {
                    val login: UserResponse = it
                    with (preferences.edit()) {
                        putString(getString(R.string.shared_pref_username), login.user.name)
                        putString(getString(R.string.shared_pref_token), login.token)
                        putInt(getString(R.string.shared_pref_userid), login.user.id)
                        commit()
                    }
                    startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable?) {
                Log.e("api:error", t?.message as String)
                progress.dismiss()
                AlertDialog.Builder(this@RegisterActivity)
                    .setTitle("Error!")
                    .setMessage("Error when create user.")
                    .show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}