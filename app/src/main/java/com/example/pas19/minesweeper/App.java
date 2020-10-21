package com.example.pas19.minesweeper;

import android.app.Application;

import androidx.room.Room;

import com.example.pas19.minesweeper.data.RecordDatabase;

public class App extends Application {

    public static App instance;

    private RecordDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, RecordDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public RecordDatabase getDatabase() {
        return database;
    }
}