package com.project.artconnect.model;

public class Discipline {
    private int id;
    private String name;

    public Discipline() {
    }

    public Discipline(String name) {
        this.name = name;
    }

    public Discipline(int id, String name) {
        this.id = id;
        this.name = name;
    }

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

    @Override
    public String toString() {
        return "Discipline{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
