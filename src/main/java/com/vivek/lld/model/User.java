package com.vivek.lld.model;

import lombok.Getter;
import lombok.Setter;

import java.util.logging.Logger;

@Getter
@Setter
public class User {
    private static final Logger logger = Logger.getLogger(User.class.getName());
    private final int id;
    private final String name;

    public User(int id, String name) {
        logger.info("User constructor called with id: " + id + ", name: " + name);
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        logger.info("equals method called");
        if(this == obj) {
            return true;
        }

        if(obj == null || getClass() != obj.getClass()) {
            return false;
        }

        User other = (User) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        logger.info("hashCode method called");
        return Integer.hashCode(id);
    }
}
