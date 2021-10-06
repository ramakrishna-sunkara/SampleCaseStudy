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

    private lateinit var itemListener: ItemListener

    interface ItemListener {
        fun onSelectCurrency(currency: String)
    }

    fun setItemListener(itemListener: ItemListener) {
        this.itemListener = itemListener
    }

    override fun getItemCount(): Int = currencies.size

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.run {
            currencies.getOrNull(position)?.let {
                bind(info = it, selectedCurrency, itemListener)
            }
        }
    }
}

class CurrencyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(info: String, selectedCurrency: String?, itemListener: CurrencyListAdapter.ItemListener) {
        with(itemView) {
            info.let { currency ->
                txtCurrency.text = currency
                selectedCurrency?.let {
                    if (it == currency) {
                        imgCheck.visibility = View.VISIBLE
                    } else {
                        imgCheck.visibility = View.INVISIBLE
                    }
                }
            }
            itemView.setOnClickListener {
                itemListener.let {
                    itemListener.onSelectCurrency(info)
                }
            }
        }
    }
}
