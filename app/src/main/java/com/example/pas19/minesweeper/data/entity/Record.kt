package com.example.pas19.minesweeper.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "records")
data class Record(
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null,
    var time : String,
    var status : String
)