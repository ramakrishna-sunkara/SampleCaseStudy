package com.android.casestudy.presentation.view
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.casestudy.R
import com.android.casestudy.data.modal.CurrencyInfo
import com.android.casestudy.databinding.ActivityCurrencyConverterBinding
import com.android.casestudy.databinding.FragmentCurrencySelectionBinding
import com.android.casestudy.presentation.adapter.CurrencyConverterListAdapter
import com.android.casestudy.presentation.adapter.CurrencyListAdapter
import com.android.casestudy.util.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CurrencySelectionFragment : BottomSheetDialogFragment(), View.OnClickListener {
    private var mListener: ItemClickListener? = null
    private var selectedCurrency: String? = null
    private var currencies = ArrayList<String>()
    private lateinit var binding: FragmentCurrencySelectionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrencySelectionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is ItemClickListener) {
            context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement ItemClickListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onClick(view: View) {
        val tvSelected = view as TextView
        mListener!!.onItemClick(tvSelected.text.toString())
        dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currencies = arguments?.getSerializable(Constants.EXTRA_CURRENCY_LIST) as ArrayList<String>;
        selectedCurrency = arguments?.getString(Constants.EXTRA_SELECTED_CURRENCY).toString()
    }

    interface ItemClickListener {
        fun onItemClick(item: String?)
    }

    private fun setAdapter() {
        binding.currencyRecyclerView.adapter =
            CurrencyListAdapter(currencies, selectedCurrency)
    }

    companion object {
        const val TAG = "CurrencySelectionFragment"
        fun newInstance(currencies: ArrayList<String>, selectedCurrency: String): CurrencySelectionFragment {
            val bundle = Bundle()
            bundle.putSerializable(Constants.EXTRA_CURRENCY_LIST, currencies)
            bundle.putString(Constants.EXTRA_SELECTED_CURRENCY, selectedCurrency)
            val currencySelectionFragment = CurrencySelectionFragment()
            currencySelectionFragment.arguments = bundle
            return currencySelectionFragment
        }
    }
}