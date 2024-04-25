package com.practice.cricketscore.models

data class Bowler(
    var name: String = "",
    var runs: Int = 0,
    var balls: Int = 0,
    var wickets: Int = 0,
    val imgUrl: String = "",
    var extras: Int = 0,
)