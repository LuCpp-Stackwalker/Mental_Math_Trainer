package com.lsw.mentalmathtrainer

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RatingBar
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginLeft
import androidx.core.view.setMargins
import kotlinx.android.synthetic.main.activity_level_auswahl.*
import kotlinx.android.synthetic.main.content_level_auswahl.*

class LevelAuswahlActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_auswahl)
        setSupportActionBar(toolbar)


        for(a in 0 until aufgaben.size)
        {
            var linearLayoutHorizontal = LinearLayout(this)
            linearLayoutHorizontal.orientation = LinearLayout.VERTICAL
            linearLayoutHorizontal.background = getDrawable(R.drawable.level_box)
            linearLayoutHorizontal.gravity = Gravity.CENTER

            var matchParentLayout = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            matchParentLayout.setMargins(10, 10, 10, 10)
            var wrapContentLayout = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            wrapContentLayout.setMargins(10, 10, 10, 10)

            var button = Button(this)
            button.setText("Level " + (a + 1))
            button.textSize = 20.0f
            linearLayoutHorizontal.addView(button, matchParentLayout)

            button.setOnClickListener{
                var intent = Intent(this, MainActivity::class.java)
                intent.putExtra("level", a)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
            }

            var ratingBar = RatingBar(this)
            ratingBar.isEnabled = false
            ratingBar.numStars = 5
            ratingBar.rating = 2.0f
            ratingBar.scaleX = 0.8f
            ratingBar.scaleY = 0.8f
            ratingBar.progressTintList = ColorStateList.valueOf(Color.YELLOW)
            ratingBar.progressBackgroundTintList = ColorStateList.valueOf(Color.GRAY)
            ratingBar.secondaryProgressTintList = ColorStateList.valueOf(Color.TRANSPARENT)
            linearLayoutHorizontal.addView(ratingBar, wrapContentLayout)

            linearLayoutLevelButtons.addView(linearLayoutHorizontal, matchParentLayout)

        }


    }
}
