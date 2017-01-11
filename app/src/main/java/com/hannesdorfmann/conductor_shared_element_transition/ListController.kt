package com.hannesdorfmann.conductor_shared_element_transition

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import com.bluelinelabs.conductor.RouterTransaction

/**
 *
 *
 * @author Hannes Dorfmann
 */
class ListController() : Controller() {

  private var adapter: ListAdapter? = null

  override fun onChangeStarted(changeHandler: ControllerChangeHandler,
      changeType: ControllerChangeType) {
    super.onChangeStarted(changeHandler, changeType)

    if (changeHandler is DetailsToListChangeHandler) {
      adapter?.viewHolderPredrawedListener = changeHandler.onViewHolderLaidOut
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    val view = inflater.inflate(R.layout.controller_list, container, false)
    val recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
    recyclerView.layoutManager = LinearLayoutManager(activity)
    adapter = ListAdapter(activity!!.layoutInflater) { position, color ->
      val transaction = RouterTransaction.with(DetailsController.newInstante(position, color))
          .pushChangeHandler(ListToDetailsChangeHandler())
          .popChangeHandler(DetailsToListChangeHandler(position))

      router.pushController(transaction)
    }
    recyclerView.adapter = adapter!!
    return view
  }

  override fun onDestroyView(view: View) {
    super.onDestroyView(view)
    adapter = null // Avoid memory leaks
  }

}