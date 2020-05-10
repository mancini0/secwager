package com.secwager.refdata

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.secwager.refdata.dto.*
import okhttp3.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.time.LocalDateTime

interface FootballDataClient {

    companion object {
        const val SERIE_A_LEAGUE_ID = 891;
        const val EPL_LEAGUE_ID = 524;

        fun create(baseUrl:String, accessKey:String): FootballDataClient {

            val gson = GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()

            val httpClient = OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder().addHeader("X-RapidAPI-Key", accessKey).build()
                        chain.proceed(request)
                    }.build()

            return Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(FootballDataClient::class.java)
        }
    }

    @GET("/fixtures/league/{leagueId}/{round}")
    fun getFixturesByLeagueAndRound(@Path("leagueId") leagueId: Int, @Path("round") round: String): Call<FixtureResponseWrapper>


}