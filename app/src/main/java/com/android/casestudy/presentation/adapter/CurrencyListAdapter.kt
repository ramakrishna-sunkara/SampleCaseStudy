package com.android.casestudy.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.casestudy.R
import kotlinx.android.synthetic.main.converter_list_item.view.txtCurrency
import kotlinx.android.synthetic.main.currency_list_item.view.*

class CurrencyListAdapter(val currencies: ArrayList<String>, val selectedCurrency: String?) :
    RecyclerView.Adapter<CurrencyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrencyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.currency_list_item, parent, false)
        return CurrencyViewHolder(itemView)
    }

    override fun getItemCount(): Int = currencies.size

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.run {
            currencies.getOrNull(position)?.let {
                bind(info = it, selectedCurrency)
            }
        }
    }
}

class CurrencyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(info: String, selectedCurrency: String?) {
        with(itemView) {
            info.let { currency ->
                txtCurrency.text = currency
                selectedCurrency?.let {
                    if (it === currency) {
                        imgCheck.visibility = View.VISIBLE
                    } else {
                        imgCheck.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }
}
