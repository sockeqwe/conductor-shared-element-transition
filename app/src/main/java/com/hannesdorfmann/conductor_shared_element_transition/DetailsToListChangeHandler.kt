package com.hannesdorfmann.conductor_shared_element_transition

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.transition.AutoTransition
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.ControllerChangeHandler

/**
 *
 *
 * @author Hannes Dorfmann
 */
class DetailsToListChangeHandler(private var position: Int) : ControllerChangeHandler() {

  constructor() : this(0)

  private var changeListener: ControllerChangeCompletedListener? = null
  private var detailsView: View? = null
  private var listView: RecyclerView? = null
  private var container: ViewGroup? = null

  private var viewHoldersLaidOut = 0

  override fun saveToBundle(bundle: Bundle) {
    super.saveToBundle(bundle)
    bundle.putInt("POSITION", position)
  }

  override fun restoreFromBundle(bundle: Bundle) {
    super.restoreFromBundle(bundle)
    position = bundle.getInt("POSITION")
  }

  override fun performChange(container: ViewGroup, from: View?, to: View?, isPush: Boolean,
      changeListener: ControllerChangeCompletedListener) {

    if (from != null && to != null) {
      this.changeListener = changeListener
      this.detailsView = from
      this.listView = to as RecyclerView
      this.container = container

      // Find the index listView add the listView view
      for (i in 0..container.childCount - 1) {
        if (container.getChildAt(i) == from) {
          // Add the list view in the hierarchy after the details view,
          // so that both are in the container, but the details view is in the front and therefore
          // the list view is not visible because hidden behind details view
          container.addView(listView, i)
          break
        }
      }

    } else {
      // Not sure if this can ever be the case
      changeListener.onChangeCompleted()
    }
  }

  val onViewHolderLaidOut = fun(position: Int, viewHolder: RecyclerView.ViewHolder) {
    viewHoldersLaidOut++

    if (position != this.position) {
      if (viewHoldersLaidOut == listView?.childCount) {
        // If we do a screen orientation change, the target ViewHolder might not be visible anymore
        container?.removeView(detailsView)
        notifyTransitionEndAndCleanUp()
      }
      return
    }

    val t = AutoTransition()
    t.addListener(object : Transition.TransitionListener {
      override fun onTransitionEnd(transition: Transition?) {
        notifyTransitionEndAndCleanUp()
      }

      override fun onTransitionResume(transition: Transition?) {
      }

      override fun onTransitionPause(transition: Transition?) {
      }

      override fun onTransitionCancel(transition: Transition?) {
        notifyTransitionEndAndCleanUp()
      }

      override fun onTransitionStart(transition: Transition?) {
      }
    })

    //viewHolder.itemView.visibility = View.GONE
    listView?.removeView(viewHolder.itemView)
    TransitionManager.beginDelayedTransition(container, t)
    container?.removeView(detailsView)
    listView?.addView(viewHolder.itemView)

  }

  fun notifyTransitionEndAndCleanUp() {
    // To avoid memory leaks
    detailsView = null
    listView = null
    container = null
    changeListener?.onChangeCompleted()
    changeListener = null
    viewHoldersLaidOut = 0
  }
}