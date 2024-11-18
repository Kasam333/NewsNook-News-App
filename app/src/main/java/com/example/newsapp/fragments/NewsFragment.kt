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
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.instance.NewsService
import com.example.newsapp.model.Article
import com.example.newsapp.model.News
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment : Fragment(R.layout.fragment_news) {

    lateinit var adapter: NewsAdapter
    private var articles = mutableListOf<Article>()
    var pageNum = 1
    var totalResults = -1
    var isLoading = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        adapter = NewsAdapter(requireContext(), articles)
        val newsRecyclerView = view.findViewById<RecyclerView>(R.id.newsRecyclerview)

        // Set up the layout manager to be vertical like a feed
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        newsRecyclerView.layoutManager = layoutManager
        newsRecyclerView.adapter = adapter

        // PagerSnapHelper to make it scroll like Instagram Reels (snap full item into view)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(newsRecyclerView)

        // Add scroll listener for pagination to load more news
        newsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && (visibleItemCount + pastVisibleItems) >= totalItemCount && pastVisibleItems >= 0) {
                    loadMoreNews()
                }
            }
        })

        // Attach ItemTouchHelper to RecyclerView for swipe detection
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // No movement required in this case
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Handle the swipe action
                if (direction == ItemTouchHelper.LEFT) {
                    val position = viewHolder.adapterPosition
                    val article = articles[position]

                    // Start the DetailActivity with the article URL
                    val intent = Intent(requireContext(), DetailActivity::class.java)
                    intent.putExtra("URL", article.url)
                    startActivity(intent)

                    // Reset the swiped item (so it's not removed from RecyclerView)
                    adapter.notifyItemChanged(position)
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(newsRecyclerView)

        // Fetch initial news data
        getNews()
    }

    private fun getNews() {
        isLoading = true
        val news = NewsService.newsInstance.getHeadlines("us", pageNum)
        news.enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                val news = response.body()
                if (news != null) {
                    totalResults = news.totalResults

                    // Filter out articles with unwanted data
                    val validArticles = news.articles.filter { article ->
                        // Filter condition: check for null or "[Removed]" in relevant fields
                        article.title != "[Removed]" && article.description != "[Removed]" && article.author != "[Removed]" && article.source.name != "[Removed]"
                    }

                    // Add valid articles to the list and notify the adapter
                    articles.addAll(validArticles)
                    adapter.notifyDataSetChanged()
                    isLoading = false
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