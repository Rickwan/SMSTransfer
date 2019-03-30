package com.wq.smstransfer.net

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author wq
 * @date 2019/3/28 下午6:39
 * @desc ${TODO}
 */
class NetworkRequest {


    companion object {

        private val url = "https://sc.ftqq.com/"

         val SECRET_KEY = "SCU47331T3a529c231357cf40ba74d7d285708e985c9dcd64ee4af"

        private var service: Interfaces? = null

        fun getInstance(): Interfaces {

            if (service == null) {

                val okHttpBuilder = OkHttpClient.Builder()
                okHttpBuilder.readTimeout(60, TimeUnit.SECONDS)
                okHttpBuilder.connectTimeout(60, TimeUnit.SECONDS)

                val builder = GsonBuilder()
                builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                val gson = builder.create()

                val retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .client(okHttpBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

                service = retrofit.create(Interfaces::class.java)
            }


            return service!!
        }

    }


}