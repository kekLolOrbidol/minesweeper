package com.example.pas19.minesweeper

data class Cage(val location : Point){
    enum class Type {
        Empty, Bomb, Label
    }
    var type = Type.Empty
    var label = 0

    fun isBomb() : Boolean = type == Type.Bomb

    fun copy() : Cage {
        val res = Cage(this.location)
        res.label = this.label
        res.type = this.type
        return res
    }
}