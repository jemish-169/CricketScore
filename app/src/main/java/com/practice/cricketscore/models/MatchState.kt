package com.practice.cricketscore.models

data class MatchState(
    var run: Int = 0,
    var wickets: Int = 0,
    var ballNumber: Int = 0,
    var bowler: Bowler = Bowler(),
    var batsman1: Batsman = Batsman(),
    var batsman2: Batsman = Batsman()
)
