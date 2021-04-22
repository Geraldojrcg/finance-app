package com.example.financeapp

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.example.financeapp.model.Article
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences;
    lateinit var adapter: ListArticleAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.title = "Finance News App"

        preferences = applicationContext.getSharedPreferences(getString(R.string.shared_pref_name), Context.MODE_PRIVATE)

        val token = preferences.getString(getString(R.string.shared_pref_token), null)

        if(token != null) {
            this.listArticles(token)
        }

        val userName = preferences.getString(getString(R.string.shared_pref_username), null)
        findViewById<Toolbar>(R.id.toolbar_home).title = "Hello $userName, here's the latest news"
    }

    private fun listArticles(token: String) {
        val progress = ProgressDialog(this)
        progress.setTitle("Loading...")
        progress.show()

        val call = WebClient().ArticleService().listArticles(token = "Bearer $token")
        call.enqueue(object : Callback<List<Article>> {
            override fun onResponse(call: Call<List<Article>>, response: Response<List<Article>>) {
                progress.dismiss()
                response.body()?.let {
                    val articles: List<Article> = it
                    adapter = ListArticleAdapter(this@HomeActivity, articles)
                    findViewById<ListView>(R.id.home_listview).adapter = adapter
                }
            }
            override fun onFailure(call: Call<List<Article>>, t: Throwable?) {
                Log.e("api:error", t?.message as String)
                progress.dismiss()
                AlertDialog.Builder(this@HomeActivity)
                    .setTitle("Erro!")
                    .setMessage("Erro while get articles")
                    .show()
            }
        })
    }
}