package com.android.casestudy.data.modal

import androidx.annotation.ColorRes

data class PlayersInfo(
    val name: String,
    val points: Int,
    val id: Int,
    val icon: String,
    val matchStats: List<MatchStats>
)

data class MatchStats(val winner: Player, val looser: Player, @ColorRes val resultColor: Int)

data class Player(val points: Int, val name: String)

data class PlayerDetails constructor(
    val name: String,
    val icon: String,
    val id: Int,
)