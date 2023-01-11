package com.example.demo.user;

import com.example.demo.AbstractArchUnitTest;
import org.junit.jupiter.api.Test;

public class UserArchUnitTest extends AbstractArchUnitTest {

    @Test
    public void check() {
        checkRules();
    }

    @Override
    protected String basePackage() {
        return "com.example.demo.user.";
    }
}
