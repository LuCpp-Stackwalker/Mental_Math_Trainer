package com.lsw.mentalmathtrainer

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*
import kotlin.math.max
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    // tts = TextToSpeech
    lateinit var tts: TextToSpeech

    //lateinit var levelHandler : LevelHandler

    private var level: Int = 0
    private var lösung: Int = 0
    private var richtigerButton: Int = 0
    private var aufgabenCounter: Int = 0
    private var punkte: Int = 0
    private lateinit var timer: CountDownTimer
    private lateinit var antwortButtons : Array<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {status->
            if(status != TextToSpeech.ERROR)
            {
                tts.setLanguage(Locale.GERMANY)
            }
        })

        antwortButtons = arrayOf(antwort1, antwort2, antwort3, antwort4)
        level = intent.getIntExtra("level", 0)
        startLevel(level)

        antwort1.setOnClickListener {
            handleInput(0)
        }
        antwort2.setOnClickListener {
            handleInput(1)
        }
        antwort3.setOnClickListener {
            handleInput(2)
        }
        antwort4.setOnClickListener {
            handleInput(3)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        timer.cancel()
        this.finish()
    }

    // Eigentliche Logik

    // Startet ein neues Level und initialisiert dieses
    fun startLevel(level: Int)
    {
        punkte = 0
        aufgabenCounter = 0
        this.level = level

        // Setze die Länge der TimeBar
        timeBar.max = (levelKlassisch[level].guteZeit * 5)
        timeBar.progress = 0
        // Initialisiere den Timer
        timer = object : CountDownTimer((levelKlassisch[level].guteZeit * 5).toLong(), 1) {
            override fun onTick(millisUntilFinished: Long) {
                // Aktualisiere den Fortschritt der TimeBar
                timeBar.progress = timeBar.max - millisUntilFinished.toInt()
                // Passe die Farbe der TimeBar an
                var prozent = (timeBar.progress.toFloat()) / timeBar.max
                var color = Color.rgb((255 * min(2.2f * prozent, 1.0f)).toInt(), (255 * min(2.2f * (1 - prozent), 1.0f)).toInt(), 0);
                timeBar.progressTintList = ColorStateList.valueOf(color)
            }

            override fun onFinish() {
                // Kein Input erfolgt ->  -1 = Zeit abgelaufen
                handleInput(-1)
            }
        }
        // generiere die erste Aufgabe
        generiereAufgabe()
    }

    private fun generiereAufgabe() {
        // Damit es abwechslungsreicher wird, kann es pro Level
        // mehrere OperatorCfgs geben; Wähle eine zufällige:
        var opcfg = levelKlassisch[level].cfg.random()
        var num1 : Int
        var num2 : Int
        if(opcfg.op == Operator.Geteilt)
        {
            // Bei geteilt sind Quotient und Divisor gegeben
            // Der Dividend wird dann daraus berechnet
            num2 = opcfg.range2.random()
            lösung = opcfg.range1.random()
            num1 = num2 * lösung
        }
        else
        {
            // Wähle Zufallszahlen für die Operanden
            num1 = opcfg.range1.random()
            num2 = opcfg.range2.random()
            // berechne die richtige Lösung dieser Aufgabe
            lösung = opcfg.op.apply(num1, num2)
        }
        // Lege den Text der Aufgabe fest: num1 operator num2
        aufgabe.text = num1.toString() + opcfg.op.toString() + num2
        richtigerButton = (0 until antwortButtons.size).random()
        for(i in (0 until  antwortButtons.size))
        {
            // Falls der Knopf der richtige ist, erhält er die Lösung als Text
            if(i == richtigerButton) antwortButtons[i].text = lösung.toString()
            else
            {
                var falscheAntwort : String
                // Sorge dafür, das keine Antworten doppelt sind
                do
                {
                    // generiere eine falsche Antwort
                    falscheAntwort = generiereFalscheAntwort().toString()
                    var bereitsVorhanden = false
                    // Überprüfe, ob die Antwort bereits vorhanden ist
                    for(j in 0 until i)
                    {
                        if(falscheAntwort == antwortButtons[j].text)
                        {
                            bereitsVorhanden = true
                        }
                    }
                    // wenn ja, wiederhole den Vorgang
                } while (bereitsVorhanden)
                antwortButtons[i].text = falscheAntwort
            }
        }
        // erhöhe den Aufgabencounter (Anzahl der Aufgaben) und starte den Timer neu
        aufgabenCounter++
        timeBar.progress = 0
        timer.start()
    }

    private fun generiereFalscheAntwort() : Int
    {
        var maxAbweichung = max(min(lösung / 2, 50), 2)
        var variante = (0 .. 3).random()
        // Falls max Abweichung < 10 funktionieren Varianten 1 und 2 nicht
        // Setze dann das zweite Bit auf 1 -> Zahl entweder 2 oder 3
        if(maxAbweichung < 10) variante = variante or 2
        when(variante)
        {
            // vielfaches von 10 (kleiner als maxAbweichung) addieren / subtrahieren
            0 -> return lösung + 10 * (1 .. maxAbweichung / 10).random()
            1 -> return lösung - 10 * (1 .. maxAbweichung / 10).random()
            // beliebige Zahl (kleiner als maxAbweichung) addieren / subtrahieren
            2 -> return lösung + (1 .. maxAbweichung).random()
            3 -> return lösung - (1 .. maxAbweichung).random()
        }
        // ERROR
        return 0
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
            punkte += 1000 - 100 * timeBar.progress / levelKlassisch[level].guteZeit
            textViewPunkte.text = punkte.toString()
            tts.speak(sprücheRichtig.random(), TextToSpeech.QUEUE_FLUSH, null)
        }
        else if(button != -1)
        {
            (antwortButtons[button].background as GradientDrawable).setColor(Color.RED)
            (antwortButtons[richtigerButton].background as GradientDrawable).setColor(Color.GREEN)
            tts.speak(sprücheFalsch.random(), TextToSpeech.QUEUE_FLUSH, null)
        }
        else
        {
            for(b in antwortButtons)
            {
                (b.background as GradientDrawable).setColor(Color.RED)
            }
            (antwortButtons[richtigerButton].background as GradientDrawable).setColor(Color.GREEN)
            tts.speak(sprücheZeitAbgelaufen.random(), TextToSpeech.QUEUE_FLUSH, null)
        }

        // Warte bis die Sprachausgabe fertig ist
        // Setze dann die Knöpfe zurück und generiere eine neue Aufgabe
        var handler = Handler()
        var runnable : Runnable = Runnable {  }
        runnable = Runnable {
            if(!tts.isSpeaking())
            {
                // Setze die Knöpfe zurück
                for(b in antwortButtons)
                {
                    (b.background as GradientDrawable).setColor(Color.BLUE)
                    b.isEnabled = true
                }
                if(aufgabenCounter < levelKlassisch[level].anzahlAufgaben)
                {
                    generiereAufgabe()
                }
                else
                {
                    // Gehe zum Ergebnis
                    var intent = Intent(applicationContext, ErgebnisActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    intent.putExtra("modus", "klassisch")
                    intent.putExtra("punkte", punkte)
                    var sterne = max(punkte / (levelKlassisch[level].anzahlAufgaben * 150) - 1, 0)
                    intent.putExtra("sterne", sterne)
                    intent.putExtra("level", level)
                    applicationContext.startActivity(intent)
                }
            }
            else
            {
                handler.postDelayed(runnable, 100);
            }
        }
        handler.postDelayed(runnable, 1000)

    }

    private val sprücheRichtig : Array<String> = arrayOf("Gut geraten", "Sehr gut", "Ich bin überrascht von dir",
    "Das war aber auch einfach", "Wenn du das nicht geschafft hättest", "Gut gemacht", "Richtig", "Du bist besser als erwartet",
    "Nicht schlecht", "Das kannst du sehr gut", "Das schafft ja jeder", "Schön", "Glück gehabt")

    private val sprücheFalsch : Array<String> = arrayOf("Dieses Mal hast du falsch geraten", "So wird das nie was",
    "Strenge dich mehr an", "Falsch!", "Nicht gerade gut", "0 Punkte", "Das muss besser werden",
    "Denken! nicht raten!", "Pech gehabt", "Dieses Mal hattest du kein Glück")

    private val sprücheZeitAbgelaufen : Array<String> = arrayOf("Du, bist, zu, langsam", "Bist du eingeschlafen?",
    "Du musst schneller werden", "zu langsam", "Zeit abgelaufen", "Bist du sonst auch so langsam", "Beeil dich",
    "Nicht trödeln", "Kannst du dich nicht für ein paar Sekunden konzentrieren?")

/*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

 */
}

enum class Operator
{
    // Konstanten des Enums
    Plus, Minus, Mal, Geteilt;

    // Funktionen des Enums
    fun apply(a : Int, b : Int) : Int
    {
        when(this)
        {
            Plus -> return a+b
            Minus -> return a-b
            Mal -> return a*b
            Geteilt -> return a/b
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
        }
    }
}

class OperatorCfg
{
    var op: Operator = Operator.Plus
    var range1 : IntRange
    var range2 : IntRange
    constructor(op : Operator, range1 : IntRange, range2 : IntRange)
    {
        this.op = op
        this.range1 = range1
        this.range2 = range2
    }
}

class LevelKlassisch
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
