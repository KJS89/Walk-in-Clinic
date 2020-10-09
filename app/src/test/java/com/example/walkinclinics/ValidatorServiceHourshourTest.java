package com.example.walkinclinics;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidatorServiceHourshourTest
{
    @Test
    public void ValidatorServiceHourshour_CorrectServiceHourshourSimple_ReturnsTrue() {
        assertTrue(ValidatorServiceHourshour.isValidServiceHourshour("09"));
    }
    @Test
    public void ValidatorServiceHourshour_InvalidServiceHourshourNull_ReturnsFalse() {
        assertFalse(ValidatorServiceHourshour.isValidServiceHourshour(""));
    }
    @Test
    public void ValidatorServiceHourshour_InvalidServiceHourshourOutOfRange_ReturnsFalse() {
        assertFalse(ValidatorServiceHourshour.isValidServiceHourshour("05"));
    }
    @Test
    public void ValidatorServiceHourshour_InvalidServiceHourshourOutOfRange2_ReturnsFalse() {
        assertFalse(ValidatorServiceHourshour.isValidServiceHourshour("79"));
    }
    @Test
    public void ValidatorServiceHourshour_InvalidServiceHourshourHasSpecailChar_ReturnsFalse() {
        assertFalse(ValidatorServiceHourshour.isValidServiceHourshour("9."));
    }
    @Test
    public void ValidatorServiceHourshour_InvalidServiceHourshourHaslowerCase_ReturnsFalse() {
        assertFalse(ValidatorServiceHourshour.isValidServiceHourshour("9s"));
    }
    @Test
    public void ValidatorServiceHourshour_InvalidServiceHourshoursHasUpperCase_ReturnsFalse() {
        assertFalse(ValidatorServiceHourshour.isValidServiceHourshour("9A"));
    }
    @Test
    public void ValidatorServiceHourshour_InvalidServiceHourshourNodigitCase_ReturnsFalse() {
        assertFalse(ValidatorServiceHourshour.isValidServiceHourshour("Se"));
    }
}

