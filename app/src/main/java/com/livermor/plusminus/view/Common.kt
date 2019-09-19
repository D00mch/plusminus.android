package com.livermor.plusminus.view

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.livermor.plusminus.App.Companion.context
import com.livermor.plusminus.AppDb
import com.livermor.plusminus.R
import com.livermor.plusminus.screen.Screen
import trikita.anvil.Anvil
import trikita.anvil.BaseDSL.weight
import trikita.anvil.DSL.*

val iconSize = dip(44)

inline fun bottomIcon(
    screen: Screen, @DrawableRes active: Int,
    crossinline onClick: (Screen) -> Unit
) {
    val curScreen = AppDb.screen
    imageView {
        size(iconSize, iconSize)
        imageResource(active)
        scaleType(ImageView.ScaleType.FIT_CENTER)
        if (curScreen == screen) {
            Anvil.currentView<ImageView>()
                .setColorFilter(ContextCompat.getColor(context, R.color.blue))
        }
        onClick { onClick(screen) }
        weight(1f)
    }
}