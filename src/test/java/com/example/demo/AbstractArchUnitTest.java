package com.example.demo;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public abstract class AbstractArchUnitTest {

    abstract protected String basePackage();

    protected void checkRules() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.example.demo");

        List<ArchRule> rules = new LinkedList<>();
        rules.add(rule(".", "."));
        rules.add(rule("adapter.in..", "adapter.in.."));
        rules.add(rule("services", "services"));
        rules.add(rule("domain", "."));
        rules.add(rule("ports.in", "adapter.in..", "ports.in", "services"));
        rules.add(rule("ports.out", "adapter.out..", "ports.out", "services"));

        for (ArchRule rule : rules) {
            rule.check(importedClasses);
        }
    }

    protected ArchRule rule(String pkg, String... accessedBy) {
        return classes().that().resideInAPackage(this.basePackage() + pkg).should()
                .onlyBeAccessed().byClassesThat()
                .resideInAnyPackage(
                        Arrays.stream(accessedBy).map(p -> this.basePackage() + p)
                                .toArray(value -> new String[accessedBy.length])
                );
    }
}
