package com.example.financeapp

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.financeapp.model.Article
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleActivity : AppCompatActivity() {
    private lateinit var preferences: SharedPreferences;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        preferences = applicationContext.getSharedPreferences(getString(R.string.shared_pref_name), Context.MODE_PRIVATE)

        val token = preferences.getString(getString(R.string.shared_pref_token), null)
        val userId = preferences.getInt(getString(R.string.shared_pref_userid), 0)
        val json = preferences.getString(getString(R.string.shared_pref_selected_article), null)

        if(json != null) {
            val article = Gson().fromJson(json, Article::class.java)
            supportActionBar?.title = article.title

            val image = findViewById<ImageView>(R.id.article_image)
            if (article.image.isEmpty()) {
                image.setImageResource(R.drawable.no_image_icon);
            } else{
                Picasso.get().load(article.image).into(image)
            }

            findViewById<TextView>(R.id.article_title).text = article.title

            findViewById<TextView>(R.id.article_text).text = article.body

            if(token != null && !article.readed) {
                readArticle(token, userId, article.id)
            }
        }
    }

    private fun readArticle(token: String, userId: Int, articleId: Int) {
        val call = WebClient().ArticleService().readArticle(token = "Bearer $token", userId = userId, articleId = articleId)
        call.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {}
            override fun onFailure(call: Call<Any>, t: Throwable?) {
                Log.e("api:error", t?.message as String)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}