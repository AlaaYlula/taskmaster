package com.example.taskmaster.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.taskmaster.data.Task;

@Database(entities = {Task.class},version = 1)
public abstract class AppDatabase  extends RoomDatabase {

    public abstract TaskDao taskDao();

    //instance
    private static AppDatabase appDatabase;
    // contractor
    public AppDatabase(){

    }
    //Method getInstance
    public static synchronized AppDatabase getInstance(Context context){
        if(appDatabase == null){
            appDatabase = Room.databaseBuilder(context,
                    AppDatabase.class, "AppDatabase").allowMainThreadQueries().build();
        }
        return appDatabase;
    }

}
