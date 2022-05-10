package com.example.taskmaster.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.taskmaster.data.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task")
    List<Task> getAll();

    @Query("SELECT * FROM task WHERE id= :id")
    Task getTaskById(int id);

    @Insert
    Long insertTask(Task task);

    @Delete
    void deleteTask(Task id);
}
