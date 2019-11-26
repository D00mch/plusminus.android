package com.livermor.plusminus.screen.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import trikita.anvil.RenderableView
import trikita.anvil.DSL.*

class DataScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = object : RenderableView(container?.context) {
        override fun view() {
            linearLayout {
                size(MATCH, MATCH)
                orientation(LinearLayout.VERTICAL)
                button {
                    text("DATA")
                    onClick {}
                }
            }
        }
    }
}
