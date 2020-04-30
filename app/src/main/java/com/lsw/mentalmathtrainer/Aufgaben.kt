package com.lsw.mentalmathtrainer


val aufgaben : Array<Level> = arrayOf(
    Level(arrayOf(
        OperatorCfg(Operator.Plus, 1..10, 1..10)
    ), 5, 3000),
    Level(arrayOf(
        OperatorCfg(Operator.Plus, 10..50, 10..50)
    ), 5, 3000),
    Level(arrayOf(
        OperatorCfg(Operator.Minus, 50..100, 10..50)
    ), 5, 3000),
    Level(arrayOf(
        OperatorCfg(Operator.Minus, 50..100, 10..50),
        OperatorCfg(Operator.Plus, 10..50, 10..50)
    ), 10, 3000),
    Level(arrayOf(
        OperatorCfg(Operator.Minus, 100..150, 10..80),
        OperatorCfg(Operator.Plus, 10..50, 50..100)
    ), 10, 3000),
    Level(arrayOf(
        OperatorCfg(Operator.Minus, 100..200, 40..100),
        OperatorCfg(Operator.Plus, 50..100, 50..100)
    ), 5, 2500),
    Level(arrayOf(
        OperatorCfg(Operator.Mal, 1..10, 1..10)
    ), 5, 3000),
    Level(arrayOf(
        OperatorCfg(Operator.Mal, 5..15, 5..15)
    ), 5, 3000),
    Level(arrayOf(
        OperatorCfg(Operator.Geteilt, 5..15, 5..15)
    ), 5, 3000)
)

var sterneHighscore = Array<Int>(aufgaben.size){0}