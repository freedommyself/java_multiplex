package org.java.multiplex.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Test {

    public static void main(String[] args) {
        User king = new User(1, "king");
        Optional<User> user = Optional.ofNullable(king);
        User userObj = user.orElse(null);
        System.err.println(userObj.getName());

        User user2 = null;
        Optional<User> user21 = Optional.ofNullable(user2);
        User userObj2 = user21.orElseGet(() -> new User(0, "DEFAULT"));
        System.err.println(userObj2.getName());

        System.err.println(user.map(u -> u.getName()).orElse("Unknown"));
        System.err.println(user21.map(u -> u.getName()).orElse("Default"));

        List<String> interests = new ArrayList<String>();
        interests.add("a");
        interests.add("b");
        interests.add("c");
        userObj.setInterests(interests);
        List<String> interests2 = Optional.of(userObj)
                .flatMap(u -> Optional.ofNullable(u.getInterests()))
                .orElse(Collections.emptyList());

        System.out.println(interests2);
    }
}

class User {

    Integer id;
    String name;
    List<String> interests;

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
}

