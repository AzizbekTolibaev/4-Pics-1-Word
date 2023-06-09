package com.example.a4pics1word
object Contains {
    fun getQuestions(): MutableList<Question> {
        val questions = mutableListOf<Question>()
        questions.add(
            Question(
                0,
                listOf(
                    R.drawable.photo1_1,
                    R.drawable.photo1_2,
                    R.drawable.photo1_3,
                    R.drawable.photo1_4
                ),
                "ХОЛОД"
            )
        )
        questions.add(
            Question(
                1,
                listOf(
                    R.drawable.photo2_1,
                    R.drawable.photo2_2,
                    R.drawable.photo2_3,
                    R.drawable.photo2_4
                ),
                "ГРОМКО"
            )
        )
        questions.add(
            Question(
                2,
                listOf(
                    R.drawable.photo3_1,
                    R.drawable.photo3_2,
                    R.drawable.photo3_3,
                    R.drawable.photo3_4
                ),
                "ГОРЯЧИЙ"
            )
        )
        questions.add(
            Question(
                3,
                listOf(
                    R.drawable.photo4_1,
                    R.drawable.photo4_2,
                    R.drawable.photo4_3,
                    R.drawable.photo4_4
                ),
                "МУЗЫКА"
            )
        )
        questions.add(
            Question(
                2,
                listOf(
                    R.drawable.photo5_1,
                    R.drawable.photo5_2,
                    R.drawable.photo5_3,
                    R.drawable.photo5_4
                ),
                "ТОЧКА"
            )
        )
        return questions
    }
}