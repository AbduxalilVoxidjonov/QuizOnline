package com.example.quizonline.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizonline.QuizActivity
import com.example.quizonline.databinding.TopicItemBinding
import com.example.quizonline.network.model.topic.TopicData
import com.example.quizonline.network.model.topic.TopicDataItem

class TopicListAdapter(private val topicData: TopicData) :
    RecyclerView.Adapter<TopicListAdapter.QuizViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = TopicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuizViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.bind(topicData.data[position])
    }

    override fun getItemCount(): Int {
        return topicData.data.size
    }

    class QuizViewHolder(private val binding: TopicItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(topicDataItem: TopicDataItem) {
            binding.apply {
                topicId.text = topicDataItem.id.toString() + " - Mavzu"
                topicName.text = topicDataItem.name
                root.setOnClickListener {
                    val intent = Intent(root.context, QuizActivity::class.java)
                    intent.putExtra("topicId", topicDataItem.id)
                    root.context.startActivity(intent)
                }
            }
        }
    }
}