package com.secwager.refdata

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.secwager.refdata.dto.Fixture
import com.secwager.refdata.dto.FixtureResponseWrapper
import kotlinx.coroutines.future.await
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.InputStreamReader
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers

class FootballDataClient(val baseUrl: String, val apiKey: String) {


    companion object {
        private val log: Logger = LoggerFactory.getLogger(FootballDataClient::class.java)

        const val SERIE_A_LEAGUE_ID = 891;
        const val EPL_LEAGUE_ID = 524;
        const val LA_LIGA_LEAGUE_ID = 525;

        private val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(object : TypeToken<List<String>>() {}.getType(), MatchDaysDeserializer())
                .create()

        private val httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS).build()

    }

    suspend fun getCurrentRound(leagueId: Int): String? {
        return httpClient.sendAsync(HttpRequest.newBuilder()
                .header("X-RapidAPI-Key", apiKey)
                .uri(URI.create((baseUrl + "/fixtures/rounds/${leagueId}/current")).normalize())
                .build(), BodyHandlers.ofString()).thenApply {
            JsonParser.parseString(it.body()).asJsonObject["api"]
                    .asJsonObject["fixtures"].asJsonArray[0].asString
        }.exceptionally {
            log.error("could not retrieve current round for leagueId: {} due to exception {}", leagueId, it)
            null
        }.await()
    }

    suspend fun getFixturesByLeagueAndRound(leagueId: Int, roundId: String): List<Fixture> {
        return httpClient.sendAsync(HttpRequest.newBuilder()
                .header("X-RapidAPI-Key", apiKey)
                .uri(URI.create(baseUrl + "/fixtures/league/${leagueId}/${roundId}").normalize())
                .build(), BodyHandlers.ofInputStream()).thenApply {
            InputStreamReader(it.body()).use {
                (gson.fromJson(it, FixtureResponseWrapper::class.java).api.fixtures)
            }
        }.exceptionally {
            log.error("Could not retrieve fixtures for leagueId: {}, roundId: {} due to " +
                    "exception {}", leagueId, roundId, it)
            listOf()
        }.await()
    }

}