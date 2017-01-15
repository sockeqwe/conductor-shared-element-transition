package com.hannesdorfmann.conductor_shared_element_transition

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView

/**
 *
 *
 * @author Hannes Dorfmann
 */
class ListAdapter(val inflater: LayoutInflater, val clickListener: (position: Int, backgroundColor: Int) -> Unit) : RecyclerView.Adapter<ListAdapter.ItemViewHolder>() {


  private val itemsColor = arrayListOf(
      Color.argb(255, 123, 123, 123),
      Color.argb(255, 123, 19, 60),
      Color.argb(255, 250, 19, 60),
      Color.argb(255, 0, 255, 0),
      Color.argb(255, 255, 0, 0),
      Color.argb(255, 0, 0, 255),
      Color.argb(255, 90, 90, 90),
      Color.argb(255, 150, 150, 150),
      Color.argb(255, 10, 10, 10),
      Color.argb(255, 70, 10, 110),
      Color.argb(255, 20, 110, 110),
      Color.argb(255, 30, 30, 190)
  )
  var viewHolderPredrawedListener: ((Int, RecyclerView.ViewHolder) -> Unit)? = null

  override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
    holder.itemView.viewTreeObserver.addOnPreDrawListener(MyPreDrawListener(position, holder))
    holder.textView.text = "Item $position"
    holder.itemView.transitionName = "Item$position"
    holder.itemView.setBackgroundColor(itemsColor[position])
  }

  override fun getItemCount() = itemsColor.size

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder {
    return ItemViewHolder(inflater.inflate(R.layout.item, parent, false))
  }

  inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textView: TextView = itemView.findViewById(R.id.textView) as TextView

    init {
      itemView.setOnClickListener {
        clickListener(adapterPosition, itemsColor[adapterPosition])
      }
    }
  }

  inner class MyPreDrawListener(val position: Int, val viewHolder: RecyclerView.ViewHolder) : ViewTreeObserver.OnPreDrawListener {

    override fun onPreDraw(): Boolean {
      viewHolder.itemView.viewTreeObserver.removeOnPreDrawListener(this)
      viewHolderPredrawedListener?.invoke(position, viewHolder)

      // Returning false, so the view is not drawn the first time it is added to the view hierachy
      // This avoids flickering, because upon adding it to the view hierachy the android framework tries to draw it
      // Just setting the Visibility to INVISIBLE doesnt' work
      return false
    }
  }
}