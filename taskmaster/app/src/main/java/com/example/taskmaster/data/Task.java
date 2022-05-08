package com.example.taskmaster.data;

public class Task {

    String title;
    String body;
    State state;

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
}
