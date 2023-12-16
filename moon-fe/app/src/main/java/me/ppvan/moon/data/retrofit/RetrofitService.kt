package me.ppvan.moon.data.retrofit

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {
    var retrofit: Retrofit? = null
        private set

    init {
        initializeRetrofit()
    }

    private fun initializeRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl("http://172.20.10.3:8080")
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
    }
}