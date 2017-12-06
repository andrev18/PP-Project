package model;

public class User {
    private final String name;
    private final int age;

    private int x;

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












    public void setX(int x) {
        this.x = x;
    }
}
