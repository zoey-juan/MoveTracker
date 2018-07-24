package com.zoeyjuan.movetracker.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.track_list_item.view.*

class TrackHolder(view: View) : RecyclerView.ViewHolder(view) {
    val itemName = view.item_name
    val itemImg = view.img_item
    val loading = view.loadingView
    val error = view.errorView
}