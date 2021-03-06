package com.android.casestudy.presentation.view
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.casestudy.data.local.IPreferenceHelper
import com.android.casestudy.data.local.PreferenceManager
import com.android.casestudy.databinding.FragmentCurrencySelectionBinding
import com.android.casestudy.presentation.adapter.CurrencyListAdapter
import com.android.casestudy.presentation.adapter.CurrencyListAdapter.ItemListener
import com.android.casestudy.util.Constants
import com.android.casestudy.util.DataCache
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CurrencySelectionFragment : BottomSheetDialogFragment(), View.OnClickListener {
    private var mListener: ItemClickListener? = null
    private var selectedCurrency: String? = null
    private lateinit var binding: FragmentCurrencySelectionBinding
    val preferenceHelper: IPreferenceHelper by lazy { PreferenceManager(requireActivity()) }

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
        selectedCurrency = arguments?.getString(Constants.EXTRA_SELECTED_CURRENCY).toString()
    }

    interface ItemClickListener {
        fun onItemClick(item: String?)
    }

    private fun setAdapter() {
        val adapter = CurrencyListAdapter(preferenceHelper.getCurrencyList() as ArrayList<String>, selectedCurrency)
        adapter.setItemListener(object : ItemListener{
            override fun onSelectCurrency(currency: String) {
                mListener!!.onItemClick(currency)
                dismiss()
            }
        })
        binding.currencyRecyclerView.adapter = adapter
    }

    companion object {
        const val TAG = "CurrencySelectionFragment"
        fun newInstance(selectedCurrency: String): CurrencySelectionFragment {
            val bundle = Bundle()
            bundle.putString(Constants.EXTRA_SELECTED_CURRENCY, selectedCurrency)
            val currencySelectionFragment = CurrencySelectionFragment()
            currencySelectionFragment.arguments = bundle
            return currencySelectionFragment
        }
    }
}