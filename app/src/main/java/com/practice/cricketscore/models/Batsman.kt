package com.practice.cricketscore.models

data class Batsman(
    var name: String = "",
    var runs: Int = 0,
    var balls: Int = 0,
    var wicketReason: String = "",
    val imgUrl: String = "",
)