package com.example.pas19.minesweeper.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pas19.minesweeper.data.dao.RecordDao
import com.example.pas19.minesweeper.data.entity.Record

@Database(entities = [Record::class], version = 2)
abstract class RecordDatabase : RoomDatabase() {
    abstract fun recordDao() : RecordDao
}