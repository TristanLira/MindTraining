package com.example.mindtraining

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

class GameStats(public val score: Int, public val played: Int, public val difficulty: String) {

    @RequiresApi(Build.VERSION_CODES.O)
    public val date = LocalDateTime.now()


}