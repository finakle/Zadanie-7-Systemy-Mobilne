package com.example.myapplication;

import java.util.Date;
import java.util.UUID;

public class Task {

    public UUID id;
    public String name;
    public Date date = new Date();
    public boolean done;

    public Task() {
        this.id = UUID.randomUUID();
        this.date =new Date();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
