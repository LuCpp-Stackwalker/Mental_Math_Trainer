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
    ), 5, 3000),
    LevelKlassisch(arrayOf(
        OperatorCfg(Operator.Geteilt, 8..20, 8..20),
        OperatorCfg(Operator.Mal, 8..20, 8..20)
    ), 10, 3000),
    LevelKlassisch(arrayOf(
        OperatorCfg(Operator.Geteilt, 8..20, 8..20),
        OperatorCfg(Operator.Mal, 8..20, 8..20),
        OperatorCfg(Operator.Minus, 100..200, 40..100),
        OperatorCfg(Operator.Plus, 50..100, 50..100)
    ), 15, 2500)
)

val levelGleichung : Array<GleichungLevel> = arrayOf(
    GleichungLevel(arrayOf(
        GleichungCfg(1..1, 1..5, 1..5, GleichungForm.Gleich0)
    ), 300_000),
    GleichungLevel(arrayOf(
        GleichungCfg(1..1, 2..10, 1..5, GleichungForm.Gleich0)
    ), 250_000),
    GleichungLevel(arrayOf(
        GleichungCfg(1..1, 1..5, 1..5, GleichungForm.Polynom)
    ), 200_000),
    GleichungLevel(arrayOf(
        GleichungCfg(1..1, 1..6, 1..6, GleichungForm.Polynom),
        GleichungCfg(1..1, 2..10, 1..10, GleichungForm.Gleich0)
    ), 180_000),
    GleichungLevel(arrayOf(
        GleichungCfg(1..5, 2..9, 1..9, GleichungForm.Gleich0)
    ), 180_000),
    GleichungLevel(arrayOf(
        GleichungCfg(1..5, 1..6, 1..6, GleichungForm.Polynom),
        GleichungCfg(1..5, 2..10, 1..10, GleichungForm.Gleich0)
    ), 180_000),
    GleichungLevel(arrayOf(
        GleichungCfg(1..5, 2..10, 2..10, GleichungForm.Polynom)
    ), 160_000),
    GleichungLevel(arrayOf(
        GleichungCfg(1..5, 2..10, 2..10, GleichungForm.Polynom),
        GleichungCfg(1..5, 1..6, 1..6, GleichungForm.Linearfaktor)
    ), 160_000),
    GleichungLevel(arrayOf(
        GleichungCfg(1..5, 1..6, 1..6, GleichungForm.BinomischeFormel),
        GleichungCfg(1..5, 1..6, 1..6, GleichungForm.Linearfaktor)
    ), 150_000)
)