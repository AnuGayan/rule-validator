package org.wso2.rule.validator.functions.core;

import org.junit.jupiter.api.Test;
import org.wso2.rule.validator.InvalidRulesetException;
import org.wso2.rule.validator.document.LintTarget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class XorFunctionTest {
    @Test
    void givenNoPropertiesShouldReturnErrorMessage() {
        Map<String, Object> document = new HashMap<>();
        document.put("version", "1.0.0");
        document.put("title", "Swagger Petstore");
        document.put("termsOfService", "http://swagger.io/terms/");

        Map<String, Object> options = Map.of("properties", new ArrayList<>(List.of("yada-yada", "whatever", "foo")));
        XorFunction xorFunction = new XorFunction(options);
        LintTarget target = new LintTarget(new ArrayList<>(), document);

        try {
            assertFalse(xorFunction.execute(target));
        } catch (InvalidRulesetException e) {
            fail("Execution should not throw an exception.");
        }
    }

    @Test
    void givenMultiplePropertiesThatDoNotMatchShouldReturnErrorMessage() {
        Map<String, Object> document = new HashMap<>();
        document.put("version", "1.0.0");
        document.put("title", "Swagger Petstore");
        document.put("termsOfService", "http://swagger.io/terms/");

        Map<String, Object> options = Map.of("properties", new ArrayList<>(List.of("yada-yada", "whatever", "foo")));
        XorFunction xorFunction = new XorFunction(options);
        LintTarget target = new LintTarget(new ArrayList<>(), document);

        try {
            assertFalse(xorFunction.execute(target));
        } catch (InvalidRulesetException e) {
            fail("Execution should not throw an exception.");
        }
    }

    @Test
    void givenBothPropertiesShouldReturnErrorMessage() {
        Map<String, Object> document = new HashMap<>();
        document.put("version", "1.0.0");
        document.put("title", "Swagger Petstore");

        Map<String, Object> options = Map.of("properties", new ArrayList<>(List.of("version", "title")));
        XorFunction xorFunction = new XorFunction(options);
        LintTarget target = new LintTarget(new ArrayList<>(), document);

        try {
            assertFalse(xorFunction.execute(target));
        } catch (InvalidRulesetException e) {
            fail("Execution should not throw an exception.");
        }
    }

    @Test
    void givenInvalidInputShouldReturnNoErrorMessage() {
        Map<String, Object> options = Map.of("properties", new ArrayList<>(List.of("version", "title")));
        XorFunction xorFunction = new XorFunction(options);
        LintTarget target = new LintTarget(new ArrayList<>(), null);

        try {
            assertTrue(xorFunction.execute(target));
        } catch (InvalidRulesetException e) {
            fail("Execution should not throw an exception.");
        }
    }

    @Test
    void givenOnlyOnePropertyShouldReturnNoErrorMessage() {
        Map<String, Object> document = new HashMap<>();
        document.put("version", "1.0.0");
        document.put("title", "Swagger Petstore");
        document.put("termsOfService", "http://swagger.io/terms/");

        Map<String, Object> options = Map.of("properties", new ArrayList<>(List.of("something", "title")));
        XorFunction xorFunction = new XorFunction(options);
        LintTarget target = new LintTarget(new ArrayList<>(), document);

        try {
            assertTrue(xorFunction.execute(target));
        } catch (InvalidRulesetException e) {
            fail("Execution should not throw an exception.");
        }
    }

    @Test
    void givenValidOptionsShouldNotThrow() {
        Map<String, Object> options = Map.of("properties", new ArrayList<>(List.of("foo", "bar")));
        XorFunction xorFunction = new XorFunction(options);
        LintTarget target = new LintTarget(new ArrayList<>(), new HashMap<>());

        assertDoesNotThrow(() -> xorFunction.execute(target));
    }

    @Test
    void givenInvalidOptions_shouldThrow() {
        List<Object> invalidOptionsList = Arrays.asList(Map.of("properties", new ArrayList<>(List.of("foo", 2))),
                Map.of("properties", new ArrayList<>(List.of("foo"))), Map.of("properties", new ArrayList<>(List.of())),
                Map.of("properties", new ArrayList<>(List.of("foo", new HashMap<>()))),
                Map.of("properties", new ArrayList<>(List.of("foo", "bar")), "foo", true));

        assertThrows(InvalidRulesetException.class,
                () -> new XorFunction(null).execute(new LintTarget(new ArrayList<>(), new HashMap<>())));

        for (Object invalidOptions : invalidOptionsList) {
            XorFunction xorFunction = new XorFunction((Map<String, Object>) invalidOptions);
            LintTarget target = new LintTarget(new ArrayList<>(), new HashMap<>());

            assertThrows(InvalidRulesetException.class, () -> xorFunction.execute(target));
        }
    }
}
