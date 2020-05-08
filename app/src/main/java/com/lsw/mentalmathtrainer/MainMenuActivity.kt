package com.lsw.mentalmathtrainer

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.content_main_menu.*

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        buttonKlassischerModus.setOnClickListener {
            var intent = Intent(applicationContext, LevelAuswahlActivity::class.java)
            intent.putExtra("modus", "klassisch")
            startActivity(intent)
        }

        buttonQuadratischeGleichungen.setOnClickListener {
            var intent = Intent(applicationContext, LevelAuswahlActivity::class.java)
            intent.putExtra("modus", "gleichung")
            startActivity(intent)
        }
    }
}