package com.example.demo.register;

import com.example.demo.AbstractArchUnitTest;
import org.junit.jupiter.api.Test;

public class RegisterArchUnitTest extends AbstractArchUnitTest {

    @Test
    public void check() {
        checkRules();
    }

    @Override
    protected String basePackage() {
        return "com.example.demo.register.";
    }
}
