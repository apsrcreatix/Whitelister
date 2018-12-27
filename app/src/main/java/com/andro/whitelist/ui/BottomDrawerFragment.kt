package com.andro.whitelist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.andro.whitelist.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_drawer.*

class BottomDrawerFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_drawer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        navigation_view.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav1 -> Toast.makeText(context, "Clicked 1", Toast.LENGTH_LONG).show()
                R.id.nav2 -> Toast.makeText(context, "Clicked 2", Toast.LENGTH_LONG).show()
                R.id.nav3 -> Toast.makeText(context, "Clicked 3", Toast.LENGTH_LONG).show()
                R.id.nav4 -> Toast.makeText(context, "Clicked 4", Toast.LENGTH_LONG).show()
            }
            true
        }
    }
}