package com.lsw.mentalmathtrainer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_ergebnis.*
import kotlinx.android.synthetic.main.content_ergebnis.*
import java.util.*

class ErgebnisActivity : AppCompatActivity() {

    lateinit var tts : TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ergebnis)
        setSupportActionBar(toolbar)

        var sterne = intent.getIntExtra("sterne", 0)
        ergebnisSterne.rating = sterne.toFloat();
        ergebnisSterne.isEnabled = false
        var level = intent.getIntExtra("level", 0)
        tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status->
            if(status != TextToSpeech.ERROR)
            {
                tts.setLanguage(Locale.GERMANY)
                tts.speak(sprücheNachSternen[sterne].random(), TextToSpeech.QUEUE_FLUSH, null)
            }
        })

        var modus = intent.getStringExtra("modus")
        var preferences : SharedPreferences
        var modusKlasse : Class<out Any>
        if(modus == "klassisch")
        {
            preferences = getSharedPreferences("HIGHSCORE_KLASSISCH", Context.MODE_PRIVATE)
            modusKlasse = MainActivity::class.java
        }
        else if(modus == "gleichung")
        {
            preferences = getSharedPreferences("HIGHSCORE_GLEICHUNG", Context.MODE_PRIVATE)
            modusKlasse = QuadratischeGleichungActivity::class.java
        }
        else
        {
            return
        }

        if(modus == "klassisch")
            textViewPunkteErgebnis.text = intent.getIntExtra("punkte", 0).toString() + " Punkte!"
        else if(modus == "gleichung")
            textViewPunkteErgebnis.text = ""
        // Speichere Highscore
        if(sterne > preferences.getInt("sterneHighscore_" + level, 0))
        {
            sterneHighscore[level] = sterne
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
            val intent = Intent(applicationContext, modusKlasse)
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.putExtra("level", level)
            startActivity(intent)
        }

        // Teste, ob es überhaupt ein nächstes Level gibt
        if(level < levelKlassisch.size - 1)
        {
            buttonNext.setOnClickListener{
                val intent = Intent(applicationContext, modusKlasse)
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

    override fun onDestroy() {
        super.onDestroy()
        tts.stop()
    }

    private val sprücheNachSternen : Array<Array<String>> = arrayOf(
        arrayOf("War das Absicht oder bist du wirklich so schlecht",
            "Immerhin hast du dich stets bemüht", "Versuchs nochmal",
            "Das kann nur besser werden"
        ),
        arrayOf("Immerhin ein Stern...", "Das geht aber besser", "Damit wäre ich nicht zufrieden",
            "streng dich beim nächsten Mal mehr an", "Nicht wirklich gut"),
        arrayOf("Unteres Mittelfeld", "Du bist auf einem guten Weg zur Besserung",
            "Gerade so in Ordnung", "Für deine Verhältnisse ist das in Ordnung"),
        arrayOf("solides Mittelfeld", "Viele sind besser als du, viele sind schlechter als du",
            "etwas besser geht noch", "Ich bin fast zufrieden mit dir", "In Ordnung"),
        arrayOf("Gute Leistung", "Nächstes Mal schaffst du 5 Sterne", "Besser als die meisten",
            "Ich bin immer noch besser!", "Gut, aber nicht perfekt", "So wird das was"),
        arrayOf("Sehr gut", "Du hast meinen Rekord erreicht", "Ich bin beeindruckt",
            "Besser geht es nicht", "Schön", "Tolle Leistung")
    )
}
