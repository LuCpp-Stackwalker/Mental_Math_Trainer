package com.lsw.mentalmathtrainer

import android.os.Bundle
import android.os.PersistableBundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_quadratische_gleichung.*
import java.util.*
import kotlin.math.absoluteValue

class QuadratischeGleichungActivity : AppCompatActivity()
{
    lateinit var tts : TextToSpeech

    private var lösung1 : Int = 0
    private var lösung2 : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quadratische_gleichung)

        tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {status->
            if(status != TextToSpeech.ERROR)
            {
                tts.setLanguage(Locale.GERMANY)
            }
        })

        generiereGleichung()

        var textChangedListener = object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                überprüfeInput()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        inputX1.addTextChangedListener(textChangedListener)
        inputX2.addTextChangedListener(textChangedListener)
    }

    private fun abcToStr(a : Int, b : Int, c : Int) : String
    {
        var strA = a.toString() + "x\u00b2"
        var strB = (if(b >= 0)  " + " else " - ") + b.absoluteValue.toString() + "x"
        var strC = (if(c >= 0)  " + " else " - ") + c.absoluteValue.toString()

        return strA + strB + strC + " = 0"
    }

    private fun generiereGleichung() {
        lösung1 = (1..10).random()
        lösung2 = (1..10).random()
        var faktor = (1..5).random()

        var a = faktor
        var b = - faktor * (lösung1 + lösung2)
        var c = faktor * lösung1 * lösung2

        textViewGleichung.text = abcToStr(a, b, c)
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
            }
        }
    }
}