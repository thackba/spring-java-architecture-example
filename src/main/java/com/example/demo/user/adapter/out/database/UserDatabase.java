package com.example.demo.user.adapter.out.database;

import am.ik.yavi.core.ConstraintViolation;
import com.example.demo.user.domain.User;
import com.example.demo.user.ports.out.LoadUser;
import com.example.demo.user.ports.out.SaveUser;
import com.example.demo.user.ports.out.UserNameExists;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.MeterRegistry;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDatabase implements LoadUser, SaveUser, UserNameExists {

    final private MeterRegistry meterRegistry;
    final private UserRepository userRepository;

    public UserDatabase(MeterRegistry meterRegistry, UserRepository userRepository) {
        this.meterRegistry = meterRegistry;
        this.userRepository = userRepository;
    }

    @Override
    public Option<User> loadUser(String id) {
        return userRepository.findById(id).map(Option::of).orElseGet(Option::none).flatMap(this::convert);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(UserEntity.fromDomain(user));
    }

    @Override
    public boolean userNameExists(String username) {
        return userRepository.findByName(username).isPresent();
    }

    private Option<User> convert(UserEntity userEntity) {
        Either<List<ConstraintViolation>, User> converted = userEntity.toDomain();
        if (converted.isLeft()) {
            meterRegistry.counter("corrupted_data", List.of(
                    new ImmutableTag("class", this.getClass().getSimpleName()),
                    new ImmutableTag("id", userEntity.getId())
            )).increment();
        }
        return converted.toOption();
    }
}
