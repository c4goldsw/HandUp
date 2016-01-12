package com.handup.handup.model.lsquery;

/**
 * Created by Christopher on 1/4/2016.
 */
public class Course {

    private String name;
    private String description;
    private String stats;

    private int id;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription(){return description;}

    public void setStats(int points, int users) {
        this.stats = "Points: " + users + ". Users: " + points;
    }

    public String getStats(){return stats;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}