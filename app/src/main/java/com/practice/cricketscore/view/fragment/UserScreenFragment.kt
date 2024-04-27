package com.practice.cricketscore.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.practice.cricketscore.adapter.BallAdapter
import com.practice.cricketscore.databinding.FragmentUserScreenBinding
import com.practice.cricketscore.models.Match
import com.practice.cricketscore.repository.Repository
import com.practice.cricketscore.view.activity.MainActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

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
        binding.shareGame.setOnClickListener {
            val image = binding.main
            val bitmap = Bitmap.createBitmap(
                image.width, image.height, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            image.draw(canvas)

            try {
                val cachePath = File(requireActivity().applicationContext.cacheDir, "images")
                cachePath.mkdirs()
                val stream =
                    FileOutputStream("$cachePath/image.png")
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.close()

                val newFile = File(cachePath, "image.png")
                val contentUri =
                    FileProvider.getUriForFile(
                        requireActivity().applicationContext,
                        "com.practice.cricketscore.fileProvider",
                        newFile
                    )

                if (contentUri != null) {
                    val shareIntent = Intent()
                    shareIntent.setAction(Intent.ACTION_SEND)
                    shareIntent.setType("image/png")
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
                    startActivity(Intent.createChooser(shareIntent, "Choose an app"))
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
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
                if (batsman.balls == 0) 0.0.toString() else (Math.round(batsman.runs.toDouble() / batsman.balls.toDouble()) * 100.0).toString()

        }

        for (i in 1..5) {
            val row = binding.bowlingTable.getChildAt(i) as TableRow

            val bowler = match.bowlingTeam.players[i - 1]

            (row.getChildAt(0) as TextView).text = bowler.name
            (row.getChildAt(1) as TextView).text = bowler.runs.toString()
            (row.getChildAt(2) as TextView).text =
                "${bowler.balls / 6}.${bowler.balls % 6}"

            (row.getChildAt(3) as TextView).text = bowler.wickets.toString()
            (row.getChildAt(4) as TextView).text =
                if (bowler.balls == 0) 0.0.toString() else (Math.round(bowler.runs.toDouble() / bowler.balls.toDouble()) * 6.0).toString()
            (row.getChildAt(5) as TextView).text = bowler.extras.toString()
        }

        val adapter = BallAdapter(
            requireContext(),
            match.bowlingTimeline
        )
        binding.ballRv.adapter = adapter
        binding.ballRv.scrollToPosition(match.bowlingTimeline.size - 1)
    }

}