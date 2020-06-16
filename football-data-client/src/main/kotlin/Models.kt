package com.secwager.refdata.dto

import com.google.gson.annotations.SerializedName

data class FixtureResponseWrapper(val api: FixtureResponse)
data class FixtureResponse(val results: Long, val fixtures: List<Fixture>)


data class League(val name: String, val country: String, val logo: String, val flag: String)

data class Team(val teamId: Long, val teamName: String, val logo: String)

data class Score(val halftime: String?, val fulltime: String?, val extraTime: String?, val penalty: String?)


class Fixture(
        @SerializedName("fixture_id") val fixtureId: Int,
        @SerializedName("league_id") val leagueId: Int,
        val league: League,
        @SerializedName("event_date") val eventDate: String,
        @SerializedName("event_timestamp") val eventTimestamp: Long,
        @SerializedName("firstHalfStart") val firstHalfStart: Long,
        @SerializedName("secondHalfStart") val secondHalfStart: Long,
        val round: String,
        val status: String,
        @SerializedName("statusShort") val statusShort: String,
        val elapsed: Int,
        val venue: String,
        val referee: String?,
        @SerializedName("homeTeam") val homeTeam: Team,
        @SerializedName("awayTeam") val awayTeam: Team,
        @SerializedName("goalsHomeTeam") val goalsHomeTeam: Int,
        @SerializedName("goalsAwayTeam") val goalsAwayTeam: Int,
        val score: Score?)