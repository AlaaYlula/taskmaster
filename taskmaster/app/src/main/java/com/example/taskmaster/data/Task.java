package com.example.taskmaster.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String body;
    public State state;

    public enum State {
        New,
        In_Progress,
        Complete
    }

    public Task(String title, String body, State state) {
        this.title = title;
        this.body = body;
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public State getState() {
        return state;
    }

    public int getId() {
        return id;
    }
}
