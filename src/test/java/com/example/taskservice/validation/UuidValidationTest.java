package com.example.taskservice.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class UuidValidationTest {

    private static final String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    private final Pattern uuidPattern = Pattern.compile(UUID_REGEX);

    @ParameterizedTest
    @ValueSource(strings = {
        "123e4567-e89b-12d3-a456-426614174000",
        "550e8400-e29b-41d4-a716-446655440000",
        "6ba7b810-9dad-11d1-80b4-00c04fd430c8",
        "00000000-0000-0000-0000-000000000000",
        "ffffffff-ffff-ffff-ffff-ffffffffffff",
        "FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF"
    })
    void validUUIDs_ShouldMatchPattern(String uuid) {
        assertTrue(uuidPattern.matcher(uuid).matches(), 
                   "Valid UUID should match pattern: " + uuid);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid-uuid",
        "123e4567-e89b-12d3-a456",                           // Too short
        "123e4567-e89b-12d3-a456-426614174000-extra",        // Too long
        "123e4567e89b12d3a456426614174000",                  // Missing dashes
        "123e4567-e89b-12d3-a456-42661417400g",             // Invalid hex character
        "",                                                   // Empty
        " ",                                                  // Whitespace
        "123e4567-e89b-12d3-a456-42661417400Z"              // Invalid char at end
    })
    void invalidUUIDs_ShouldNotMatchPattern(String uuid) {
        assertFalse(uuidPattern.matcher(uuid).matches(), 
                    "Invalid UUID should not match pattern: " + uuid);
    }

    @Test
    void uuidPattern_ShouldMatchExpectedLength() {
        String validUuid = "123e4567-e89b-12d3-a456-426614174000";
        assertEquals(36, validUuid.length());
        assertTrue(uuidPattern.matcher(validUuid).matches());
    }
} 