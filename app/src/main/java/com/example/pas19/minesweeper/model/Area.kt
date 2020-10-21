package com.example.pas19.minesweeper

data class Generator(private val width: Int) {
    fun generate(index: Int): Cage {
        val point = pointForIndex(index)
        return Cage(point)
    }

    private fun pointForIndex(index : Int) : Point {
        return Point(index % width, index / width)
    }

    fun indexForPoint(point : Point) : Int{
        return point.y * width + point.x
    }
}

data class Field(private val size : Size) {
    private val generator : Generator =
        Generator(size.width)
    var cages: ArrayList<Cage> = arrayListOf()

    init {
        (0 until size.area).map { i -> generator.generate(i) }.toCollection(this.cages)
    }

    private fun isAvailable(point : Point) : Boolean {
        val index = generator.indexForPoint(point)
        return (0 until size.area).contains(index)
    }

    operator fun get(point: Point) : Cage? {
        if(!isAvailable(point))
            return null
        return cages[generator.indexForPoint(point)]
    }

    operator fun set(point : Point, newValue : Cage?) {
        if(!isAvailable(point))
            return
        val index = generator.indexForPoint(point)
        if (newValue != null )
            cages[index] = newValue
        else
            cages.removeAt(index)
    }

    fun getNeighbourCells(cage: Cage): ArrayList<Cage> {
        val neighbours = ArrayList<Cage>()
        for(i in -1..1) {
            for(j in -1..1) {
                val supposedPoint = Point(cage.location.x + i, cage.location.y + j)

                if(!(0 until size.width).contains(supposedPoint.x) ||
                    !(0 until size.height).contains(supposedPoint.y))
                    continue

                val supposedNeighbour = this[supposedPoint]

                if( supposedNeighbour == null || supposedNeighbour.location == cage.location)
                    continue

                neighbours.add(supposedNeighbour)
            }
        }
        return  neighbours
    }
}

