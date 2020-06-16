package com.secwager.refdata

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.secwager.refdata.dto.FixtureResponseWrapper
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface FootballDataClient {

    companion object {
        const val SERIE_A_LEAGUE_ID = 891;
        const val EPL_LEAGUE_ID = 524;
        const val LA_LIGA_LEAGUE_ID = 525;

        fun create(baseUrl: String, accessKey: String): FootballDataClient {

            val gson = GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .registerTypeAdapter(object : TypeToken<List<String>>() {}.getType(), MatchDaysDeserializer())
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

    @GET("/fixtures/rounds/{leagueId}/current")
    fun getCurrentRoundByLeague(@Path("leagueId") leagueId: Int): Call<List<String>>

    @GET("/fixtures/rounds/{league_id}")
    fun getRounds(@Path("leagueId") leagueId: Int): Call<List<String>>

}