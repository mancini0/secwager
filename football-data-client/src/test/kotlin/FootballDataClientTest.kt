package com.secwager.refdata

import com.secwager.refdata.dto.Fixture
import com.secwager.refdata.dto.League
import com.secwager.refdata.dto.Score
import com.secwager.refdata.dto.Team
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Test

class FootballDataClientTest {
    companion object {
    }

    @Test
    fun serieA_MatchEnded() {
        runBlocking {
            val server = MockWebServer()
            val content = this::class.java.classLoader.getResource("serieAweek23.json").readText()
            server.enqueue(MockResponse().setBody(content))
            val url = server.url("/")
            val client = FootballDataClient(url.toString(), "some-auth-token");
            val fixtures = client.getFixturesByLeagueAndRound(FootballDataClient.SERIE_A_LEAGUE_ID, "Regular_Season_-_23")
            val udineseAtBrescia = fixtures.single { f -> f.awayTeam.teamName.equals("Udinese") }

            val expected = Fixture(fixtureId = 232753, leagueId = 891, eventDate = "2020-02-09T14:00:00+00:00",
                    eventTimestamp = 1581256800,
                    firstHalfStart = 1581256800,
                    secondHalfStart = 1581260400,
                    round = "Regular Season - 23",
                    status = "Match Finished",
                    elapsed = 90,
                    venue = "Stadio Mario Rigamonti",
                    referee = "Marco Piccinini, Italy",
                    statusShort = "FT",
                    league = League(name = "Serie A",
                            country = "Italy",
                            logo = "https://media.api-football.com/leagues/94.png", flag = "https://media.api-football.com/flags/it.svg"),
                    homeTeam = Team(
                            teamId = 518,
                            teamName = "Brescia",
                            logo = "https://media.api-football.com/teams/518.png"),
                    awayTeam = Team(teamId = 494,
                            teamName = "Udinese",
                            logo = "https://media.api-football.com/teams/494.png"), goalsAwayTeam = 1, goalsHomeTeam = 1, score =
            Score(halftime = "0-0", fulltime = "1-1", extraTime = null, penalty = null))

            assertEquals(udineseAtBrescia, expected)

        }
    }
}
