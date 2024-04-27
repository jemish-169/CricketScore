package com.practice.cricketscore.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.practice.cricketscore.adapter.BatsmanAdapter
import com.practice.cricketscore.adapter.BowlerAdapter
import com.practice.cricketscore.databinding.FragmentCreateGameBinding
import com.practice.cricketscore.databinding.LoadingDialogBinding
import com.practice.cricketscore.models.Match
import com.practice.cricketscore.repository.Repository
import com.practice.cricketscore.utils.Constants
import com.practice.cricketscore.view.activity.MainActivity

class CreateGameFragment : Fragment() {

    private lateinit var binding: FragmentCreateGameBinding
    private val batsmanList = Constants.getBatsmanList()
    private val bowlerList = Constants.getBowlerList()
    private lateinit var repository: Repository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateGameBinding.inflate(layoutInflater)
        repository = (activity as MainActivity).getRepository()
        loadViews()
        return binding.root
    }

    private fun loadViews() {
        val match = Match()
        val batAdapter = BatsmanAdapter(requireContext(), batsmanList)
        val bowlAdapter = BowlerAdapter(requireContext(), bowlerList)
        binding.battingTeamRv.adapter = batAdapter
        binding.bowlTeamRv.adapter = bowlAdapter

        binding.teamConfirmButton.setOnClickListener {
            if (binding.batTeamNameEt.text.isNullOrEmpty()) {
                binding.batTeamNameTil.setErrorMessage("Name can not be empty!")
                return@setOnClickListener
            }
            if (binding.bowlTeamNameEt.text.isNullOrEmpty()) {
                binding.bowlTeamNameTil.setErrorMessage("Name can not be empty!")
                return@setOnClickListener
            }

            match.bowlingTeam.players.forEach {
                if (it.name.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Player name can not be empty!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            }
            match.battingTeam.players.forEach {
                if (it.name.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Player name can not be empty!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            }
            val batTeamName = binding.batTeamNameEt.text.toString().trim()
            val bowlTeamName = binding.bowlTeamNameEt.text.toString().trim()

            match.battingTeam.name = batTeamName
            match.bowlingTeam.name = bowlTeamName

            match.battingTeam.players = batsmanList
            match.bowlingTeam.players = bowlerList

            val dialog = showDialog()
            dialog.show()
            repository.updateMatch(match)
            dialog.dismiss()

            findNavController().navigateUp()
        }

        binding.batTeamNameEt.doOnTextChanged { _, _, _, _ ->
            removeError()
        }
        binding.bowlTeamNameEt.doOnTextChanged { _, _, _, _ ->
            removeError()
        }
    }

    private fun showDialog(): Dialog {
        val dialog = Dialog(requireContext())
        val dialogBinding = LoadingDialogBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)

        dialog.setCancelable(false)
        return dialog
    }

    private fun removeError() {
        binding.batTeamNameTil.setErrorMessage("")
        binding.bowlTeamNameTil.setErrorMessage("")
    }

    private fun TextInputLayout.setErrorMessage(message: String) {
        error = message
        isErrorEnabled = message.isNotEmpty()
    }
}