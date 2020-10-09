package com.example.walkinclinics;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidatorEmailTest {


    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertTrue(ValidatorEmail.isValidEmail("name@email.com"));
    }
    @Test
    public void emailValidator_CorrectEmailSubDomain_ReturnsTrue() {
        assertTrue(ValidatorEmail.isValidEmail("name@email.co.uk"));
    }
    @Test
    public void emailValidator_InvalidEmailNoTld_ReturnsFalse() {
        assertFalse(ValidatorEmail.isValidEmail("name@email"));
    }
    @Test
    public void emailValidator_InvalidEmailDoubleDot_ReturnsFalse() {
        assertFalse(ValidatorEmail.isValidEmail("name@email..com"));
    }
    @Test
    public void emailValidator_InvalidEmailNoUsername_ReturnsFalse() {
        assertFalse(ValidatorEmail.isValidEmail("@email.com"));
    }
    @Test
    public void emailValidator_EmptyString_ReturnsFalse() {
        assertFalse(ValidatorEmail.isValidEmail(""));
    }
    @Test
    public void emailValidator_NullEmail_ReturnsFalse() {
        assertFalse(ValidatorEmail.isValidEmail(null));
    }
}
