package com.lsw.mentalmathtrainer


val aufgaben : Array<Level> = arrayOf(
    Level(arrayOf(
        OperatorCfg(Operator.Plus, 10, 50, 40, 100),
        OperatorCfg(Operator.Minus, 60, 100, 30, 60)
    ), 5, 1500),
    Level(arrayOf(
        OperatorCfg(Operator.Plus, 50, 100, 40, 100),
        OperatorCfg(Operator.Minus, 100, 200, 50, 100)
    ), 1, 1500),
    Level(arrayOf(
        OperatorCfg(Operator.Plus, 50, 100, 40, 100),
        OperatorCfg(Operator.Minus, 100, 200, 50, 100)
    ), 1, 1500),
    Level(arrayOf(
        OperatorCfg(Operator.Plus, 50, 100, 40, 100),
        OperatorCfg(Operator.Minus, 100, 200, 50, 100)
    ), 1, 1500),
    Level(arrayOf(
        OperatorCfg(Operator.Plus, 50, 100, 40, 100),
        OperatorCfg(Operator.Minus, 100, 200, 50, 100)
    ), 1, 1500),
    Level(arrayOf(
        OperatorCfg(Operator.Plus, 50, 100, 40, 100),
        OperatorCfg(Operator.Minus, 100, 200, 50, 100)
    ), 1, 1500),
    Level(arrayOf(
        OperatorCfg(Operator.Plus, 50, 100, 40, 100),
        OperatorCfg(Operator.Minus, 100, 200, 50, 100)
    ), 1, 1500)

)

var sterneHighscore = Array<Int>(aufgaben.size){0}