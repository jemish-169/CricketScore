package com.practice.cricketscore.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.practice.cricketscore.databinding.FragmentUserScreenBinding
import com.practice.cricketscore.models.Match
import com.practice.cricketscore.repository.Repository
import com.practice.cricketscore.view.activity.MainActivity

class UserScreenFragment : Fragment() {

    private lateinit var binding: FragmentUserScreenBinding
    private lateinit var repository: Repository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserScreenBinding.inflate(layoutInflater)
        repository = (activity as MainActivity).getRepository()
        loadViews()
        repository.getOneMatch()
        bindObservers()
        return binding.root
    }

    private fun bindObservers() {
        repository.matchLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                updateViews(it)
            }
        })
    }


    private fun loadViews() {
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateViews(match: Match) {

        binding.userScreenToolBar.text = match.battingTeam.name + " vs " + match.bowlingTeam.name

        binding.runWicket.text = "${match.matchState.run}/${match.matchState.wickets}"
        binding.overBalls.text =
            "${match.matchState.ballNumber / 6}.${match.matchState.ballNumber % 6}"

        if (match.matchState.ballNumber == 0)
            binding.requireRunRate.text =
                "CR - ${(match.matchState.run / (match.matchState.ballNumber + 1)) * 6}"
        else binding.requireRunRate.text =
            "CR - ${(match.matchState.run / match.matchState.ballNumber) * 6}"

        binding.tvBatsmanOneName.text = match.matchState.batsman1.name
        binding.tvBatsmanTwoName.text = match.matchState.batsman2.name

        binding.tvBatsmanTwoRun.text =
            "${match.matchState.batsman2.runs}(${match.matchState.batsman1.balls})"
        binding.tvBatsmanOneRun.text =
            "${match.matchState.batsman1.runs}(${match.matchState.batsman2.balls})"

        binding.battingTeamName.text = match.battingTeam.name
        binding.bowlingTeamName.text = match.bowlingTeam.name


        for (i in 1..5) {
            val row = binding.battingTable.getChildAt(i) as TableRow

            val batsman = match.battingTeam.players[i - 1]

            (row.getChildAt(0) as TextView).text = batsman.name
            (row.getChildAt(1) as TextView).text = batsman.runs.toString()
            (row.getChildAt(2) as TextView).text = batsman.balls.toString()
            (row.getChildAt(3) as TextView).text = batsman.wicketReason
            (row.getChildAt(4) as TextView).text =
                if (batsman.balls == 0) 0.0.toString() else ((batsman.runs.toDouble() / batsman.balls.toDouble()) * 100.0).toString()

        }

        for (i in 1..5) {
            val row = binding.bowlingTable.getChildAt(i) as TableRow

            val bowler = match.bowlingTeam.players[i - 1]

            (row.getChildAt(0) as TextView).text = bowler.name
            (row.getChildAt(1) as TextView).text = bowler.runs.toString()
            (row.getChildAt(2) as TextView).text = bowler.balls.toString()
            (row.getChildAt(3) as TextView).text = bowler.wickets.toString()
            (row.getChildAt(4) as TextView).text =
                if (bowler.balls == 0) 0.0.toString() else ((bowler.runs.toDouble() / bowler.balls.toDouble()) * 6.0).toString()
            (row.getChildAt(5) as TextView).text = bowler.extras.toString()
        }
    }

}