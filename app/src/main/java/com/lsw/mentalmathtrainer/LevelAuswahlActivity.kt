package com.lsw.mentalmathtrainer

import android.content.Context
import android.content.Intent
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

    var sternErgebnisse = ArrayList<RatingBar>()

    fun zähleGesammelteSterne() : Int
    {
        val preferences = getSharedPreferences("HIGHSCORE_PREF", Context.MODE_PRIVATE)
        var counter = 0
        for(level in 0 until aufgaben.size)
        {
            counter += preferences.getInt("sterneHighscore_" + level, 0)
        }
        return counter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_auswahl)
        setSupportActionBar(toolbar)
        val preferences = getSharedPreferences("HIGHSCORE_PREF", Context.MODE_PRIVATE)

        for(level in 0 until aufgaben.size)
        {
            // lade gespeicherte Daten
            var sterne = preferences.getInt("sterneHighscore_" + level, 0)
            sterneHighscore[level] = sterne

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
                var intent = Intent(this, MainActivity::class.java)
                intent.putExtra("level", level)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
            }

            var ratingBar = RatingBar(this)
            ratingBar.isEnabled = false
            ratingBar.numStars = 5
            ratingBar.rating = sterne.toFloat()
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
                (5 * aufgaben.size).toString() + " Sternen"
    }

    override fun onResume() {
        super.onResume()
        for(level in 0 until aufgaben.size)
        {
            sternErgebnisse[level].rating = sterneHighscore[level].toFloat()
        }
        textViewSterneGesamt.text = zähleGesammelteSterne().toString() +  " von " +
                (5 * aufgaben.size).toString() + " Sternen"
    }
}
