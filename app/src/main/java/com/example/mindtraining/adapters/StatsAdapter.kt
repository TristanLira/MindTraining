package com.example.mindtraining.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.mindtraining.GameStats
import com.example.mindtraining.R


class StatsAdapter(context: Context, var stats: ArrayList<GameStats>):
    ArrayAdapter<GameStats>(context, 0, stats){

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_stats, parent, false)

        val stat = stats[position]

        val scoreText = view.findViewById<TextView>(R.id.scoreText)
        val difficultyText = view.findViewById<TextView>(R.id.difficultyText)
        val dateText = view.findViewById<TextView>(R.id.dateText)

        scoreText.text = "${stat.score} / ${stat.played}"
        difficultyText.text = "(${stat.difficulty})"
        dateText.text = stat.date.toString()

        return view
    }
}