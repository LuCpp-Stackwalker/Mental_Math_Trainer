package com.lsw.mentalmathtrainer

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_quadratische_gleichung.*
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.min

class QuadratischeGleichungActivity : AppCompatActivity()
{
    lateinit var tts : TextToSpeech

    private var level : Int = 0
    private var lösung1 : Int = 0
    private var lösung2 : Int = 0
    private var gelösteAufgaben : Int = 0

    private lateinit var timer : CountDownTimer

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quadratische_gleichung)

        tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {status->
            if(status != TextToSpeech.ERROR)
            {
                tts.setLanguage(Locale.GERMANY)
            }
        })

        var textChangedListener = object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                überprüfeInput()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        inputX1.addTextChangedListener(textChangedListener)
        inputX2.addTextChangedListener(textChangedListener)

        startLevel(intent.getIntExtra("level", 0))
    }

    private fun startLevel(level : Int)
    {
        gelösteAufgaben = 0
        this.level = level
        timeBarGleichung.max = levelGleichung[level].zeit
        timeBarGleichung.progress = 0
        timer = object : CountDownTimer(levelGleichung[level].zeit.toLong(), 1)
        {
            override fun onTick(millisUntilFinished: Long) {
                timeBarGleichung.progress = timeBarGleichung.max - millisUntilFinished.toInt()
                var prozent = (timeBarGleichung.progress.toFloat()) / timeBarGleichung.max
                var color = Color.rgb((255 * min(2.2f * prozent, 1.0f)).toInt(), (255 * min(2.2f * (1 - prozent), 1.0f)).toInt(), 0);
                timeBarGleichung.progressTintList = ColorStateList.valueOf(color)
            }

            override fun onFinish() {
                beendeLevel()
            }
        }
        timer.start()
        generiereGleichung()
    }

    private fun abcToStr(a : Int, b : Int, c : Int) : String
    {
        var strA = ""
        if(a != 0)
        {
            strA = (if(a < 0) " - " else "") + (if(a.absoluteValue != 1) a.absoluteValue.toString() else "") + "x\u00b2"
        }
        var strB = ""
        if(b != 0)
        {
            strB = (if(b < 0)  " - " else if(a != 0) " + " else "") + (if(b.absoluteValue != 1) b.absoluteValue.toString() else "") + "x"
        }
        var strC = ""
        if(c != 0)
        {
            strC = (if(c < 0)  " - " else if(a != 0 || b != 0) " + " else "") + c.absoluteValue.toString()
        }
        return strA + strB + strC
    }

    private fun linearfaktorToStr(x1 : Int, x2 : Int) : String
    {
        var str1 = "(x" + (if(x1 >= 0) "-" else "+") + x1.absoluteValue.toString() + ")"
        var str2 = "(x" + (if(x2 >= 0) "-" else "+") + x2.absoluteValue.toString() + ")"
        return str1 + "\u22c5" + str2
    }

    private fun binomischeFormelToStr(a : Int, b : Int) : String
    {
        return "(" + (if(a != 1) a.toString() else "") + "x" + (if(b > 0) "+" else "") + b.toString() + ")\u00b2"
    }

    private fun generiereGleichung() {
        inputX1.text.clear()
        inputX2.text.clear()

        var cfg = levelGleichung[level].cfgs.random()
        lösung1 = cfg.lösung1.random() * (if((0..1).random() == 0) -1 else 1)
        lösung2 = cfg.lösung2.random() * (if((0..1).random() == 0) -1 else 1)
        var faktor = cfg.faktor.random() * (if((0..5).random() == 0) -1 else 1)

        var a = faktor
        var b = - faktor * (lösung1 + lösung2)
        var c = faktor * lösung1 * lösung2

        if(cfg.form == GleichungForm.Gleich0)
        {
            textViewGleichung.text = abcToStr(a, b, c) + " = 0"
        }
        else if(cfg.form == GleichungForm.Polynom)
        {
            // ranges müssen vom kleinen zum großen Wet gehen
            var a2 = (-a.absoluteValue .. a.absoluteValue).random()
            var b2 = (-b.absoluteValue .. b.absoluteValue).random()
            var c2 = (-c.absoluteValue .. c.absoluteValue).random()
            a += a2
            b += b2
            c += c2
            textViewGleichung.text = abcToStr(a, b, c) + " = " + abcToStr(a2, b2, c2)
        }
        else if(cfg.form == GleichungForm.Linearfaktor)
        {
            var faktor2 = (1..faktor).random()
            var x1 = (-lösung1.absoluteValue .. lösung1.absoluteValue).random()
            var x2 = (-lösung2.absoluteValue .. lösung2.absoluteValue).random()

            a += faktor2
            b += - faktor2 * (x1 + x2)
            c += faktor2 * x1 * x2

            textViewGleichung.text = abcToStr(a, b, c) + " = " + linearfaktorToStr(x1, x2)
        }
        else if(cfg.form == GleichungForm.BinomischeFormel)
        {
            var x = (1 .. b.absoluteValue).random()
            if((0..1).random() == 0)
            {
                x *= -1
            }

            a += 1
            b += 2 * x
            c += x * x

            textViewGleichung.text = abcToStr(a, b, c) + " = " + binomischeFormelToStr(1, x)
        }

    }

    private fun überprüfeInput()
    {
        var input1 = inputX1.text.toString().toIntOrNull()
        var input2 = inputX2.text.toString().toIntOrNull()

        if(input1 != null && input2 != null)
        {
            if((input1 == lösung1 && input2 == lösung2) || (input1 == lösung2 && input2 == lösung1))
            {
                tts.speak("richtig", TextToSpeech.QUEUE_FLUSH, null)
                gelösteAufgaben++
                textViewGelösteGleichungen.text = gelösteAufgaben.toString()
                if(gelösteAufgaben == 5)
                {
                    beendeLevel()
                }
                else
                {
                    generiereGleichung()
                }
            }
        }
    }

    private fun beendeLevel()
    {
        // stoppe den Timer
        timer.cancel()
        // zeige Ergebnisse
        var intent = Intent(applicationContext, ErgebnisActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.putExtra("modus", "gleichung")
        intent.putExtra("sterne", gelösteAufgaben)
        intent.putExtra("level", level)
        applicationContext.startActivity(intent)
    }
}

enum class GleichungForm
{
    Gleich0,
    Polynom,
    Linearfaktor,
    BinomischeFormel
}

class GleichungCfg(val faktor : IntRange, val lösung1 : IntRange, val lösung2: IntRange, val form : GleichungForm)

class GleichungLevel(val cfgs : Array<GleichungCfg>, val zeit : Int)