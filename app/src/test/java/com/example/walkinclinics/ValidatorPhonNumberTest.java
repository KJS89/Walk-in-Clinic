package com.example.walkinclinics;


import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidatorPhonNumberTest
{
    @Test
    public void ValidatorPhonNumber_CorrectPhonNumberSimple_ReturnsTrue() {
        assertTrue(ValidatorPhoneNumber.isValidPhonenumber("2019201820"));
    }
    @Test
    public void ValidatorPhonNumber_InvalidPhonNumberNull_ReturnsFalse() {
        assertFalse(ValidatorPhoneNumber.isValidPhonenumber(""));
    }
    @Test
    public void ValidatorPhonNumber_InvalidPhonNumberShorLength_ReturnsFalse() {
        assertFalse(ValidatorPhoneNumber.isValidPhonenumber("78955"));
    }
    @Test
    public void ValidatorPhonNumber_InvalidPhonNumberHasSpecailChar_ReturnsFalse() {
        assertFalse(ValidatorPhoneNumber.isValidPhonenumber("201920182."));
    }
    @Test
    public void ValidatorPhonNumber_InvalidPhonNumberHaslowerCase_ReturnsFalse() {
        assertFalse(ValidatorPhoneNumber.isValidPhonenumber("20192b0182"));
    }
    @Test
    public void ValidatorPhonNumber_InvalidPhonNumberHasUpperCase_ReturnsFalse() {
        assertFalse(ValidatorPhoneNumber.isValidPhonenumber("201920A182"));
    }

}
