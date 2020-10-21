package com.example.pas19.minesweeper

data class Point(val x :Int ,val y :Int)

data class Size(val  width : Int, val height : Int){
    val area: Int
        get() = width * height
}