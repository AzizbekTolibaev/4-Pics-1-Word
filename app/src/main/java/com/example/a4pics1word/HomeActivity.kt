package com.example.a4pics1word

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.a4pics1word.databinding.ActivityHomeBinding
import kotlin.random.Random

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setQuestion()

        binding.playBtn.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setQuestion() {
        val shPreferences = getSharedPreferences("shPreferences", Context.MODE_PRIVATE)
        val lastIndex = shPreferences.getInt("last_question", 0)
        val level = lastIndex + 1
        binding.tvLevel.text = level.toString()

        val que = Contains.getQuestions()[lastIndex]

        binding.apply {
            img1.setImageResource(que.images[0])
            img2.setImageResource(que.images[1])
            img3.setImageResource(que.images[2])
            img4.setImageResource(que.images[3])
        }

        val coin = shPreferences.getInt("tv_coin", 0)
        binding.tvCoin.text = coin.toString()
    }

    override fun onResume() {
        setQuestion()
        super.onResume()
    }
}