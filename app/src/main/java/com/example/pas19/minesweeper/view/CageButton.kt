package com.example.pas19.minesweeper

import android.content.Context
import android.widget.ImageButton

class CageButton(context: Context) : ImageButton(context) {
    init{
        setBackgroundResource(R.drawable.lock)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        setMeasuredDimension(width, width)
    }

    fun open(type : GameView.CellType, label : Int = 0) {
        when(type){
            GameView.CellType.Closed -> setBackgroundResource(R.drawable.lock)
            GameView.CellType.Bomb -> setBackgroundResource(R.drawable.bomb_n)
            GameView.CellType.Exploded -> setBackgroundResource(R.drawable.expl)
            GameView.CellType.Flagged -> setBackgroundResource(R.drawable.flag)
            GameView.CellType.Empty -> setBackgroundResource(R.drawable.empty)
            GameView.CellType.Label ->
                when(label) {
                    1 -> setBackgroundResource(R.drawable.d1)
                    2 -> setBackgroundResource(R.drawable.d2)
                    3 -> setBackgroundResource(R.drawable.d3)
                    4 -> setBackgroundResource(R.drawable.d4)
                    5 -> setBackgroundResource(R.drawable.d5)
                    6 -> setBackgroundResource(R.drawable.d6)
                    7 -> setBackgroundResource(R.drawable.d7)
                    8 -> setBackgroundResource(R.drawable.d8)
                }
        }
    }
}

