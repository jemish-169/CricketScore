package com.practice.cricketscore.view.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practice.cricketscore.adapter.BallAdapter
import com.practice.cricketscore.databinding.FragmentGameBoardBinding
import com.practice.cricketscore.databinding.MatchOverBinding
import com.practice.cricketscore.databinding.PlayerDialogBinding
import com.practice.cricketscore.models.BallAction
import com.practice.cricketscore.models.Batsman
import com.practice.cricketscore.models.Match
import com.practice.cricketscore.repository.Repository
import com.practice.cricketscore.utils.Constants
import com.practice.cricketscore.view.activity.MainActivity

class GameBoardFragment : Fragment() {

    private lateinit var binding: FragmentGameBoardBinding
    private lateinit var repository: Repository
    private var editMatch = Match()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBoardBinding.inflate(layoutInflater)
        repository = (activity as MainActivity).getRepository()
        editMatch = repository.editMatch
        initViews()
        loadViews()
        updateViews()
        return binding.root
    }

    private fun initViews() {
        if (editMatch.matchState.batsman1.name == "") editMatch.matchState.batsman1 =
            editMatch.battingTeam.players[0]
        if (editMatch.matchState.batsman2.name == "") editMatch.matchState.batsman2 =
            editMatch.battingTeam.players[1]
        if (editMatch.matchState.bowler.name == "") showBowlerDialog()
    }


    @SuppressLint("SetTextI18n")
    private fun loadViews() {
        binding.gameBoardToolBar.text =
            editMatch.battingTeam.name + " vs " + editMatch.bowlingTeam.name

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.overGame.setOnClickListener {
            gameOver()
        }
        binding.ballUndo.setOnClickListener {
            undoBall()
        }

        binding.run0.setOnClickListener { updateRun(0) }
        binding.run1.setOnClickListener { updateRun(1) }
        binding.run2.setOnClickListener { updateRun(2) }
        binding.run3.setOnClickListener { updateRun(3) }
        binding.run4.setOnClickListener { updateRun(4) }
        binding.run5.setOnClickListener { updateRun(5) }
        binding.run6.setOnClickListener { updateRun(6) }
        binding.run7.setOnClickListener { updateRun(7) }

        binding.ballNb.setOnClickListener {
            editMatch.matchState.run++
            editMatch.matchState.bowler.runs++
            editMatch.matchState.bowler.extras++

            editMatch.bowlingTeam.players.forEach {
                if (it.name == editMatch.matchState.bowler.name) {
                    it.runs++
                }
            }
            editMatch.bowlingTimeline.add(BallAction(1, 0, "NB"))
            updateViews()
            repository.updateMatch(editMatch)
        }
        binding.ballWide.setOnClickListener {
            editMatch.matchState.run++
            editMatch.matchState.bowler.runs++
            editMatch.matchState.bowler.extras++

            editMatch.bowlingTeam.players.forEach {
                if (it.name == editMatch.matchState.bowler.name) {
                    it.runs++
                }
            }
            editMatch.bowlingTimeline.add(BallAction(1, 0, "WD"))
            updateViews()
            repository.updateMatch(editMatch)
        }
        binding.ballOut.setOnClickListener {
            editMatch.matchState.wickets++
            editMatch.matchState.ballNumber++
            editMatch.battingTeam.players.forEach {
                if (it.name == editMatch.matchState.batsman1.name) {
                    it.wicketReason = "Caught out"
                    it.balls++
                }
            }
            editMatch.bowlingTeam.players.forEach {
                if (it.name == editMatch.matchState.bowler.name) {
                    it.wickets++
                    it.balls++
                }
            }
            editMatch.bowlingTimeline.add(BallAction(0, 1, "W"))
            editMatch.matchState.batsman1 = Batsman()

            if (editMatch.matchState.wickets >= 4) {
                gameOver()
            } else {
                editMatch.matchState.batsman1 =
                    editMatch.battingTeam.players[editMatch.matchState.wickets + 1]
            }
            if (editMatch.matchState.ballNumber % 6 == 0) {
                val temp = editMatch.matchState.batsman2
                editMatch.matchState.batsman2 = editMatch.matchState.batsman1
                editMatch.matchState.batsman1 = temp
                showBowlerDialog()
            }
            updateViews()
            repository.updateMatch(editMatch)
        }
    }

    private fun undoBall() {

        if (editMatch.matchState.ballNumber <= 0) return

        val run = editMatch.bowlingTimeline.last()

        editMatch.matchState.ballNumber--
        editMatch.matchState.run -= run.runs


        if (editMatch.matchState.ballNumber % 6 == 0) {
            val temp = editMatch.matchState.batsman2
            editMatch.matchState.batsman2 = editMatch.matchState.batsman1
            editMatch.matchState.batsman1 = temp

            showBowlerDialog()
        }
        if (run.runs % 2 == 1) {
            val temp = editMatch.matchState.batsman2
            editMatch.matchState.batsman2 = editMatch.matchState.batsman1
            editMatch.matchState.batsman1 = temp
        }
        if (run.ballDesc == "W")
            showBatsmanDialog()

        editMatch.bowlingTimeline.removeAt(editMatch.bowlingTimeline.lastIndex)


        if (run.ballDesc == "") {

            editMatch.battingTeam.players.forEach {
                if (it.name == editMatch.matchState.batsman1.name) {
                    it.balls--
                    it.runs -= run.runs
                }
            }
            editMatch.bowlingTeam.players.forEach {
                if (it.name == editMatch.matchState.bowler.name) {
                    it.balls--
                    it.runs -= run.runs
                }
            }
        } else if (run.ballDesc == "W") {
            editMatch.battingTeam.players.forEach {
                if (it.name == editMatch.matchState.batsman1.name) {
                    it.runs -= run.runs
                    it.balls--
                    it.wicketReason = ""
                }
            }
            editMatch.bowlingTeam.players.forEach {
                if (it.name == editMatch.matchState.bowler.name) {
                    it.runs -= run.runs
                    it.balls--
                    it.wickets--
                }
            }
        } else {
            editMatch.bowlingTeam.players.forEach {
                if (it.name == editMatch.matchState.bowler.name) {
                    it.runs--
                    it.wickets--
                    it.extras--
                }
            }
            editMatch.matchState.ballNumber++
        }
        updateViews()
        repository.updateMatch(editMatch)
    }

    private fun updateRun(run: Int) {

        editMatch.matchState.run += run
        editMatch.matchState.ballNumber++

        editMatch.matchState.batsman1.runs += run
        editMatch.matchState.batsman1.balls++

        editMatch.matchState.bowler.runs += run
        editMatch.matchState.bowler.balls++

        editMatch.bowlingTimeline.add(BallAction(run, 0, ""))

        if (run % 2 == 1) {
            val temp = editMatch.matchState.batsman2
            editMatch.matchState.batsman2 = editMatch.matchState.batsman1
            editMatch.matchState.batsman1 = temp
        }

        if (editMatch.matchState.ballNumber % 6 == 0) {
            val temp = editMatch.matchState.batsman2
            editMatch.matchState.batsman2 = editMatch.matchState.batsman1
            editMatch.matchState.batsman1 = temp
            showBowlerDialog()

        }

        updateViews()
        repository.updateMatch(editMatch)

    }


    private fun showBowlerDialog() {

        val dialog = Dialog(requireContext())
        val dialogBinding = PlayerDialogBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(dialogBinding.root)

        dialog.setCancelable(false)


        dialogBinding.tv1.text = editMatch.bowlingTeam.players[0].name
        dialogBinding.tv2.text = editMatch.bowlingTeam.players[1].name
        dialogBinding.tv3.text = editMatch.bowlingTeam.players[2].name
        dialogBinding.tv4.text = editMatch.bowlingTeam.players[3].name
        dialogBinding.tv5.text = editMatch.bowlingTeam.players[4].name

        dialogBinding.tv1.setOnClickListener {
            editMatch.matchState.bowler = editMatch.bowlingTeam.players[0]
            updateViews()
            dialog.dismiss()
        }
        dialogBinding.tv2.setOnClickListener {
            editMatch.matchState.bowler = editMatch.bowlingTeam.players[1]
            updateViews()
            dialog.dismiss()
        }
        dialogBinding.tv3.setOnClickListener {
            editMatch.matchState.bowler = editMatch.bowlingTeam.players[2]
            updateViews()
            dialog.dismiss()
        }
        dialogBinding.tv4.setOnClickListener {
            editMatch.matchState.bowler = editMatch.bowlingTeam.players[3]
            updateViews()
            dialog.dismiss()
        }
        dialogBinding.tv5.setOnClickListener {
            editMatch.matchState.bowler = editMatch.bowlingTeam.players[4]
            updateViews()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showBatsmanDialog() {

        val dialog = Dialog(requireContext())
        val dialogBinding = PlayerDialogBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(dialogBinding.root)

        dialog.setCancelable(false)


        dialogBinding.tv1.text = editMatch.battingTeam.players[0].name
        dialogBinding.tv2.text = editMatch.battingTeam.players[1].name
        dialogBinding.tv3.text = editMatch.battingTeam.players[2].name
        dialogBinding.tv4.text = editMatch.battingTeam.players[3].name
        dialogBinding.tv5.text = editMatch.battingTeam.players[4].name

        dialogBinding.tv1.setOnClickListener {
            editMatch.matchState.batsman1 = editMatch.battingTeam.players[0]
            updateViews()
            dialog.dismiss()
        }
        dialogBinding.tv2.setOnClickListener {
            editMatch.matchState.batsman1 = editMatch.battingTeam.players[1]
            updateViews()
            dialog.dismiss()
        }
        dialogBinding.tv3.setOnClickListener {
            editMatch.matchState.batsman1 = editMatch.battingTeam.players[2]
            updateViews()
            dialog.dismiss()
        }
        dialogBinding.tv4.setOnClickListener {
            editMatch.matchState.batsman1 = editMatch.battingTeam.players[3]
            updateViews()
            dialog.dismiss()
        }
        dialogBinding.tv5.setOnClickListener {
            editMatch.matchState.batsman1 = editMatch.battingTeam.players[4]
            updateViews()
            dialog.dismiss()
        }
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    fun updateViews() {
        val match = editMatch
        if (editMatch.matchState.ballNumber >= 30) {
            gameOver()
            return
        }

        binding.gameBoardToolBar.text = match.battingTeam.name + " vs " + match.bowlingTeam.name

        binding.runWicket.text = "${match.matchState.run}/${match.matchState.wickets}"
        binding.overBalls.text =
            "${match.matchState.ballNumber / 6}.${match.matchState.ballNumber % 6}"

        if (match.matchState.ballNumber == 0)
            binding.requireRunRate.text =
                "CR - ${Math.round(match.matchState.run.toDouble() / (match.matchState.ballNumber + 1).toDouble()) * 6.000}"
        else binding.requireRunRate.text =
            "CR - ${Math.round(match.matchState.run.toDouble() / match.matchState.ballNumber.toDouble()) * 6.000}"

        binding.tvBatsmanOneName.text = match.matchState.batsman1.name
        binding.tvBatsmanTwoName.text = match.matchState.batsman2.name

        binding.tvBatsmanTwoRun.text =
            "${match.matchState.batsman2.runs}(${match.matchState.batsman2.balls})"
        binding.tvBatsmanOneRun.text =
            "${match.matchState.batsman1.runs}(${match.matchState.batsman1.balls})"

        binding.tvBallerName.text = match.matchState.bowler.name
        binding.ballerBalls.text =
            "(${match.matchState.bowler.balls / 6}.${match.matchState.bowler.balls % 6})"
        binding.ballerRuns.text = match.matchState.bowler.runs.toString()
        binding.ballerWickets.text = match.matchState.bowler.wickets.toString()

        val adapter = BallAdapter(
            requireContext(),
            match.bowlingTimeline
        )
        binding.ballRv.adapter = adapter
        binding.ballRv.scrollToPosition(match.bowlingTimeline.size - 1)
    }

    private fun gameOver() {
        editMatch.matchStatus = Constants.COMPLETED
        val dialog = Dialog(requireContext())
        val dialogBinding = MatchOverBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(dialogBinding.root)

        dialog.setCancelable(false)

        dialogBinding.submitButton.setOnClickListener {
            dialog.dismiss()
            repository.updateMatch(editMatch)
            findNavController().navigateUp()
        }
        dialog.show()
    }
}