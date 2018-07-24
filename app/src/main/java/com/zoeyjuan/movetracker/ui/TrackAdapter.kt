package com.zoeyjuan.movetracker.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.zoeyjuan.movetracker.R
import com.zoeyjuan.movetracker.database.UserLocation
import com.zoeyjuan.movetracker.ui.util.DateTimeMapper
import com.zoeyjuan.movetracker.ui.util.StaticMapInfoGenerator

class TrackAdapter(private val layoutInflater: LayoutInflater,
                   private val staticMapGenerator: StaticMapInfoGenerator,
                   private val items: MutableList<Pair<Long, MutableList<UserLocation>>>,
                   private val picasso: Picasso)
    : RecyclerView.Adapter<TrackHolder>() {

    private val trackMap = mutableMapOf<Long, MutableList<UserLocation>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        return TrackHolder(layoutInflater.inflate(R.layout.track_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        onBindViewHolder(holder, position, mutableListOf())
    }

    /**
     *  it's not a best design here ue to time limited.
     *  The view should design as can update partially
     */
    override fun onBindViewHolder(holder: TrackHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            val geoPointList = staticMapGenerator.getGeoPointLitst(items[position].second)

            holder.loading.visibility = View.VISIBLE
            holder.error.visibility = View.GONE
            holder.itemName.text=
                    "Start: ${DateTimeMapper().map(items.get(position).second.first().trackTime)}\n" +
                    "End: ${DateTimeMapper().map(items.get(position).second.last().trackTime)}"

            picasso.load(staticMapGenerator.createUrlStr(geoPointList))
                    .centerInside()
                    .fit()
                    .into(holder.itemImg, object : Callback {
                        override fun onSuccess() {
                            holder.loading.visibility = View.GONE
                        }

                        override fun onError(e: Exception) {
                            holder.loading.visibility = View.GONE
                            holder.error.visibility = View.VISIBLE
                        }
                    })
        } else {
            // TODO Optional: partial update
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateList(list: MutableList<UserLocation>) {
        list.forEach {
                if (trackMap.containsKey(it.timeStamp)) {
                    trackMap.get(it.timeStamp)?.add(it)
                } else {
                    trackMap.put(it.timeStamp, mutableListOf(it))
                }
        }
        items.clear()
        items.addAll(trackMap.toList().sortedByDescending { it.first })
    }
}
