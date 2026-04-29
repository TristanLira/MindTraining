package com.example.mindtraining

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mindtraining.config.GameLevel
import com.example.mindtraining.GameStats
import com.example.mindtraining.Operation
import com.example.mindtraining.StatsManager

class GameActivity : AppCompatActivity() {

    private lateinit var timeLeftText: TextView
    private lateinit var scoreText: TextView

    private lateinit var operationText: TextView

    private lateinit var opt0: Button
    private lateinit var opt1: Button
    private lateinit var opt2: Button
    private lateinit var opt3: Button

    private var correctOpt: Int = -1

    private var score: Int = 0
    private var played: Int = 0

    private var selectedLevel: GameLevel = GameLevel.EASY

    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val levelString = intent.getStringExtra("selectedLevel")

        when(levelString) {
            "EASY" -> selectedLevel = GameLevel.EASY
            "MEDIUM" -> selectedLevel = GameLevel.MEDIUM
            "HARD" -> selectedLevel = GameLevel.HARD
        }

        initComponents()
        initTimer()

        //inicia el juego con la primera operación
        generateNewOperation()
    }

    private fun initComponents() {
        timeLeftText = findViewById(R.id.timeLeftText)
        scoreText = findViewById(R.id.scoreText)
        operationText = findViewById(R.id.operationText)

        scoreText.text = score.toString()

        opt0 = findViewById(R.id.opt0)
        opt1 = findViewById(R.id.opt1)
        opt2 = findViewById(R.id.opt2)
        opt3 = findViewById(R.id.opt3)

        opt0.setOnClickListener { validateAnswer(0) }
        opt1.setOnClickListener { validateAnswer(1) }
        opt2.setOnClickListener { validateAnswer(2) }
        opt3.setOnClickListener { validateAnswer(3) }
    }

    private fun initTimer() {
        timer = object: CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val remaining = millisUntilFinished / 1000
                timeLeftText.text = "${remaining}s"
            }

            override fun onFinish() {
                timeLeftText.text = "0s"
                showScore()
            }

        }.start()
    }

    private fun showScore() {
        //registra el juego que acaba de terminar
        val stats = GameStats(score, played, selectedLevel.toString())
        StatsManager.addStats(stats)

        val dialog = AlertDialog.Builder(this)
            .setMessage("Puntaje: ${score}/$played")
            .setTitle("Terminó el juego!")
            .setPositiveButton("Regresar al menú", { dialog, which -> returnToMenu() })
            .show()
    }

    private fun returnToMenu() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun generateNewOperation() {
        /*La manera en que se genera la operación y las respuestas erróneas es mediante la clase Operation.
        * Operation recibe dos enteros y un operador char, y según los datos regresa el resultado con el
        * metodo result.
        * Para generar resultados convincentes, se generan cuatro operaciones diferentes con los mismos
        * parámetros (según el nivel) y se selecciona una sola como la correcta. De esta manera, todas las
        * opciones tienen resultados convincentes y no dentro de un rango fijo*/

        //primero genera el arreglo con objetos predeterminados, después los asigna en el when
        val operations = Array(4) { Operation(0, 0, '+') }

        when(selectedLevel) {
            GameLevel.EASY -> {
                for (i in 0..3) {
                    val n1: Int = (0..10).random()
                    val n2: Int = (0..10).random()
                    val operator: Char = arrayOf('+', '-').random()
                    operations[i] = Operation(n1, n2, operator)
                }
            }

            GameLevel.MEDIUM -> {
                for (i in 0..3) {
                    val n1: Int = (0..20).random()
                    val n2: Int = (0..20).random()
                    val operator: Char = arrayOf('+', '-', '*').random()
                    operations[i] = Operation(n1, n2, operator)
                }
            }

            GameLevel.HARD -> {
                for (i in 0..3) {
                    val n1: Int = (1..50).random()
                    val n2: Int = (1..50).random()
                    val operator: Char = arrayOf('+', '-', '*', '/').random()
                    operations[i] = Operation(n1, n2, operator)
                }
            }
        }

        val correctOpt = (0 until operations.size).random() //posición aleatoria en el array
        this.correctOpt = correctOpt

        //agrega la operación y resultados al
        operationText.text = operations[correctOpt].toString()
        opt0.text = operations[0].result().toString()
        opt1.text = operations[1].result().toString()
        opt2.text = operations[2].result().toString()
        opt3.text = operations[3].result().toString()
    }

    private fun validateAnswer(answer: Int) {
        played++

        if (answer == correctOpt) {
            score++
            scoreText.text = score.toString()
        }
        else {
            Toast.makeText(this, "Respuesta incorrecta.", Toast.LENGTH_SHORT).show()
        }

        //genera una nueva operación sin importar si se respondió bien o mal
        generateNewOperation()
    }

}