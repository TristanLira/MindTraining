package com.example.mindtraining

object StatsManager {
    val statsList = ArrayList<GameStats>()

    public fun addStats(stats: GameStats) {
        statsList.add(stats)
    }
}