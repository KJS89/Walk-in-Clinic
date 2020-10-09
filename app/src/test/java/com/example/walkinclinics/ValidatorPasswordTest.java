package com.example.walkinclinics;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ValidatorPasswordTest
{
    @Test
    public void passwordValidator_CorrectPasswordSimple_ReturnsTrue() {
        assertTrue(ValidatorPassword.isValidPassword("Seg2105.2019"));

    }
    @Test
    public void passwordValidator_InvalidPasswordNull_ReturnsFalse() {
        assertFalse(ValidatorPassword.isValidPassword(""));
    }
    @Test
    public void passwordValidator_InvalidPasswordShortLength_ReturnsFalse() {
        assertFalse(ValidatorPassword.isValidPassword("Se21."));
    }
    @Test
    public void passwordValidator_InvalidPasswordLongLength_ReturnsFalse() {
        assertFalse(ValidatorPassword.isValidPassword("2019Seg2105.2019Seg2105.2019Seg2105."));
    }
    @Test
    public void passwordValidator_InvalidPasswordNoSpecailChar_ReturnsFalse() {
        assertFalse(ValidatorPassword.isValidPassword("Seg21052019"));
    }
    @Test
    public void passwordValidator_InvalidPasswordNolowerCase_ReturnsFalse() {
        assertFalse(ValidatorPassword.isValidPassword("SEG2105.2019"));
    }
    @Test
    public void passwordValidator_InvalidPasswordNodigitCase_ReturnsFalse() {
        assertFalse(ValidatorPassword.isValidPassword("Seg.sectionc"));
    }

}
