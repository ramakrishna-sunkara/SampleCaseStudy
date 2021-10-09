package com.android.casestudy.presentation.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.casestudy.data.modal.CurrencyConvertedInfo
import com.android.casestudy.databinding.ActivityCurrencyConverterBinding
import com.android.casestudy.presentation.adapter.CurrencyConverterListAdapter
import com.android.casestudy.presentation.vm.SWBTViewModel
import com.android.casestudy.states.ConverterState
import com.android.casestudy.util.Constants
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CurrencyConverterActivity : AppCompatActivity(), CurrencySelectionFragment.ItemClickListener {

    private val viewModel: SWBTViewModel by viewModels()
    private lateinit var binding: ActivityCurrencyConverterBinding
    private var adapter: CurrencyConverterListAdapter? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrencyConverterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addObservers()
        init()
    }

    private fun init(){
        viewModel.setSelectedCurrency(Constants.DEFAULT_CURRENCY)
        viewModel.loadConvertCurrencyData()
        binding.txtResult.setOnClickListener {
            viewModel.selectedCurrency.value?.let {
                showBottomSheet(it)
            }
        }
        binding.etAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.toString().isEmpty()) {
                    adapter?.setQuantity(1.00)
                    adapter?.notifyDataSetChanged()
                }
                else {
                    adapter?.setQuantity(s.toString().toDouble())
                    adapter?.notifyDataSetChanged()
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun addObservers() {
        viewModel.converterState.observe(this, {
            when (it) {
                is ConverterState.InitialLoading -> {
                    //binding.progressBar.visibility = View.VISIBLE
                    //binding.currencyRecyclerView.visibility = View.GONE
                }
                is ConverterState.ApiError -> {
                    //binding.currencyRecyclerView.visibility = View.GONE
                    //binding.progressBar.visibility = View.GONE
                    //binding.errorTv.visibility = View.VISIBLE
                    //binding.errorTv.text = it.errorMessage
                }
                is ConverterState.Success -> {
                    viewModel.setSelectedCurrency(it.convertedCurrency.currency)
                    setAdapter(it.convertedCurrency.currencyConvertedInfoList)
                }
                else -> Unit
            }
        })

        viewModel.selectedCurrency.observe(this, {
            it.let { binding.txtResult.text = it }
        })
    }

    private fun setAdapter(response: List<CurrencyConvertedInfo>) {
        binding.currencyRecyclerView.visibility = View.VISIBLE
        adapter = CurrencyConverterListAdapter(itemList = response)
        binding.currencyRecyclerView.adapter = adapter
    }

    fun showBottomSheet(selectedCurrency: String) {
        val currencySelectionFragment: CurrencySelectionFragment =
            CurrencySelectionFragment.newInstance(selectedCurrency)
        currencySelectionFragment.show(
            supportFragmentManager,
            CurrencySelectionFragment.TAG
        )
    }

    override fun onItemClick(item: String?) {
        viewModel.setSelectedCurrency(item)
        viewModel.loadConvertCurrencyData()
    }
}