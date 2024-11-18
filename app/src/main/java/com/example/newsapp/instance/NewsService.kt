package com.example.newsapp.instance

import com.example.newsapp.model.News
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//https://newsapi.org/v2/top-headlines?country=us&apiKey=d46a606082784036bc4c42e02a575508
//https://newsapi.org/v2/everything?q=apple&from=2024-10-14&to=2024-10-14&sortBy=popularity&apiKey=d46a606082784036bc4c42e02a575508

const val BASE_URL = "https://newsapi.org/"
const val API_KEY = "49fb6a04ac754376b0f9a2ce56d10184"

interface NewsInterface {

    @GET("v2/top-headlines?apiKey=$API_KEY")
    fun getHeadlines(@Query("country") country:String, @Query("page") page:Int) : Call<News>

    @GET("v2/top-headlines?apiKey=$API_KEY")
    fun getCategories(@Query("country") country:String, @Query("category") category:String, @Query("page") page:Int) : Call<News>

    //above getHeadline fun call this http
    //https://newsapi.org/v2/top-headlines?apiKey=d46a606082784036bc4c42e02a575508&country=us&page=1
}

object NewsService{
    val newsInstance: NewsInterface
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        newsInstance = retrofit.create(NewsInterface::class.java)
    }
}