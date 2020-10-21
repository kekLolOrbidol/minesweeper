package com.example.pas19.minesweeper

import kotlin.collections.ArrayList

data class Domain(private var field : Field, private val initialLocation : Point, private val bombsCount : Int) {

    constructor(size : Size, initialLocation : Point, bombsCount : Int) : this(
        Field(
            size
        ),initialLocation, bombsCount){
        putBombs()
        addLabels()
    }

    var openedCells = ArrayList<Cage>()
        private set

    private fun generateBombsLocations() : ArrayList<Point>{
        val field = this.field.copy()
        field[initialLocation] = null
        return (0 until bombsCount).mapNotNull { field.cages.pickRandomElement()?.location }.toCollection(ArrayList())
    }

    private fun putBombs() {
        for(location in generateBombsLocations()){
            field[location]?.type = Cage.Type.Bomb
        }
    }

    private fun addLabels() {
        for (cell in field.cages)
            if (cell.type == Cage.Type.Bomb) {
                val neighbours = field.getNeighbourCells(cell)
                for(neighbour in neighbours)
                    if (neighbour.type != Cage.Type.Bomb) {
                    neighbour.type = Cage.Type.Label
                    neighbour.label++
                 }
            }
    }

    fun revealCellAt(point: Point) : Cage.Type {
        val crrCell = field[point] ?: return Cage.Type.Empty

        if (!openedCells.contains(crrCell))
            openedCells.add(crrCell)

        if(crrCell.type == Cage.Type.Empty) {
            val neighbours = field.getNeighbourCells(crrCell)
            for(neighbour in neighbours) {
                if(!openedCells.contains(neighbour) && neighbour.type!= Cage.Type.Bomb)
                    revealCellAt(neighbour.location)
            }
        }
        return crrCell.type
    }

    fun revealBombs() {
        for(cell in field.cages)
            if (cell.isBomb() && !openedCells.contains(cell))
                openedCells.add(cell)
    }

}