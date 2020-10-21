package com.example.pas19.minesweeper

import com.example.pas19.minesweeper.data.entity.Record
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Presenter(private var gameView: GameView, private val model : Model) :
    StateListener {
    enum class State {
        Win, Lose, Continues
    }
    enum class Level{
        Beginner, Intermediate, Expert
    }
    var state : State =
        State.Continues
    fun openCellAt(point: Point){
        model.revealCellAt(point, this)
    }

    override fun onChangeGameField() {
        model.getOpenedCells().forEach { i -> gameView.openCellAt(i.location,
            when (i.type) {
                Cage.Type.Empty -> GameView.CellType.Empty
                Cage.Type.Bomb -> when (state) {
                        State.Continues -> GameView.CellType.Bomb
                        State.Win -> GameView.CellType.Flagged
                        State.Lose -> GameView.CellType.Exploded
                    }
                Cage.Type.Label -> GameView.CellType.Label
            },
            i.label)
        }
    }

    override fun onLose() {
        GlobalScope.launch(Dispatchers.IO){
            val db = App.instance.database
            val recordDao = db.recordDao()
            recordDao.insert(Record(time = getTime(), status = "Lose"))
        }
        state = State.Lose
        model.lose()
        gameView.isBlocked=true
        gameView.showLoseMessage()
    }

    fun getTime() : String{
        val currentDate = Date()
// Форматирование времени как "день.месяц.год"
// Форматирование времени как "день.месяц.год"
        val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val dateText: String = dateFormat.format(currentDate)
// Форматирование времени как "часы:минуты:секунды"
// Форматирование времени как "часы:минуты:секунды"
        val timeFormat: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val timeText: String = timeFormat.format(currentDate)
        return "$dateText $timeText"
    }

    override fun onWin() {
        GlobalScope.launch(Dispatchers.IO){
            val db = App.instance.database
            val recordDao = db.recordDao()
            recordDao.insert(Record(time = getTime(), status = "Win"))
        }
        state = State.Win
        model.win()
        gameView.isBlocked = true
        gameView.showWinMessage()
    }

    fun newGame() {
        state = State.Continues
        model.restart()
        buildGameField()
        gameView.isBlocked = false
    }

    fun changeLevel(level : Level) {
        when(level){
            Level.Beginner -> model.configuration = Model.Configuration.Beginner
            Level.Intermediate -> model.configuration = Model.Configuration.Intermediate
            Level.Expert -> model.configuration = Model.Configuration.Expert
        }
        newGame()
    }

    fun buildGameField() {
        gameView.buildGameField(model.configuration.size)
    }
}