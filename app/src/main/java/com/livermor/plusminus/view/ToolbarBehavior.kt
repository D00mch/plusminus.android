package com.livermor.plusminus.view

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.behaviorule.arturdumchev.library.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.livermor.plusminus.R
import com.livermor.plusminus.TAG
import kotlinx.android.synthetic.main.activity_scrolling.view.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.collections.immutable.persistentListOf

class ToolbarBehavior (
    context: Context?,
    attrs: AttributeSet?
) : BehaviorByRules(context, attrs) {
    
    override fun View.provideAppbar(): AppBarLayout = ablAppbar
    override fun View.provideCollapsingToolbar(): CollapsingToolbarLayout = ctlToolbar
    override fun calcAppbarHeight(child: View): Int =
        (child.height + child.pixels(R.dimen.toolbar_height)).toInt()

    override fun View.setUpViews(): List<RuledView> {
        Log.i(TAG(), "setUpViews")
        return listOf(
            RuledView(
                iTopDetails,
                BRuleYOffset(
                    min = 0f,
                    max = pixels(R.dimen.toolbar_height)
                )
            )
        )
    }
}