package com.example.demo.user.adapter.out.simple;

import com.example.demo.user.ports.out.GenerateUserId;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdGenerator implements GenerateUserId {
	
    @Override
    public String generateUserId() {
        return UUID.randomUUID().toString();
    }
}
