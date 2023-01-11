package com.example.demo.register.app;

import com.example.demo.register.domain.Login;
import com.example.demo.register.ports.in.RegisterLogin;
import com.example.demo.register.ports.out.InformAboutLogin;

import org.springframework.stereotype.Component;

@Component
public class RegisterLoginService implements RegisterLogin {

	final private InformAboutLogin informAboutLogin;

	public RegisterLoginService(InformAboutLogin informAboutLogin) {
		this.informAboutLogin = informAboutLogin;
	}

	@Override
	public boolean execute(RegisterLogin.Command command) {
		// something to register
		return Login.validator.applicative().andThen(login -> {
			informAboutLogin.informAboutLogin(login);
			return true;
		}).validate(command.toDomain()).orElseGet(cvs -> false);

	}
}
