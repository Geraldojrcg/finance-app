package com.example.financeapp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.financeapp.model.Article
import com.google.gson.Gson
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
        val userId = preferences.getInt(getString(R.string.shared_pref_userid), 0)

        if(token != null) {
            this.listArticles(token, userId)
        }

        val userName = preferences.getString(getString(R.string.shared_pref_username), null)
        findViewById<Toolbar>(R.id.toolbar_home).title = "Hello $userName, here's the latest news"

        findViewById<ListView>(R.id.home_listview).onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                val item = adapter.getItem(position)
                val json = Gson().toJson(item)
                with (preferences.edit()) {
                    putString(getString(R.string.shared_pref_selected_article), json)
                    commit()
                }
                startActivityForResult(Intent(this@HomeActivity, ArticleActivity::class.java), 1)
            }
    }

    private fun listArticles(token: String, userId: Int) {
        val progress = ProgressDialog(this)
        progress.setTitle("Loading...")
        progress.show()

        val call = WebClient().ArticleService().listArticles(token = "Bearer $token", userId = userId)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val token = preferences.getString(getString(R.string.shared_pref_token), null)
        val userId = preferences.getInt(getString(R.string.shared_pref_userid), 0)

        if(token != null) {
            this.listArticles(token, userId)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_action_logout -> {
                preferences.edit().clear().apply()
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}