package com.example.demo.user.adapter.out.database;

import com.example.demo.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {

    public static UserEntity fromDomain(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.id());
        entity.setName(user.name());
        return entity;
    }

    @Id
    private String id;

    private String name;

    public User toDomain() {
        return new User(this.getId(), this.getName());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
