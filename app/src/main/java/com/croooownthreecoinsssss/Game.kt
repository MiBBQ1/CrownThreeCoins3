package com.croooownthreecoinsssss

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random

class Game : AppCompatActivity() {

    private lateinit var scoreView: TextView
    private var isClick = false

    private val imgViews: MutableList<ImageView> = mutableListOf()

    private val drawableList = listOf(
        R.drawable.img1,
        R.drawable.img2,
        R.drawable.img3,
        R.drawable.img4,
        R.drawable.img5,
        R.drawable.img6,
        R.drawable.img7,
        R.drawable.img8,
        R.drawable.img9,
        R.drawable.img10,
    )

    private val imageLose = R.drawable.button

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        scoreView = findViewById(R.id.scoreText)

        imgViews.add(findViewById(R.id.img1))
        imgViews.add(findViewById(R.id.img2))
        imgViews.add(findViewById(R.id.img3))
        imgViews.add(findViewById(R.id.img4))
        imgViews.add(findViewById(R.id.img5))
        imgViews.add(findViewById(R.id.img6))

        game()

    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun game() {
        imgViews.forEach { image ->
            image.setImageDrawable(null)
            image.setOnClickListener {
                var score = scoreView.text.toString().split(": ")[1].toInt()
                if (!isClick) {
                    isClick = true
                    val drawable = if (Random.nextInt(imgViews.size) == 1) imageLose else drawableList.random()
                    image.setImageDrawable(getDrawable(drawable))
                    if (drawable == imageLose) {
                        score = 0
                        Toast.makeText(applicationContext, "You Lose...", Toast.LENGTH_SHORT).show()
                    }
                    else score++

                    scoreView.text = "Score: $score"
                    object : CountDownTimer(500, 500) {
                        override fun onTick(millisUntilFinished: Long) {

                        }

                        override fun onFinish() {
                            isClick = false
                            game()
                        }
                    }.start()
                }
            }
        }
    }
}