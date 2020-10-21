package com.example.pas19.minesweeper.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pas19.minesweeper.App
import com.example.pas19.minesweeper.R
import com.example.pas19.minesweeper.adapters.RecordAdapter
import com.example.pas19.minesweeper.model.RecordItem
import kotlinx.android.synthetic.main.activity_records.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecordsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records)
        val db = App.instance.database
        val recordDao = db.recordDao()
        GlobalScope.launch(Dispatchers.IO){
            val recordEntitys = recordDao.getAll()
            val recordItems = mutableListOf<RecordItem>()
            for(ent in recordEntitys){
                val record = RecordItem(ent.time, ent.status)
                recordItems.add(record)
            }
            withContext(Dispatchers.Main){
                val myAdapter = RecordAdapter(recordItems)
                recyclerView.apply {
                    adapter = myAdapter
                    layoutManager = LinearLayoutManager(this@RecordsActivity)
                }
            }
        }
    }
}