package model;

public class UserBuilder {

    private User object = new User();

    public User build() {
        return object;
    }

    public UserBuilder setX(int value) {
        object.setX(value);
        return this;
    }

}
