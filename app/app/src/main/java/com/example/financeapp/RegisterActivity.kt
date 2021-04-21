package com.example.financeapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.financeapp.model.Login
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
                val status = this.registerUser(name, email, password)
                if(status == 1) {
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        }

    }

    private fun registerUser(name: String, email: String, password: String): Int{
        var status = 1

        AlertDialog.Builder(this)
            .setTitle("Registro")
            .setMessage("Criando seu usuário, por favor aguarde!")
            .show()

        val call = WebClient().UserService().createUser(name, email, password)
        call.enqueue(object : Callback<Login?> {
            override fun onResponse(call: Call<Login?>?, response: Response<Login?>) {
                response.body()?.let {
                    val login: Login = it
                    with (preferences.edit()) {
                        putString(getString(R.string.shared_pref_name), login.user.name)
                        putString(getString(R.string.shared_pref_token), login.token)
                        putInt(getString(R.string.shared_pref_userid), login.user.id)
                        commit()
                    }
                    status = 1
                }

                AlertDialog.Builder(this@RegisterActivity)
                    .setTitle("Sucesso!")
                    .setMessage("Usuário criado com sucesso.")
                    .show()
            }
            override fun onFailure(call: Call<Login?>?, t: Throwable?) {
                Log.e("onFailure error", t?.message as String)
                status = 0

                AlertDialog.Builder(this@RegisterActivity)
                    .setTitle("Erro!")
                    .setMessage("Erro ao criar usuário.")
                    .show()
            }
        })
        return status
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}