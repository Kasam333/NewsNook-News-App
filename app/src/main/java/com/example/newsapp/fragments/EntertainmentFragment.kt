package com.example.newsapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.activities.DetailActivity
import com.example.newsapp.adapter.CategoriesNewsAdapter
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.instance.NewsService
import com.example.newsapp.model.Article
import com.example.newsapp.model.News
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EntertainmentFragment : Fragment(R.layout.fragment_entertainment) {

    lateinit var adapter: CategoriesNewsAdapter
    private var articles = mutableListOf<Article>()
    var pageNum = 1
    var totalResults = -1
    var isLoading = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        adapter = CategoriesNewsAdapter(requireContext(), articles)
        val newsRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)

        // Set up the layout manager to be vertical like a feed
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        newsRecyclerView.layoutManager = layoutManager
        newsRecyclerView.adapter = adapter

        // Fetch initial news data
        getNews()
    }

    private fun getNews() {
        isLoading = true
        val news = NewsService.newsInstance.getCategories("us","entertainment" ,pageNum)
        news.enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                val news = response.body()
                if (news != null) {
                    totalResults = news.totalResults

                    // Filter out articles with unwanted data
                    val validArticles = news.articles.filter { article ->
                        // Filter condition: check for null or "[Removed]" in relevant fields
                        article.title != "[Removed]" && article.description != "[Removed]"
                    }

                    // Add valid articles to the list and notify the adapter
                    articles.addAll(validArticles)
                    adapter.notifyDataSetChanged()
                    isLoading = false
                    loadMoreNews()
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.d("NewsApp", "Error in fetching news", t)
                isLoading = false
            }
        })
    }

    private fun loadMoreNews() {
        if (articles.size < totalResults) {
            pageNum++
            getNews()
        }
    }
}