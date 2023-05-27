package com.example.a4pics1word

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.a4pics1word.databinding.ActivityGameBinding
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private var currentIndex = 0
    private var coin = 0
    private var click = 0
    private var level = 1
    private lateinit var vibratePhone: Vibrator
    private val answerList = mutableListOf<TextView>()
    private val optionList = mutableListOf<TextView>()
    private val userAnswerList = mutableListOf<Pair<String, TextView>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vibratePhone = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val shPreferences = getSharedPreferences("shPreferences", Context.MODE_PRIVATE)

        val lastIndex = shPreferences.getInt("last_question", 0)
        currentIndex = lastIndex
        level = lastIndex + 1

        val coinNum = shPreferences.getInt("tv_coin", 0)
        coin = coinNum

        binding.tvLevel.text = level.toString()
        binding.tvCoin.text = coin.toString()

        fillOptionAndAnswerList()
        setQuestion()

        binding.apply {
            backImg.setOnClickListener {
                onBackPressed()
            }

            img1.setOnClickListener {
                click = 1
                animationScaleImageView(1)
            }

            img2.setOnClickListener {
                click = 2
                animationScaleImageView(2)
            }

            img3.setOnClickListener {
                click = 3
                animationScaleImageView(3)
            }

            img4.setOnClickListener {
                click = 4
                animationScaleImageView(4)
            }

            bigImage.setOnClickListener {
                defaultAnimation(click)
            }

            addAnswerLetter.setOnClickListener {
                val question = Contains.getQuestions()[currentIndex]

                if (tvCoin.text.toString()
                        .toInt() >= 10 && userAnswerList.filter { it.first != "" }.size != question.answer.length
                ) {
                    coin -= 10
                    tvCoin.text = coin.toString()
                    shPreferences.edit().putInt("tv_coin", coin).apply()
                    val empty = answerList.find { it.text == "" }
                    val emptyIndex = answerList.indexOf(empty)
                    val letter = question.answer[emptyIndex].toString()
                    val optionView = optionList.find { it.text == letter }
                    optionView?.let {
                        setLetter(it, true)
                    }
                    empty?.isEnabled = false
                } else {
                    Toast.makeText(applicationContext, "Not enough coins!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        optionList.forEach { tv ->
            tv.setOnClickListener {
                setLetter(tv)
            }
        }

        answerList.forEach { tv ->
            tv.setOnClickListener {
                removeLetter(tv)
            }
        }
    }

    private fun setQuestion() {
        val que = Contains.getQuestions()[currentIndex]

        binding.apply {
            img1.setImageResource(que.images[0])
            img2.setImageResource(que.images[1])
            img3.setImageResource(que.images[2])
            img4.setImageResource(que.images[3])
        }

        answerList.forEach {
            it.text = ""
            it.visibility = View.GONE
            it.setTextColor(Color.parseColor("#FFFFFF"))
        }

        repeat(que.answer.length) {
            answerList[it].visibility = View.VISIBLE
        }

        userAnswerList.clear()

        setOptionLetters()
    }

    private fun setOptionLetters() {
        binding.apply {
            val optionLetters = mutableListOf<Char>()
            optionLetters.addAll(Contains.getQuestions()[currentIndex].answer.toList())

            repeat(12 - optionLetters.size) {
                optionLetters.add(Random.nextInt(1040, 1072).toChar())
            }
            optionLetters.shuffle()

            optionList.forEachIndexed { index, textView ->
                textView.text = optionLetters[index].toString()
            }
        }
    }

    private fun setLetter(textView: TextView, isGreen: Boolean = false) {
        val letter = textView.text.toString()
        if (letter.isNotEmpty() && userAnswerList.filter { it.first != "" }.size != Contains.getQuestions()[currentIndex].answer.length) {
            val pair = Pair(letter, textView)
            val emptyIndex = userAnswerList.indexOf(Pair("", binding.tvLevel))
            if (emptyIndex == -1) {
                userAnswerList.add(pair)
            } else {
                userAnswerList[emptyIndex] = pair
            }
            if (isGreen) {
                answerList[userAnswerList.indexOf(pair)].setTextColor(Color.parseColor("#7CB342"))
            }
            textView.text = ""
            answerList[userAnswerList.indexOf(pair)].text = letter
        }

        // Success letter
        if (userAnswerList.filter { it.first != "" }.size == Contains.getQuestions()[currentIndex].answer.length) {
            var answer = ""
            userAnswerList.forEach {
                answer += it.first
            }
            if (answer == Contains.getQuestions()[currentIndex].answer) {
                if (currentIndex == 4) {
                    currentIndex = 0
                    level = 1
                    binding.tvLevel.text = level.toString()
                } else {
                    currentIndex++
                    level++
                    binding.tvLevel.text = level.toString()
                }
                val shPreferences = getSharedPreferences("shPreferences", Context.MODE_PRIVATE)
                shPreferences.edit().putInt("last_question", currentIndex).apply()

                binding.winRotateImg.startAnimation(
                    AnimationUtils.loadAnimation(
                        this, R.anim.win_rotate
                    )
                )

                binding.winLayout.visibility = View.VISIBLE

                binding.winImage.setOnClickListener {
                    val addCoin = coin + 4
                    coin = addCoin
                    binding.tvCoin.text = addCoin.toString()
                    shPreferences.edit().putInt("tv_coin", addCoin).apply()

                    binding.winLayout.visibility = View.GONE
                    setQuestion()
                }
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                vibratePhone.vibrate(60)
            }
        }
    }

    private fun removeLetter(textView: TextView) {
        val letter = textView.text.toString()
        if (letter.isNotEmpty()) {
            val index = answerList.indexOf(textView)
            val pair = userAnswerList[index]
            textView.text = ""
            pair.second.text = pair.first

            userAnswerList[index] = Pair("", binding.tvLevel)
        }
    }

    private fun fillOptionAndAnswerList() {
        binding.apply {
            optionList.add(tvOption1)
            optionList.add(tvOption2)
            optionList.add(tvOption3)
            optionList.add(tvOption4)
            optionList.add(tvOption5)
            optionList.add(tvOption6)
            optionList.add(tvOption7)
            optionList.add(tvOption8)
            optionList.add(tvOption9)
            optionList.add(tvOption10)
            optionList.add(tvOption11)
            optionList.add(tvOption12)

            answerList.add(tvAnswer1)
            answerList.add(tvAnswer2)
            answerList.add(tvAnswer3)
            answerList.add(tvAnswer4)
            answerList.add(tvAnswer5)
            answerList.add(tvAnswer6)
            answerList.add(tvAnswer7)
            answerList.add(tvAnswer8)

        }
    }

    private fun animationScaleImageView(id: Int) {
        val question = Contains.getQuestions()[currentIndex]
        when (id) {
            1 -> {
                binding.bigImage.setImageResource(question.images[0])
                binding.bigImage.visibility = View.VISIBLE

                binding.bigImage.startAnimation(
                    AnimationUtils.loadAnimation(
                        this, R.anim.scale_up_1
                    )
                )
            }
            2 -> {
                binding.bigImage.setImageResource(question.images[1])
                binding.bigImage.visibility = View.VISIBLE

                binding.bigImage.startAnimation(
                    AnimationUtils.loadAnimation(
                        this, R.anim.scale_up_2
                    )
                )
            }
            3 -> {
                binding.bigImage.setImageResource(question.images[2])
                binding.bigImage.visibility = View.VISIBLE

                binding.bigImage.startAnimation(
                    AnimationUtils.loadAnimation(
                        this, R.anim.scale_up_3
                    )
                )
            }
            4 -> {
                binding.bigImage.setImageResource(question.images[3])
                binding.bigImage.visibility = View.VISIBLE

                binding.bigImage.startAnimation(
                    AnimationUtils.loadAnimation(
                        this, R.anim.scale_up_4
                    )
                )
            }
        }
    }

    private fun defaultAnimation(id: Int) {
        if (id != 0) {
            when (id) {
                1 -> {
                    binding.bigImage.visibility = View.GONE

                    binding.bigImage.startAnimation(
                        AnimationUtils.loadAnimation(
                            this, R.anim.scale_down_1
                        )
                    )
                }
                2 -> {
                    binding.bigImage.visibility = View.GONE

                    binding.bigImage.startAnimation(
                        AnimationUtils.loadAnimation(
                            this, R.anim.scale_down_2
                        )
                    )
                }
                3 -> {
                    binding.bigImage.visibility = View.GONE

                    binding.bigImage.startAnimation(
                        AnimationUtils.loadAnimation(
                            this, R.anim.scale_down_3
                        )
                    )
                }
                4 -> {
                    binding.bigImage.visibility = View.GONE

                    binding.bigImage.startAnimation(
                        AnimationUtils.loadAnimation(
                            this, R.anim.scale_down_4
                        )
                    )
                }
            }
            click = 0
        }
    }
}
