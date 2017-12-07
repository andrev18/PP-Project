package model;

import processor.BuilderProperty;

public class User {
    private String name;
    private int age;

    private int x;

    public User() {
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;



    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }












    @BuilderProperty
    public void setX(int x) {
        this.x = x;
    }
}
