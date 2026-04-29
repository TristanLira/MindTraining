package com.example.mindtraining

import android.app.AlertDialog
import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mindtraining.adapters.StatsAdapter
import com.example.mindtraining.config.GameLevel

class MainActivity : AppCompatActivity() {

    private lateinit var mainToolbar: Toolbar

    private lateinit var rgDifficulty: RadioGroup
    private lateinit var rbEasy: RadioButton
    private lateinit var rbMedium: RadioButton
    private lateinit var rbHard: RadioButton

    private lateinit var btnStart: Button

    private var selectedLevel: GameLevel = GameLevel.EASY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initToolbar()
        initRadioGroup()
        initStartButton()
    }

    private fun initToolbar() {
        mainToolbar = findViewById(R.id.mainToolbar)
        setSupportActionBar(mainToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.scoreItem -> createListDialog()
            R.id.exitItem -> confirmExit()
        }

        return true
    }

    private fun createListDialog() {
        val dialogView = layoutInflater.inflate(R.layout.stats_list_dialog, null)
        val statsListView = dialogView.findViewById<ListView>(R.id.statsListView)
        val adapter = StatsAdapter(this, StatsManager.statsList)
        statsListView.adapter = adapter

        val dialog = AlertDialog.Builder(this)
            .setTitle("Puntuaciones")
            .setView(dialogView)
            .setPositiveButton("Cerrar", null)
            .create()
            .show()
    }

    private fun initRadioGroup() {
        rgDifficulty = findViewById(R.id.rgDifficulty)
        rbEasy = findViewById(R.id.rbEasy)
        rbMedium = findViewById(R.id.rbMedium)
        rbHard = findViewById(R.id.rbHard)

        rgDifficulty.setOnCheckedChangeListener { group, selectedRadio ->
            when(selectedRadio) {
                R.id.rbEasy -> selectedLevel = GameLevel.EASY
                R.id.rbMedium -> selectedLevel = GameLevel.MEDIUM
                R.id.rbHard -> selectedLevel = GameLevel.HARD
            }
        }
    }

    private fun initStartButton() {
        btnStart = findViewById(R.id.btnStart)

        btnStart.setOnClickListener {
            if (rgDifficulty.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Selecciona un nivel", Toast.LENGTH_SHORT).show()
            } else {
                startGame()
            }
        }
    }

    private fun startGame() {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("selectedLevel", selectedLevel.name)
        startActivity(intent)
        finish()
    }

    private fun confirmExit() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Salir")
            .setMessage("Quiere salir de la aplicación?")
            .setPositiveButton("Confirmar", {dialog, x -> finish()} )
            .setNegativeButton("Cancelar", {dialog, x -> dialog.dismiss()} )
            .create()
        dialog.show()
    }

}