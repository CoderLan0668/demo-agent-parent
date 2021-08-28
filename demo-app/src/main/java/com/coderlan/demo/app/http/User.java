package com.coderlan.demo.app.http;

public class User {

    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String sayHelloTo(User other) {
        return "hello, dear " + other.getName();
    }
}
