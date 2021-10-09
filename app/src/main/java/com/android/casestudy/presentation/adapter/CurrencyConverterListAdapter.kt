package com.android.casestudy.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.casestudy.R
import com.android.casestudy.data.modal.CurrencyConvertedInfo
import com.android.casestudy.util.Constants
import kotlinx.android.synthetic.main.converter_list_item.view.*

class CurrencyConverterListAdapter(val itemList: List<CurrencyConvertedInfo?>) :
    RecyclerView.Adapter<CurrencyConverterViewHolder>() {
    private var count: Double = 1.00
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrencyConverterViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.converter_list_item, parent, false)
        return CurrencyConverterViewHolder(itemView)
    }

    fun setQuantity(quantity: Double){
        count = quantity
    }
    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: CurrencyConverterViewHolder, position: Int) {
        holder.run {
            itemList.getOrNull(position)?.let {
                bind(convertedInfo = it, count)
            }
        }
    }
}

class CurrencyConverterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(convertedInfo: CurrencyConvertedInfo, count: Double) {
        with(itemView) {
            convertedInfo.let {
                txtCountry.text = it.countryName
                txtCurrency.text = Constants.FOUR_DECIMAL.format(it.currencyValue * count)
            }
        }
    }
}
