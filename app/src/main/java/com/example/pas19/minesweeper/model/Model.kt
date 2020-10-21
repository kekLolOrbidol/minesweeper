package com.example.pas19.minesweeper

import kotlin.collections.ArrayList

class Model {
    data class Configuration(val size : Size, private var bombsCnt: Int) {
        val bombsCount
            get() = bombsCnt
        init {if(bombsCnt >= size.area) bombsCnt = size.area - 1}
        companion object {
            val Beginner = Configuration(
                Size(9, 9),
                10
            )
            val Intermediate = Configuration(
                Size(16, 16),
                40
            )
            val Expert = Configuration(
                Size(31, 16),
                99
            )
        }
    }
    private var domain : Domain? = null

    var configuration = Configuration.Beginner
    set(value) {
        field = value
        restart()
    }

    fun revealCellAt(point: Point, stateListener: StateListener) {
        if(domain == null) {
            domain = Domain(configuration.size, point, configuration.bombsCount)
        }
        if(domain?.revealCellAt(point)== Cage.Type.Bomb) {
            stateListener.onLose()
        }
        if(domain?.openedCells?.count() == configuration.size.area - configuration.bombsCount)
            stateListener.onWin()

        stateListener.onChangeGameField()
    }

    fun restart() {
        domain = null
    }

    fun lose() {
        domain?.revealBombs()
    }

    fun win() {
        domain?.revealBombs()
    }

    fun getOpenedCells() : ArrayList<Cage> {
        val res = ArrayList<Cage>()
        domain?.openedCells?.forEach { i -> res.add(i.copy()) }
        return res
    }

}