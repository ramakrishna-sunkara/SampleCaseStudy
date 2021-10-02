package com.android.casestudy.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.android.casestudy.data.modal.PlayersInfo
import com.android.casestudy.databinding.ActivityMainBinding
import com.android.casestudy.presentation.adapter.SWBTPlayersListAdapter
import com.android.casestudy.presentation.vm.SWBTViewModel
import com.android.casestudy.states.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayersActivity : AppCompatActivity() {

    private val viewModel: SWBTViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getStarWarsTournamentDetails()
        viewModel.uiState.observe(this, {
            when (it) {
                is UiState.InitialLoading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.currencyRecyclerView.visibility = View.GONE
                }
                is UiState.ApiError -> {
                    binding.currencyRecyclerView.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.errorTv.visibility = View.VISIBLE
                    binding.errorTv.text = it.errorMessage
                }
                is UiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.errorTv.visibility = View.GONE
                    setAdapter(it.response)
                }
                else -> Unit
            }
        })
    }

    private fun setAdapter(response: List<PlayersInfo?>) {
        binding.currencyRecyclerView.visibility = View.VISIBLE
        binding.currencyRecyclerView.adapter =
            SWBTPlayersListAdapter(itemList = response)
    }
}