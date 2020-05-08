package com.lsw.mentalmathtrainer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_level_auswahl.*
import kotlinx.android.synthetic.main.content_level_auswahl.*

class LevelAuswahlActivity : AppCompatActivity() {

    var levelCount = 0
    lateinit var preferences : SharedPreferences
    lateinit var modusKlasse : Class<out Any>

    var sternErgebnisse = ArrayList<RatingBar>()

    fun zähleGesammelteSterne() : Int
    {
        var counter = 0
        for(level in 0 until levelCount)
        {
            counter += preferences.getInt("sterneHighscore_" + level, 0)
        }
        return counter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_auswahl)
        setSupportActionBar(toolbar)
        var modus = intent.getStringExtra("modus")


        if(modus == "klassisch")
        {
            levelCount = levelKlassisch.size
            preferences = getSharedPreferences("HIGHSCORE_KLASSISCH", Context.MODE_PRIVATE)
            modusKlasse = MainActivity::class.java
        }
        else if(modus == "gleichung")
        {
            levelCount = levelGleichung.size
            preferences = getSharedPreferences("HIGHSCORE_GLEICHUNG", Context.MODE_PRIVATE)
            modusKlasse = QuadratischeGleichungActivity::class.java
        }
        else
        {
            return
        }

        for(level in 0 until levelCount)
        {
            var linearLayoutVertical = LinearLayout(this)
            linearLayoutVertical.orientation = LinearLayout.VERTICAL
            linearLayoutVertical.background = getDrawable(R.drawable.level_box)
            linearLayoutVertical.gravity = Gravity.CENTER

            var matchParentLayout = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            matchParentLayout.setMargins(10, 10, 10, 10)
            var wrapContentLayout = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            wrapContentLayout.setMargins(10, 10, 10, 10)

            var button = Button(this)
            button.setText("Level " + (level + 1))
            button.textSize = 20.0f
            linearLayoutVertical.addView(button, matchParentLayout)

            button.setOnClickListener{
                var intent = Intent(this, modusKlasse)
                intent.putExtra("level", level)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
            }

            var ratingBar = RatingBar(this)
            ratingBar.isEnabled = false
            ratingBar.numStars = 5
            ratingBar.rating = preferences.getInt("sterneHighscore_" + level, 0).toFloat()
            ratingBar.scaleX = 0.8f
            ratingBar.scaleY = 0.8f
            ratingBar.progressTintList = ColorStateList.valueOf(Color.YELLOW)
            ratingBar.progressBackgroundTintList = ColorStateList.valueOf(Color.GRAY)
            ratingBar.secondaryProgressTintList = ColorStateList.valueOf(Color.TRANSPARENT)
            sternErgebnisse.add(ratingBar)

            linearLayoutVertical.addView(ratingBar, wrapContentLayout)

            linearLayoutLevelButtons.addView(linearLayoutVertical, matchParentLayout)

        }
        textViewSterneGesamt.text = zähleGesammelteSterne().toString() +  " von " +
                (5 * levelCount).toString() + " Sternen"
    }

    override fun onResume() {
        super.onResume()
        for(level in 0 until levelCount)
        {
            sternErgebnisse[level].rating = preferences.getInt("sterneHighscore_" + level, 0).toFloat()
        }
        textViewSterneGesamt.text = zähleGesammelteSterne().toString() +  " von " +
                (5 * levelCount).toString() + " Sternen"


    }
}
