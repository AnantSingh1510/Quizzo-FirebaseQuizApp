package com.example.onlinequiz

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.onlinequiz.databinding.ActivityQuizBinding
import com.example.onlinequiz.databinding.ScoreDialogBinding

class QuizActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        var questionModelList : List<QuestionModel> = listOf()
        var time: String = ""
    }

    lateinit var binding: ActivityQuizBinding

    var currentQuestionIndex = 0
    var selectedAnswer = ""
    var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadQuestions()
        startTimer()

        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            nextBtn.setOnClickListener(this@QuizActivity)
        }
    }

    private fun startTimer() {
        var totalTimeInMillis = time.toInt() * 60 * 1000L
        object : CountDownTimer(totalTimeInMillis, 1000L){
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished/1000
                val minutes = seconds/60
                val remainingSeconds = seconds % 60
                binding.timerIndicatorTextview.text = String.format("%02d:%02d", minutes, remainingSeconds)
            }

            override fun onFinish() {
                // Finish the quiz
            }

        }.start()
    }

    private fun loadQuestions(){

        selectedAnswer = ""

        if(currentQuestionIndex == questionModelList.size){
            finishQuiz()
            return
        }

        binding.apply {
            questionIndicatorTextview.text = "Question ${currentQuestionIndex+1} / ${questionModelList.size}"
            questionProgressIndicator.progress =
                ((currentQuestionIndex.toFloat() / questionModelList.size.toFloat()) * 100).toInt()
            questionTextview.text = questionModelList[currentQuestionIndex].question
            btn0.text = questionModelList[currentQuestionIndex].options[0]
            btn1.text = questionModelList[currentQuestionIndex].options[1]
            btn2.text = questionModelList[currentQuestionIndex].options[2]
            btn3.text = questionModelList[currentQuestionIndex].options[3]
        }
    }

    override fun onClick(v: View?) {

        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.gray))
            btn1.setBackgroundColor(getColor(R.color.gray))
            btn2.setBackgroundColor(getColor(R.color.gray))
            btn3.setBackgroundColor(getColor(R.color.gray))
        }

        val clickedButton = v as Button
        if(clickedButton.id == R.id.next_btn){

            if(selectedAnswer == questionModelList[currentQuestionIndex].correct)
                score++

            currentQuestionIndex++
            loadQuestions()
        }
        else{
            //Options
            selectedAnswer = clickedButton.text.toString()
            clickedButton.setBackgroundColor(getColor(R.color.orange))
        }
    }

    private fun finishQuiz(){
        val totalQuestions = questionModelList.size
        val percentage = ((score.toFloat() / totalQuestions.toFloat()) * 100).toInt()

        val dialogBinding = ScoreDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgressIndicator.progress = percentage
            scoreProgressText.text = "$percentage %"

            if(percentage > 60){
                scoreTitle.text = "Congratulations!"
                scoreTitle.setTextColor(Color.BLUE)
            }
            else{
                scoreTitle.text = "Better luck next time!"
                scoreTitle.setTextColor(Color.RED)
            }
            scoreSubtitle.text = "$score out of $totalQuestions are correct"
            finishBtn.setOnClickListener {
                finish()
            }
        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()
    }
}