package com.lsw.mentalmathtrainer


val levelKlassisch : Array<LevelKlassisch> = arrayOf(
    LevelKlassisch(arrayOf(
        OperatorCfg(Operator.Plus, 1..10, 1..10)
    ), 5, 3000),
    LevelKlassisch(arrayOf(
        OperatorCfg(Operator.Plus, 10..50, 10..50)
    ), 5, 3000),
    LevelKlassisch(arrayOf(
        OperatorCfg(Operator.Minus, 50..100, 10..50)
    ), 5, 3000),
    LevelKlassisch(arrayOf(
        OperatorCfg(Operator.Minus, 50..100, 10..50),
        OperatorCfg(Operator.Plus, 10..50, 10..50)
    ), 10, 3000),
    LevelKlassisch(arrayOf(
        OperatorCfg(Operator.Minus, 100..150, 10..80),
        OperatorCfg(Operator.Plus, 10..50, 50..100)
    ), 10, 3000),
    LevelKlassisch(arrayOf(
        OperatorCfg(Operator.Minus, 100..200, 40..100),
        OperatorCfg(Operator.Plus, 50..100, 50..100)
    ), 5, 2500),
    LevelKlassisch(arrayOf(
        OperatorCfg(Operator.Mal, 1..10, 1..10)
    ), 5, 3000),
    LevelKlassisch(arrayOf(
        OperatorCfg(Operator.Mal, 5..15, 5..15)
    ), 5, 3000),
    LevelKlassisch(arrayOf(
        OperatorCfg(Operator.Geteilt, 5..15, 5..15)
    ), 5, 3000)
)

var sterneHighscore = Array<Int>(levelKlassisch.size){0}

val levelGleichung : Array<GleichungLevel> = arrayOf(
    GleichungLevel(arrayOf(
        GleichungCfg(1..1, 1..5, 1..5, GleichungForm.Gleich0)
    ), 30000),
    GleichungLevel(arrayOf(
        GleichungCfg(1..1, 1..5, 1..5, GleichungForm.Polynom)
    ), 100000),
    GleichungLevel(arrayOf(
        GleichungCfg(1..1, 1..5, 1..5, GleichungForm.Linearfaktor)
    ), 100000),
    GleichungLevel(arrayOf(
        GleichungCfg(1..1, 1..5, 1..5, GleichungForm.BinomischeFormel)
    ), 100000)
)