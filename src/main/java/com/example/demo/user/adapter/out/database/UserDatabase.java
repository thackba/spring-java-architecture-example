package com.example.demo.user.adapter.out.database;

import com.example.demo.user.domain.User;
import com.example.demo.user.ports.out.LoadUser;
import com.example.demo.user.ports.out.SaveUser;
import com.example.demo.user.ports.out.UserNameExists;

import am.ik.yavi.core.ConstraintViolations;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.MeterRegistry;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class UserDatabase implements LoadUser, SaveUser, UserNameExists {

	final private MeterRegistry meterRegistry;
	final private UserRepository userRepository;

	public UserDatabase(MeterRegistry meterRegistry, UserRepository userRepository) {
		this.meterRegistry = meterRegistry;
		this.userRepository = userRepository;
	}

	@Override
	public Optional<User> loadUser(String id) {
		return userRepository.findById(id).map(UserEntity::toDomain).filter(this::isValid);
	}

	@Override
	public void saveUser(User user) {
		userRepository.save(UserEntity.fromDomain(user));
	}

	@Override
	public boolean userNameExists(String username) {
		return userRepository.findByName(username).isPresent();
	}

	private boolean isValid(User user) {
		ConstraintViolations violations = User.validator.validate(user);
		if (violations.size() > 0) {
			meterRegistry.counter("invalid_object",
					Arrays.asList(new ImmutableTag("class", this.getClass().getSimpleName()))).increment();
		}
		return violations.size() == 0;
	}
}
