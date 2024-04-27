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
        refreshListener()
        adaptData(ArrayList())
        loadViews()
        return binding.root
    }

    private fun adaptData(dataList: ArrayList<Match>) {
        if (dataList.isNotEmpty()) {
            binding.matchRv.visibility = View.VISIBLE
            binding.noMatches.visibility = View.GONE
            val adapter = MatchAdapter(requireContext(), dataList)
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
           refreshListener()
        }
        binding.fabCreateMatch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createGameFragment)
        }
    }

    private fun refreshListener() {
        binding.swipeRefreshLayout.isRefreshing = true
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

    private fun TextInputLayout.setErrorMessage(message: String) {
        error = message
        isErrorEnabled = message.isNotEmpty()
    }
}