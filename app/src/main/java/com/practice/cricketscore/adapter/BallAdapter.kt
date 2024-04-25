package com.practice.cricketscore.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.practice.cricketscore.R
import com.practice.cricketscore.databinding.ItemBallBinding
import com.practice.cricketscore.models.BallAction

class BallAdapter(
    private val context: Context,
    private var ballList: List<BallAction>
) :
    RecyclerView.Adapter<BallAdapter.ViewHolder>() {

    private lateinit var binding: ItemBallBinding

    inner class ViewHolder(binding: ItemBallBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemBallBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return ballList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(ballList[position]) {
                binding.run.text = this.runs.toString()
                val drawable = ContextCompat.getDrawable(context, R.drawable.rounded_background)

                when (this.runs) {
                    4, 5 -> drawable?.let {
                        DrawableCompat.setTint(it, Color.parseColor("#d0f4de"))
                        binding.run.background = it
                    }

                    6, 7 -> drawable?.let {
                        DrawableCompat.setTint(it, Color.parseColor("#e4c1f9"))
                        binding.run.background = it
                    }

                    1, 2, 3 -> drawable?.let {
                        DrawableCompat.setTint(it, Color.parseColor("#fdffb6"))
                        binding.run.background = it
                    }

                    else -> drawable?.let {
                        DrawableCompat.setTint(it, Color.parseColor("#f8f7ff"))
                        binding.run.background = it
                    }
                }

                if (this.ballDesc != "") {
                    binding.runDesc.visibility = View.VISIBLE
                    binding.run.text = this.ballDesc
                    if (this.ballDesc == "W") {
                        drawable?.let {
                            DrawableCompat.setTint(it, Color.parseColor("#f08080"))
                            binding.run.background = it
                        }
                    }
                    else{
                        drawable?.let {
                            DrawableCompat.setTint(it, Color.parseColor("#fec89a"))
                            binding.run.background = it
                        }
                    }
                } else
                    binding.runDesc.visibility = View.INVISIBLE
            }
        }
    }
}