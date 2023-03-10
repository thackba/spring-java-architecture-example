package com.example.demo.user.adapter.out.database;

import com.example.demo.user.domain.User;
import org.hibernate.JDBCException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testDuplicateName() {
        String userId1 = UUID.randomUUID().toString();
        UserEntity userEntity1 = UserEntity.fromDomain(User.of(userId1, "username").get());
        userRepository.save(userEntity1);
        String userId2 = UUID.randomUUID().toString();
        UserEntity userEntity2 = UserEntity.fromDomain( User.of(userId2, "username").get());
        assertThatThrownBy(() -> userRepository.save(userEntity2))
                .hasCauseInstanceOf(JDBCException.class);
    }

}