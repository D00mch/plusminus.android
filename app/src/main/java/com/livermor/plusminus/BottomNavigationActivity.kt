package com.livermor.plusminus

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.livermor.plusminus.screen.MultiplayerFragment
import com.livermor.plusminus.screen.SettingsFragment
import com.livermor.plusminus.screen.SinglePlayerFragment

class BottomNavigationActivity : AppCompatActivity() {

    private val single: Fragment by lazy { SinglePlayerFragment() }
    private val settings: Fragment by lazy { SettingsFragment() }
    private val multiplayer: Fragment by lazy { MultiplayerFragment() }
    private var active: Fragment = single
    private val fm: FragmentManager by lazy { supportFragmentManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        fm.beginTransaction()
            .add(R.id.main_container, multiplayer, "3").hide(multiplayer)
            .add(R.id.main_container, settings, "3").hide(settings)
            .add(R.id.main_container, single, "3")
            .commit()

        navView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    fm.beginTransaction().hide(active).show(single).commit()
                    active = single
                    true
                }
                R.id.navigation_dashboard -> {
                    fm.beginTransaction().hide(active).show(settings).commit()
                    active = settings
                    true
                }
                R.id.navigation_notifications -> {
                    fm.beginTransaction().hide(active).show(multiplayer).commit()
                    active = multiplayer
                    true
                }
                else -> false
            }
        }
    }
}
