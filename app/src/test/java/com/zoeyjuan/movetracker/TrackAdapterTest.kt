package com.zoeyjuan.movetracker

import android.view.LayoutInflater
import android.view.View
import org.junit.Test

import org.junit.Assert.*
import android.widget.TextView
import android.view.ViewGroup
import android.widget.ImageView
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.zoeyjuan.movetracker.database.UserLocation
import com.zoeyjuan.movetracker.ui.TrackAdapter
import com.zoeyjuan.movetracker.ui.util.StaticMapInfoGenerator
import org.junit.Before
import android.widget.ProgressBar
import com.nhaarman.mockitokotlin2.any
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import com.zoeyjuan.movetracker.ui.TrackHolder
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.mockito.internal.matchers.Equality.areEqual
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class) // for giving context to mock Picasso
class TrackAdapterTest {
    private lateinit var parent: ViewGroup
    private lateinit var itemView: View
    private lateinit var layoutInflater: LayoutInflater
    private lateinit var staticMapInfoGenerator: StaticMapInfoGenerator
    private lateinit var pairItems: MutableList<Pair<Long, MutableList<UserLocation>>>
    private lateinit var picasso: Picasso

    @Before
    fun setup() {
        parent = mock(ViewGroup::class.java)
        itemView = mock(View::class.java)
        layoutInflater = mock(LayoutInflater::class.java)
        staticMapInfoGenerator = mock(StaticMapInfoGenerator::class.java)
        pairItems = mock(MutableList::class.java) as MutableList<Pair<Long, MutableList<UserLocation>>>
        picasso = mock(Picasso::class.java)
    }

    @Test
    fun test_onCreateViewHolder() {
        // Arrange
        whenever(layoutInflater.inflate(R.layout.track_list_item, parent, false)).thenReturn(itemView)
        val trackAdapter = TrackAdapter(layoutInflater, staticMapInfoGenerator, pairItems, picasso)
        // Act
        val viewHolder = trackAdapter.onCreateViewHolder(parent, 0)

        // Assert
        verify(layoutInflater).inflate(R.layout.track_list_item, parent, false)
        assertEquals(viewHolder.itemView, itemView)
    }

    @Test
    fun test_bindViewHolder() {
        // Arrange
        whenever(pairItems[0]).thenReturn(Pair(1, mutableListOf(UserLocation())))
        val itemViewHolder = mock(TrackHolder::class.java)
        whenever(itemViewHolder.loading).thenReturn(mock(ProgressBar::class.java))
        whenever(itemViewHolder.error).thenReturn(mock(ImageView::class.java))
        whenever(itemViewHolder.itemName).thenReturn(mock(TextView::class.java))
        whenever(itemViewHolder.itemImg).thenReturn(mock(ImageView::class.java))
        whenever(staticMapInfoGenerator.createUrlStr(any())).thenReturn("URL")
        whenever(staticMapInfoGenerator.getGeoPointLitst(any())).thenReturn(arrayOf())

        val requestCreator = mock(RequestCreator::class.java)
        whenever(requestCreator.centerInside()).thenReturn(requestCreator)
        whenever(requestCreator.fit()).thenReturn(requestCreator)
        whenever(picasso.load("URL")).thenReturn(requestCreator)

        val trackAdapter = TrackAdapter(layoutInflater, staticMapInfoGenerator, pairItems, picasso)

        // Act
        trackAdapter.onBindViewHolder(itemViewHolder, 0)

        // Assert
        verify(itemViewHolder.loading).visibility = View.VISIBLE
        verify(itemViewHolder.error).visibility = View.GONE
        verify(itemViewHolder.itemName).text = ArgumentMatchers.anyString()

        verify(staticMapInfoGenerator).createUrlStr(any())
        verify(itemViewHolder).itemImg
        verify(picasso).load("URL")
    }

    @Test
    fun test_getItemCount() {
        // Arrange
        val items = mutableListOf (
                UserLocation(33, 2, 112.0, 12.0),
                UserLocation(44, 2, 112.0, 12.0),
                UserLocation(55, 3, 112.0, 12.0),
                UserLocation(22, 1, 112.0, 12.0),
                UserLocation(11, 1, 112.0, 12.0)
        )
        val pairItemList = mutableListOf<Pair<Long, MutableList<UserLocation>>>()
        val adapter = TrackAdapter(layoutInflater, staticMapInfoGenerator, pairItemList, picasso)
        adapter.updateList(items)
        // Act
        val size = adapter.itemCount

        // Assert
        assertEquals(3, size)
    }

    @Test
    fun test_updateList() {
        // Arrange
        val items = mutableListOf (
                UserLocation(33, 2, 112.0, 12.0),
                UserLocation(44, 2, 112.0, 12.0),
                UserLocation(55, 3, 112.0, 12.0),
                UserLocation(22, 1, 112.0, 12.0),
                UserLocation(11, 1, 112.0, 12.0)
        )
        val pairItemList = mutableListOf<Pair<Long, MutableList<UserLocation>>>()
        val adapter = TrackAdapter(layoutInflater, staticMapInfoGenerator, pairItemList, picasso)
        // Act
        adapter.updateList(items)

        // Assert
        areEqual(
            mutableListOf (
                Pair(3, mutableListOf(UserLocation(55, 3, 112.0, 12.0))),
                Pair(2, mutableListOf(UserLocation(33, 2, 112.0, 12.0),
                                      UserLocation(44, 2, 112.0, 12.0))),
                Pair(1, mutableListOf(UserLocation(22, 1, 112.0, 12.0),
                                      UserLocation(11, 1, 112.0, 12.0)))
                ), pairItemList)
    }
}
