package com.lsw.mentalmathtrainer

import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat

import kotlinx.android.synthetic.main.activity_ergebnis.*
import kotlinx.android.synthetic.main.content_ergebnis.*
import kotlinx.android.synthetic.main.content_main.*

class ErgebnisActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ergebnis)
        setSupportActionBar(toolbar)
        textViewPunkteErgebnis.text = intent.getIntExtra("punkte", 0).toString() + " Punkte!"
        var sterne = intent.getIntExtra("sterne", 0)
        ergebnisSterne.rating = sterne.toFloat();
        ergebnisSterne.isEnabled = false

        buttonRetry.setOnClickListener{
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {

    }

}
