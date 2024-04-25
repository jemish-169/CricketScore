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
import kotlin.math.max

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
        loadViews()
        updateViews(editMatch)
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    private fun loadViews() {
        binding.gameBoardToolBar.text =
            editMatch.battingTeam.name + " vs " + editMatch.bowlingTeam.name

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.overGame.setOnClickListener {
            editMatch.matchStatus = Constants.COMPLETED
            val dialog = Dialog(requireContext())
            val dialogBinding = MatchOverBinding.inflate(LayoutInflater.from(requireContext()))
            dialog.setContentView(dialogBinding.root)

            dialog.setCancelable(false)

            dialogBinding.submitButton.setOnClickListener {
                findNavController().navigateUp()
            }
            dialog.show()
        }
        binding.ballUnod.setOnClickListener {

            val run = editMatch.bowlingTimeline.last()

            if (binding.tvBatsmanOneName.text == "") {
                editMatch.matchState.batsman1 =
                    editMatch.battingTeam.players[editMatch.matchState.wickets - 1]
            }
            if (binding.tvBatsmanTwoName.text == "") {
                editMatch.matchState.batsman2 =
                    editMatch.battingTeam.players[editMatch.matchState.wickets - 2]
            }
            if (binding.tvBallerName.text == "") {
                editMatch.matchState.bowler =
                    editMatch.bowlingTeam.players[editMatch.matchState.wickets - 1]
            }

            if (run.ballDesc == "") {
                editMatch.matchState.run -= run.runs
                editMatch.matchState.ballNumber--

                editMatch.bowlingTimeline.removeAt(editMatch.bowlingTimeline.lastIndex)

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

                if (run.runs % 2 == 1) {
                    val temp = editMatch.matchState.batsman2
                    editMatch.matchState.batsman2 = editMatch.matchState.batsman1
                    editMatch.matchState.batsman1 = temp
                }

                if (editMatch.matchState.ballNumber % 6 == 0) {
                    val temp = editMatch.matchState.batsman2
                    editMatch.matchState.batsman2 = editMatch.matchState.batsman1
                    editMatch.matchState.batsman1 = temp
                }
            } else {
                if (run.ballDesc == "W") {
                    editMatch.matchState.ballNumber--
                    editMatch.battingTeam.players.forEach {
                        if (it.name == editMatch.matchState.batsman1.name) {
                            it.balls--
                            it.wicketReason = ""
                        }
                    }
                    editMatch.bowlingTeam.players.forEach {
                        if (it.name == editMatch.matchState.bowler.name) {
                            it.balls--
                            it.wickets--
                        }
                    }
                }
                editMatch.matchState.run -= run.runs

                editMatch.bowlingTimeline.removeAt(editMatch.bowlingTimeline.lastIndex)

                editMatch.battingTeam.players.forEach {
                    if (it.name == editMatch.matchState.batsman1.name) {
                        it.runs -= run.runs
                    }
                }
                editMatch.bowlingTeam.players.forEach {
                    if (it.name == editMatch.matchState.bowler.name) {
                        it.runs -= run.runs
                    }
                }
            }
            updateViews(editMatch)
            repository.updateMatch(editMatch)
        }

//        binding.tvBallerName.setOnClickListener {
//            if (editMatch.matchState.ballNumber % 6 == 0) {
//                showDialogBowler("Select Bowler")
//            }
//        }
//        binding.tvBatsmanOneName.setOnClickListener {
//            if (editMatch.matchState.batsman1.name == "") {
//                showDialogBatsmen1("Select BatsMan 1")
//            }
//        }
//        binding.tvBatsmanTwoName.setOnClickListener {
//            if (editMatch.matchState.batsman2.name == "") {
//                showDialogBatsmen2("Select BatsMan 2")
//            }
//        }

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
                    it.balls++
                    it.runs++
                }
            }
            editMatch.bowlingTimeline.add(BallAction(1, 0, "NB"))
            updateViews(editMatch)
            repository.updateMatch(editMatch)
        }
        binding.ballWide.setOnClickListener {
            editMatch.matchState.run++
            editMatch.matchState.bowler.runs++
            editMatch.matchState.bowler.extras++

            editMatch.bowlingTeam.players.forEach {
                if (it.name == editMatch.matchState.bowler.name) {
                    it.balls++
                    it.runs++
                }
            }
            editMatch.bowlingTimeline.add(BallAction(1, 0, "WD"))
            updateViews(editMatch)
            repository.updateMatch(editMatch)
        }
        binding.ballOut.setOnClickListener {
            editMatch.matchState.wickets++
            editMatch.matchState.ballNumber++
            editMatch.battingTeam.players.forEach {
                if (it.name == editMatch.matchState.batsman1.name) {
                    it.wicketReason = "Run Out"
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
                editMatch.matchStatus = Constants.COMPLETED
                val dialog = Dialog(requireContext())
                val dialogBinding = MatchOverBinding.inflate(LayoutInflater.from(requireContext()))
                dialog.setContentView(dialogBinding.root)

                dialog.setCancelable(false)

                dialogBinding.submitButton.setOnClickListener {
                    findNavController().navigateUp()
                }
                dialog.show()
            } else {
                editMatch.matchState.batsman1 =
                    editMatch.battingTeam.players[editMatch.matchState.wickets + 1]
            }
            if (editMatch.matchState.ballNumber % 6 == 0) {
                val temp = editMatch.matchState.batsman2
                editMatch.matchState.batsman2 = editMatch.matchState.batsman1
                editMatch.matchState.batsman1 = temp
            }
            updateViews(editMatch)
            repository.updateMatch(editMatch)
        }
    }

    private fun updateRun(run: Int) {

        if (binding.tvBatsmanOneName.text == "") {
            editMatch.matchState.batsman1 =
                editMatch.battingTeam.players[editMatch.matchState.wickets]
        }
        if (binding.tvBatsmanTwoName.text == "") {
            editMatch.matchState.batsman2 =
                editMatch.battingTeam.players[editMatch.matchState.wickets + 1]
        }
        if (binding.tvBallerName.text == "") {
            editMatch.matchState.bowler =
                editMatch.bowlingTeam.players[editMatch.matchState.wickets]
        }

        editMatch.matchState.run += run
        editMatch.matchState.ballNumber++

        editMatch.bowlingTimeline.add(BallAction(run, 0, ""))

        editMatch.battingTeam.players.forEach {
            if (it.name == editMatch.matchState.batsman1.name) {
                it.balls++
                it.runs += run
            }
        }
        editMatch.bowlingTeam.players.forEach {
            if (it.name == editMatch.matchState.bowler.name) {
                it.balls++
                it.runs += run
            }
        }

        if (run % 2 == 1) {
            val temp = editMatch.matchState.batsman2
            editMatch.matchState.batsman2 = editMatch.matchState.batsman1
            editMatch.matchState.batsman1 = temp
        }

        if (editMatch.matchState.ballNumber % 6 == 0) {
            val temp = editMatch.matchState.batsman2
            editMatch.matchState.batsman2 = editMatch.matchState.batsman1
            editMatch.matchState.batsman1 = temp

            editMatch.matchState.bowler =
                editMatch.bowlingTeam.players[editMatch.matchState.ballNumber / 6]
        }

        updateViews(editMatch)
        repository.updateMatch(editMatch)

    }

    private fun showDialogBatsmen1(s: String) {
        val dialog = Dialog(requireContext())
        val dialogBinding = PlayerDialogBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(dialogBinding.root)

        dialog.setCancelable(false)

        dialogBinding.selectPlayer.text = s

        dialogBinding.tv1.text = editMatch.bowlingTeam.players[0].name
        dialogBinding.tv2.text = editMatch.bowlingTeam.players[1].name
        dialogBinding.tv3.text = editMatch.bowlingTeam.players[2].name
        dialogBinding.tv4.text = editMatch.bowlingTeam.players[3].name
        dialogBinding.tv5.text = editMatch.bowlingTeam.players[4].name

        dialogBinding.tv1.setOnClickListener {
            editMatch.matchState.batsman1 = editMatch.battingTeam.players[0]
            dialog.dismiss()
        }
        dialogBinding.tv2.setOnClickListener {
            editMatch.matchState.batsman1 = editMatch.battingTeam.players[1]
            dialog.dismiss()
        }
        dialogBinding.tv3.setOnClickListener {
            editMatch.matchState.batsman1 = editMatch.battingTeam.players[2]
            dialog.dismiss()
        }
        dialogBinding.tv4.setOnClickListener {
            editMatch.matchState.batsman1 = editMatch.battingTeam.players[3]
            dialog.dismiss()
        }
        dialogBinding.tv5.setOnClickListener {
            editMatch.matchState.batsman1 = editMatch.battingTeam.players[4]
            dialog.dismiss()
        }
    }

    private fun showDialogBatsmen2(s: String) {
        val dialog = Dialog(requireContext())
        val dialogBinding = PlayerDialogBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(dialogBinding.root)

        dialog.setCancelable(false)

        dialogBinding.selectPlayer.text = s

        dialogBinding.tv1.text = editMatch.battingTeam.players[0].name
        dialogBinding.tv2.text = editMatch.battingTeam.players[1].name
        dialogBinding.tv3.text = editMatch.battingTeam.players[2].name
        dialogBinding.tv4.text = editMatch.battingTeam.players[3].name
        dialogBinding.tv5.text = editMatch.battingTeam.players[4].name

        dialogBinding.tv1.setOnClickListener {
            editMatch.matchState.batsman2 = editMatch.battingTeam.players[0]
            dialog.dismiss()
        }
        dialogBinding.tv2.setOnClickListener {
            editMatch.matchState.batsman2 = editMatch.battingTeam.players[1]
            dialog.dismiss()
        }
        dialogBinding.tv3.setOnClickListener {
            editMatch.matchState.batsman2 = editMatch.battingTeam.players[2]
            dialog.dismiss()
        }
        dialogBinding.tv4.setOnClickListener {
            editMatch.matchState.batsman2 = editMatch.battingTeam.players[3]
            dialog.dismiss()
        }
        dialogBinding.tv5.setOnClickListener {
            editMatch.matchState.batsman2 = editMatch.battingTeam.players[4]
            dialog.dismiss()
        }

    }

    private fun showDialogBowler(s: String) {

        val dialog = Dialog(requireContext())
        val dialogBinding = PlayerDialogBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(dialogBinding.root)

        dialog.setCancelable(false)

        dialogBinding.selectPlayer.text = s

        dialogBinding.tv1.text = editMatch.bowlingTeam.players[0].name
        dialogBinding.tv2.text = editMatch.bowlingTeam.players[1].name
        dialogBinding.tv3.text = editMatch.bowlingTeam.players[2].name
        dialogBinding.tv4.text = editMatch.bowlingTeam.players[3].name
        dialogBinding.tv5.text = editMatch.bowlingTeam.players[4].name

        dialogBinding.tv1.setOnClickListener {
            editMatch.matchState.bowler = editMatch.bowlingTeam.players[0]
            dialog.dismiss()
        }
        dialogBinding.tv2.setOnClickListener {
            editMatch.matchState.bowler = editMatch.bowlingTeam.players[1]
            dialog.dismiss()
        }
        dialogBinding.tv3.setOnClickListener {
            editMatch.matchState.bowler = editMatch.bowlingTeam.players[2]
            dialog.dismiss()
        }
        dialogBinding.tv4.setOnClickListener {
            editMatch.matchState.bowler = editMatch.bowlingTeam.players[3]
            dialog.dismiss()
        }
        dialogBinding.tv5.setOnClickListener {
            editMatch.matchState.bowler = editMatch.bowlingTeam.players[4]
            dialog.dismiss()
        }

    }

    @SuppressLint("SetTextI18n")
    fun updateViews(match: Match) {

        if (editMatch.matchState.ballNumber >= 30) {
            val dialog = Dialog(requireContext())
            val dialogBinding = MatchOverBinding.inflate(LayoutInflater.from(requireContext()))
            dialog.setContentView(dialogBinding.root)

            dialog.setCancelable(false)

            dialogBinding.submitButton.setOnClickListener {
                findNavController().navigateUp()
            }
            dialog.show()
        }

        binding.gameBoardToolBar.text = match.battingTeam.name + " vs " + match.bowlingTeam.name

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

        binding.tvBallerName.text = match.matchState.bowler.name
        binding.ballerBalls.text =
            "${match.matchState.bowler.balls / 6}.${match.matchState.bowler.balls % 6}"
        binding.ballerRuns.text = match.matchState.bowler.runs.toString()
        binding.ballerWickets.text = match.matchState.bowler.wickets.toString()

        val adapter = BallAdapter(
            requireContext(),
            match.bowlingTimeline.takeLast(max(match.bowlingTimeline.size % 7, 1))
        )
        binding.ballRv.adapter = adapter
    }

}