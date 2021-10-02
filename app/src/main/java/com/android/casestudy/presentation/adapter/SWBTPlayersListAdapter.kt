package com.android.casestudy.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.casestudy.R
import com.android.casestudy.data.modal.PlayersInfo
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.player_list_item.view.imgView
import kotlinx.android.synthetic.main.player_list_item.view.name
import kotlinx.android.synthetic.main.player_list_item.view.points

class SWBTPlayersListAdapter(val itemList: List<PlayersInfo?>) :
    RecyclerView.Adapter<PlayersInfoViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlayersInfoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.player_list_item, parent, false)
        return PlayersInfoViewHolder(itemView)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: PlayersInfoViewHolder, position: Int) {
        holder.run {
            itemList.getOrNull(position)?.let {
                bind(info = it)
            }
        }
    }
}

class PlayersInfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(info: PlayersInfo) {
        with(itemView) {
            info.let {
                name.text = it.name
                points.text = it.points.toString()
                Glide.with(this.context).load(it.icon).into(imgView)
            }
        }
    }
}
