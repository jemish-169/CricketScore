package com.practice.cricketscore.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.practice.cricketscore.R
import com.practice.cricketscore.adapter.MatchAdapter
import com.practice.cricketscore.databinding.FragmentHomeBinding
import com.practice.cricketscore.databinding.NameDialogBinding
import com.practice.cricketscore.models.Match
import com.practice.cricketscore.repository.Repository
import com.practice.cricketscore.view.activity.MainActivity

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var repository: Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        repository = (activity as MainActivity).getRepository()
        repository.getMatches(object : Repository.DataCallback {
            override fun onDataLoaded(dataList: ArrayList<Match>) {
                adaptData(dataList)
            }

            override fun onError(errorMessage: String?) {
                adaptData(ArrayList())
            }
        })
        adaptData(ArrayList())
        loadViews()
        return binding.root
    }

    private fun adaptData(dataList: ArrayList<Match>) {
        if (dataList.isNotEmpty()) {
            binding.matchRv.visibility = View.VISIBLE
            binding.noMatches.visibility = View.GONE
            val adapter = MatchAdapter(requireContext(), repository.getUserName(), dataList)
            binding.matchRv.adapter = adapter
            adapter.setOnClickListener(object : MatchAdapter.OnItemClickListener {
                override fun onClick(position: Int, model: Match) {
                    if (position == -1) {
                        repository.editMatch = model
                        findNavController().navigate(R.id.action_homeFragment_to_gameBoardFragment)
                    } else {
                        repository.match = model
                        findNavController().navigate(R.id.action_homeFragment_to_userScreenFragment)
                    }
                }
            })
        } else {
            binding.noMatches.visibility = View.VISIBLE
            binding.matchRv.visibility = View.GONE
        }
    }

    private fun loadViews() {
        binding.matchRv.setHasFixedSize(true)
        binding.swipeRefreshLayout.setOnRefreshListener {
            repository.getMatches(object : Repository.DataCallback {
                override fun onDataLoaded(dataList: ArrayList<Match>) {
                    binding.swipeRefreshLayout.isRefreshing = false
                    adaptData(dataList)
                }

                override fun onError(errorMessage: String?) {
                    binding.swipeRefreshLayout.isRefreshing = false
                    adaptData(ArrayList())
                }
            })
        }
        binding.fabCreateMatch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createGameFragment)
        }
        if (repository.getUserName().isBlank()) {
            val dialog = Dialog(requireContext())
            val dialogBinding = NameDialogBinding.inflate(LayoutInflater.from(requireContext()))
            dialog.setContentView(dialogBinding.root)

            dialogBinding.cancelButton.visibility = View.INVISIBLE
            dialog.setCancelable(false)
            dialogBinding.submitButton.setOnClickListener {
                if (dialogBinding.userNameEt.text.isNullOrEmpty()) {
                    dialogBinding.userNameTil.setErrorMessage("Name can not be empty!")
                } else {
                    repository.setUsername(dialogBinding.userNameEt.text.toString().trim())
                    dialog.dismiss()
                }
            }
            dialogBinding.userNameEt.doOnTextChanged { _, _, _, _ ->
                dialogBinding.userNameTil.setErrorMessage("")
            }
            dialog.show()
        }
    }

    private fun TextInputLayout.setErrorMessage(message: String) {
        error = message
        isErrorEnabled = message.isNotEmpty()
    }
}