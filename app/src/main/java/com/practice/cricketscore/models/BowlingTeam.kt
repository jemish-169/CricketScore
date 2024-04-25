package com.practice.cricketscore.models

data class BowlingTeam(
    var name: String = "",
    var players: List<Bowler> = listOf(Bowler(), Bowler(), Bowler(), Bowler(), Bowler())
)