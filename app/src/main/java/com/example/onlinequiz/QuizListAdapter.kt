package com.example.onlinequiz

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.onlinequiz.databinding.QuizItemRecyclerRowBinding

class QuizListAdapter(private val quizModelList: List<QuizModel>) :
    RecyclerView.Adapter<QuizListAdapter.ViewHolder>() {

    class ViewHolder(private val binding: QuizItemRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: QuizModel){
            binding.apply {
                quizTitleText.text = model.title
                quizSubtitleText.text = model.subtitle
                quizTimeText.text = model.time + " min"

                root.setOnClickListener {
                    val intent = Intent(root.context, QuizActivity::class.java)
                    QuizActivity.questionModelList = model.questionList
                    QuizActivity.time = model.time
                    root.context.startActivity(intent)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = QuizItemRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return quizModelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(quizModelList[position])
    }
}