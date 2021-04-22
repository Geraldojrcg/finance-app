package com.example.financeapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.financeapp.model.Article
import com.squareup.picasso.Picasso

class ListArticleAdapter (private var context: Context, private var articles: List<Article>): BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val article = articles[position]
        val hoder: ViewHolder
        val row: View

        if(convertView == null) {
            row = LayoutInflater.from(context).inflate(R.layout.article_item, parent, false)
            hoder = ViewHolder(row)
            row.tag = hoder
        } else {
            row = convertView
            hoder = convertView.tag as ViewHolder
        }

        hoder.article_card_title.text = article.title
        val image = hoder.article_card_image

        if (article.image.isEmpty()) {
            image.setImageResource(R.mipmap.ic_launcher);
        } else{
            Picasso.get().load(article.image).into(image)
        }

        return row
    }

    override fun getItem(position: Int): Any = articles[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = articles.size

    companion object {
        data class ViewHolder(val view: View) {
            val article_card_image: ImageView = view.findViewById(R.id.article_card_image)
            val article_card_title: TextView = view.findViewById(R.id.article_card_title)
        }
    }
}