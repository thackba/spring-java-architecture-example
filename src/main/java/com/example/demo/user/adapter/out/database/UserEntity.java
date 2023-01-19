package com.example.demo.user.adapter.out.database;

import am.ik.yavi.core.ConstraintViolation;
import com.example.demo.user.domain.User;
import io.vavr.control.Either;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private String id;
    private String name;

    public static UserEntity fromDomain(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.id());
        entity.setName(user.name());
        return entity;
    }

    public Either<List<ConstraintViolation>, User> toDomain() {
        return User.of(this.getId(), this.getName());
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
