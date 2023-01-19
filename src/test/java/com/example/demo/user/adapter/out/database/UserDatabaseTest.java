package com.example.demo.user.adapter.out.database;

import com.example.demo.helper.Helper;
import com.example.demo.user.domain.User;
import io.micrometer.core.instrument.MeterRegistry;
import io.vavr.control.Option;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDatabaseTest {

    @Test
    public void testLoadValidUser() {
        // Given
        UserRepository repository = mock(UserRepository.class);
        UserDatabase cut = new UserDatabase(Helper.getMeterRegistry(), repository);
        // When
        String testId = UUID.randomUUID().toString();
        User testUser = User.of(testId, "name12345").get();
        UserEntity entity = UserEntity.fromDomain(testUser);
        when(repository.findById(testUser.id())).thenReturn(Optional.of(entity));
        Option<User> maybeUser = cut.loadUser(testUser.id());
        // Then
        assertThat(maybeUser.isDefined()).isTrue();
        assertThat(maybeUser.get().id()).isEqualTo(testUser.id());
        assertThat(maybeUser.get().name()).isEqualTo(testUser.name());
    }

    @Test
    public void testLoadInvalidUser() {
        // Given
        UserRepository repository = mock(UserRepository.class);
        MeterRegistry registry = Helper.getMeterRegistry();
        UserDatabase cut = new UserDatabase(registry, repository);
        // When
        User testUser = User.of(UUID.randomUUID().toString(), "name12345").get();
        UserEntity entity = UserEntity.fromDomain(testUser);
        entity.setName("name");
        when(repository.findById(testUser.id())).thenReturn(Optional.of(entity));
        Option<User> maybeUser = cut.loadUser(testUser.id());
        // Then
        assertThat(maybeUser.isDefined()).isFalse();
        assertThat(Helper.getCounterValue(registry, "corrupted_data")).isEqualTo(1L);
    }
}