package com.practice.cricketscore.models

import com.practice.cricketscore.utils.Constants
import java.util.UUID

data class Match(
    var id: String = UUID.randomUUID().toString(),
    val battingTeam: BattingTeam = BattingTeam(),
    val bowlingTeam: BowlingTeam = BowlingTeam(),
    val matchState: MatchState = MatchState(),
    var matchStatus: String = Constants.RUNNING,
    val bowlingTimeline: ArrayList<BallAction> = ArrayList()
)