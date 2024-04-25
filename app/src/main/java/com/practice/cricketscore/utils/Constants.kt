package com.practice.cricketscore.utils

import com.practice.cricketscore.localData.LocalData
import com.practice.cricketscore.models.Batsman
import com.practice.cricketscore.models.BattingTeam
import com.practice.cricketscore.models.Bowler
import com.practice.cricketscore.models.BowlingTeam
import com.practice.cricketscore.models.Match
import com.practice.cricketscore.models.MatchState
import java.util.UUID

class Constants {
    companion object {
        fun getBatsmanList(): List<Batsman> {
            return arrayListOf(
                Batsman("Player 1", 0, 0, "", ""),
                Batsman("Player 2", 0, 0, "", ""),
                Batsman("Player 3", 0, 0, "", ""),
                Batsman("Player 4", 0, 0, "", ""),
                Batsman("Player 5", 0, 0, "", ""),
            )
        }

        fun getBowlerList(): List<Bowler> {
            return arrayListOf(
                Bowler("Player 6", 0, 0, 0, "", 0),
                Bowler("Player 7", 0, 0, 0, "", 0),
                Bowler("Player 8", 0, 0, 0, "", 0),
                Bowler("Player 9", 0, 0, 0, "", 0),
                Bowler("Player 10", 0, 0, 0, "", 0),
            )
        }

        const val RUNNING: String = "Running"
        const val COMPLETED: String = "Completed"
        const val USER_NAME = "user_name"

        val INIT_MATCH = Match(
            id = UUID.randomUUID().toString(),
            battingTeam = BattingTeam("", getBatsmanList()),
            bowlingTeam = BowlingTeam("", getBowlerList()),
            matchState = MatchState(0, 0, 0, Bowler(), Batsman(), Batsman()),
            matchStatus = RUNNING,
            matchCreatedBy = LocalData.getUserName(),
            bowlingTimeline = ArrayList()
        )
    }
}