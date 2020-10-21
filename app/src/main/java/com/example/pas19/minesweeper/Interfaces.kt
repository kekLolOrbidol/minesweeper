package com.example.pas19.minesweeper

interface GameView {
    var isBlocked : Boolean
    enum class CellType{
        Empty, Bomb, Label, Closed, Flagged, Exploded
    }
    fun openCellAt(point : Point,type : CellType, label : Int = 0)
    fun buildGameField(size: Size)
    fun showLoseMessage()
    fun showWinMessage()
}

interface StateListener {
    fun onChangeGameField()
    fun onWin()
    fun onLose()
}
