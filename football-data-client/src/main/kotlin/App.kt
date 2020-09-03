package com.secwager.refdata

import com.secwager.refdata.dto.Fixture
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.*

fun main() {
    runBlocking {
        val client: FDC = FDC("https://api-football.com/demo/v2/", UUID.randomUUID().toString())
        val fixtures = mutableListOf<Fixture>()
        listOf(FDC.EPL_LEAGUE_ID, FDC.LA_LIGA_LEAGUE_ID, FDC.SERIE_A_LEAGUE_ID).map { league ->
            async {
                client.getCurrentRound(league).let { round ->
                    client.getFixturesByLeagueAndRound(league, round)
                }
            }
        }.forEach {
            it.await().let { fixtures.addAll(it) }
        }
        println(fixtures);
    }
}