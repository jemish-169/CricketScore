package com.practice.cricketscore.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.practice.cricketscore.R
import com.practice.cricketscore.databinding.ItemMatchBinding
import com.practice.cricketscore.models.Match
import com.practice.cricketscore.utils.Constants

class MatchAdapter(
    private val context: Context,
    private var matchList: ArrayList<Match>
) :
    RecyclerView.Adapter<MatchAdapter.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    private lateinit var binding: ItemMatchBinding

    inner class ViewHolder(binding: ItemMatchBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemMatchBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return matchList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(matchList[position]) {
                binding.matchTitle.text = "${this.battingTeam.name} vs. ${this.bowlingTeam.name}"
                binding.battingTeamName.text = this.battingTeam.name
                binding.battingTeamRunWicket.text =
                    "${this.matchState.run} / ${this.matchState.wickets}"
                binding.overs.text =
                    "${this.matchState.ballNumber / 6}.${this.matchState.ballNumber % 6}"
                binding.battingTeamRunRate.text =
                    if (this.matchState.ballNumber == 0)
                        "CR - ${(this.matchState.run / (this.matchState.ballNumber + 1)) * 6}"
                    else
                        "CR - ${(this.matchState.run / (this.matchState.ballNumber)) * 6}"
                binding.matchNumber.text = "Match ${position + 1}"
                binding.matchStatus.text = this.matchStatus

                if (matchStatus == Constants.RUNNING) {
                    binding.matchStatus.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.red_background
                        )
                    )
                    binding.matchUpdateButton.visibility = View.VISIBLE
                } else {
                    binding.matchStatus.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.green_background
                        )
                    )
                    binding.matchUpdateButton.visibility = View.GONE
                }

                binding.root.setOnClickListener {
                    onItemClickListener?.onClick(position, this)
                }

                binding.matchUpdateButton.setOnClickListener {
                    onItemClickListener?.onClick(-1, this)
                }
                binding.shareMatch.setOnClickListener {
                    val str =
                        "${binding.matchNumber.text}\t${binding.matchTitle.text}\n${binding.battingTeamName.text} ${binding.battingTeamRunWicket.text}\n${binding.battingTeamRunRate.text}\t${binding.overs.text}"
                    val shareIntent = Intent()
                    shareIntent.setAction(Intent.ACTION_SEND)
                    shareIntent.setType("text/plain")
                    shareIntent.putExtra(Intent.EXTRA_TEXT, str);
                    context.startActivity(Intent.createChooser(shareIntent, "Choose an app"))

                }
            }
        }
    }

    fun setOnClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun updateMatchList(list: ArrayList<Match>) {
        this.matchList = list
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClick(position: Int, model: Match)
    }
}