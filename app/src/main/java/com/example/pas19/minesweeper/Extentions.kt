package com.example.pas19.minesweeper

import java.util.*

fun <E : Any?> MutableList<E>.pickRandomElement() : E? {
    if(isEmpty())
        return null

    val random = Random()
    val index = random.nextInt(indices.endInclusive - indices.start + 1)+ indices.start

    if(index < 0)
        return null

    return removeAt(index)
}