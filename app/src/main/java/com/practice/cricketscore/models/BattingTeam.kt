package com.practice.cricketscore.models

data class BattingTeam(
    var name: String = "",
    var players: List<Batsman> = listOf(Batsman(), Batsman(), Batsman(), Batsman(), Batsman())
)