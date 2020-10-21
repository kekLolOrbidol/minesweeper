package com.example.pas19.minesweeper.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pas19.minesweeper.data.entity.Record

@Dao
interface RecordDao{

    @Query("SELECT * FROM records")
    fun getAll() : List<Record>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: Record)
}