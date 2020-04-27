package com.lsw.mentalmathtrainer

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.CountDownTimer
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import java.util.*
import kotlin.math.max
import kotlin.math.min

enum class Operator
{
    Plus, Minus, Mal, Geteilt, Modulo, Hoch;

    fun apply(a : Int, b : Int) : Int
    {
        when(this)
        {
            Plus -> return a+b
            Minus -> return a-b
            Mal -> return a*b
            Geteilt -> return a/b
            Modulo -> return a%b
            Hoch -> return Math.pow(a.toDouble(), b.toDouble()).toInt()
        }
    }

    override fun toString() : String
    {
        when(this)
        {
            Plus -> return "+"
            Minus -> return "-"
            Mal -> return "\u22C5"
            Geteilt -> return "\u00F7"
            Modulo -> return "mod"
            Hoch -> return "^"
        }
    }
}

class OperatorCfg
{
    var op: Operator = Operator.Plus
    var min1: Int = 0
    var min2: Int = 0
    var max1: Int = 100
    var max2: Int = 100

    constructor(op : Operator, min1 : Int, max1 : Int, min2 : Int, max2 : Int)
    {
        this.op = op
        this.min1 = min1
        this.min2 = min2
        this.max1 = max1
        this.max2 = max2
    }
}

class Level
{
    val cfg: Array<OperatorCfg>
    val anzahlAufgaben: Int
    val guteZeit: Int
    constructor(cfg: Array<OperatorCfg>, anzahlAufgaben: Int, guteZeit: Int)
    {
        this.cfg = cfg
        this.anzahlAufgaben = anzahlAufgaben
        this.guteZeit = guteZeit
    }
}

class LevelHandler
{
    private val antwortButtons : Array<Button>
    private val aufgabeTextView: TextView
    private val tts: TextToSpeech
    private val timeBar: ProgressBar

    private var level: Int = 0
    private var lösung: Int = 0
    private var richtigerButton: Int = 0
    private var aufgabenCounter: Int = 0
    private var punkte: Int = 0
    public lateinit var timer: CountDownTimer

    constructor(antwortButtons: Array<Button>, textView: TextView, tts: TextToSpeech, timeBar: ProgressBar)
    {
        this.antwortButtons = antwortButtons
        this.aufgabeTextView = textView
        this.tts = tts
        this.timeBar = timeBar
    }

    private fun generiereFalscheAntwort() : Int
    {
        var variante = (0 .. 3).random()
        when(variante)
        {
            0 -> return lösung + 10 * (1 .. 4).random()
            1 -> return lösung - 10 * (1 .. 4).random()
            2 -> return lösung + (1 .. 40).random()
            3 -> return lösung - (1 .. 40).random()
        }
        // ERROR
        return 0
    }

    private fun generiereAufgabe()
    {
        var op = aufgaben[level].cfg.random()
        var num1 = (op.min1 .. op.max1).random()
        var num2 = (op.min2 .. op.max2).random()
        lösung = op.op.apply(num1, num2)
        aufgabeTextView.text = num1.toString() + op.op.toString() + num2
        richtigerButton = (0 until antwortButtons.size).random()
        for(i in (0 until  antwortButtons.size))
        {
            if(i == richtigerButton) antwortButtons[i].text = lösung.toString()
            else antwortButtons[i].text = generiereFalscheAntwort().toString()
        }

        aufgabenCounter++
        timeBar.progress = 0
        timer.start()
    }

    fun startLevel(level: Int)
    {
        punkte = 0
        aufgabenCounter = 0
        this.level = level

        timeBar.max = (aufgaben[level].guteZeit * 5)
        timeBar.progress = 0
        timer = object : CountDownTimer((aufgaben[level].guteZeit * 5).toLong(), 1) {
            override fun onTick(millisUntilFinished: Long) {
                timeBar.progress = timeBar.max - millisUntilFinished.toInt()
                var prozent = (timeBar.progress.toFloat()) / timeBar.max
                var color = Color.rgb((255 * min(2.2f * prozent, 1.0f)).toInt(), (255 * min(2.2f * (1 - prozent), 1.0f)).toInt(), 0);
                timeBar.progressTintList = ColorStateList.valueOf(color)
            }

            override fun onFinish() {
                handleInput(-1)
            }
        }
        generiereAufgabe()
    }

    fun handleInput(button: Int)
    {
        for(b in antwortButtons)
        {
            b.isEnabled = false
        }
        timer.cancel()

        if(button == richtigerButton)
        {
            (antwortButtons[button].background as GradientDrawable).setColor(Color.GREEN)
            punkte += 1000 - 100 * timeBar.progress / aufgaben[level].guteZeit
            tvPunkte.text = punkte.toString()
            OnRichtigeAntwort()
        }
        else if(button != -1)
        {
            (antwortButtons[button].background as GradientDrawable).setColor(Color.RED)
            (antwortButtons[richtigerButton].background as GradientDrawable).setColor(Color.GREEN)
            OnFalscheAntwort()
        }
        else
        {
            for(b in antwortButtons)
            {
                (b.background as GradientDrawable).setColor(Color.RED)
            }
            (antwortButtons[richtigerButton].background as GradientDrawable).setColor(Color.GREEN)
            OnZeitAbgelaufen()
        }

        // Warte bis die Sprachausgabe fertig ist
        // Setze dann die Knöpfe zurück und generiere eine neue Aufgabe
        var handler = Handler()
        var runnable : Runnable = Runnable {  }
        runnable = Runnable {
            if(!tts.isSpeaking())
            {
                for(b in antwortButtons)
                {
                    (b.background as GradientDrawable).setColor(Color.BLUE)
                    b.isEnabled = true
                }
                if(aufgabenCounter < aufgaben[level].anzahlAufgaben)
                {
                    generiereAufgabe()
                }
                else
                {
                    var intent = Intent(appContext, ErgebnisActivity::class.java)//.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    intent.putExtra("punkte", punkte)
                    intent.putExtra("sterne", punkte / (aufgaben[level].anzahlAufgaben * 150) - 1)
                    intent.putExtra("level", level)
                    appContext.startActivity(intent)
                }
            }
            else
            {
                handler.postDelayed(runnable, 100);
            }
        }
        handler.postDelayed(runnable, 1000)

    }

    fun OnRichtigeAntwort()
    {
        tts.speak("Gut geraten!", TextToSpeech.QUEUE_FLUSH, null)
    }

    fun OnFalscheAntwort()
    {
        tts.speak("Dieses Mal hast du falsch geraten!", TextToSpeech.QUEUE_FLUSH, null)
    }

    fun OnZeitAbgelaufen()
    {
        tts.speak("Du, bist, zu, langsam", TextToSpeech.QUEUE_FLUSH, null)
    }
}