package com.hannesdorfmann.conductor_shared_element_transition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bluelinelabs.conductor.Controller

/**
 *
 *
 * @author Hannes Dorfmann
 */
class DetailsController constructor(bundle: Bundle) : Controller(bundle) {

  companion object {
    fun newInstante(position: Int, color: Int): DetailsController {
      val bundle = Bundle();
      bundle.putInt("position", position)
      bundle.putInt("color", color)
      return DetailsController(bundle)
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    val position = args.getInt("position")
    val color = args.getInt("color")

    val view = inflater.inflate(R.layout.controller_details, container, false)
    val textView = view.findViewById(R.id.textView) as TextView
    textView.text = "Item $position"
    textView.setBackgroundColor(color);
    textView.transitionName = "Item$position"
    return view
  }
}