package com.lsw.mentalmathtrainer

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_ergebnis.*
import kotlinx.android.synthetic.main.content_ergebnis.*

class ErgebnisActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ergebnis)
        setSupportActionBar(toolbar)
        textViewPunkteErgebnis.text = intent.getIntExtra("punkte", 0).toString() + " Punkte!"
        var sterne = intent.getIntExtra("sterne", 0)
        ergebnisSterne.rating = sterne.toFloat();
        ergebnisSterne.isEnabled = false

        var level = intent.getIntExtra("level", 0)

        // Speichere Highscore
        if(sterne > sterneHighscore[level])
        {
            sterneHighscore[level] = sterne
            var preferences = getSharedPreferences("HIGHSCORE_PREF", Context.MODE_PRIVATE)
            var editor = preferences.edit()
            editor.remove("sterneHighscore_" + level)
            editor.putInt("sterneHighscore_" + level, sterne)
            var success = editor.commit()
            Log.d("storeSharedPreferences", success.toString())
        }


        buttonBack.setOnClickListener{
            onBackPressed()
        }

        buttonRetry.setOnClickListener{
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.putExtra("level", level)
            startActivity(intent)
        }

        // Teste, ob es überhaupt ein nächstes Level gibt
        if(level < aufgaben.size - 1)
        {
            buttonNext.setOnClickListener{
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                intent.putExtra("level", level + 1)
                startActivity(intent)
            }
        }
        else
        {
            buttonNext.isEnabled = false
            buttonNext.imageTintList = ColorStateList.valueOf(Color.GRAY)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

}
