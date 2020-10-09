package com.example.walkinclinics;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidatorServiceHoursminTest
{
    @Test
    public void ValidatorServiceHoursmin_CorrectServiceHourshourSimple_ReturnsTrue() {
        assertTrue(ValidatorServiceHoursmin.isValidServiceHoursmin("30"));
    }
    @Test
    public void ValidatorServiceHoursmin_InvalidServiceHourshourNull_ReturnsFalse() {
        assertFalse(ValidatorServiceHoursmin.isValidServiceHoursmin(""));
    }
    @Test
    public void ValidatorServiceHoursmin_InvalidServiceHourshourOutOfRange_ReturnsFalse() {
        assertFalse(ValidatorServiceHoursmin.isValidServiceHoursmin("99"));
    }
    @Test
    public void ValidatorServiceHoursmin_InvalidServiceHourshourOutOfRange2_ReturnsFalse() {
        assertFalse(ValidatorServiceHoursmin.isValidServiceHoursmin("-1"));
    }
    @Test
    public void ValidatorServiceHoursmin_InvalidServiceHourshourHasSpecailChar_ReturnsFalse() {
        assertFalse(ValidatorServiceHoursmin.isValidServiceHoursmin("9."));
    }
    @Test
    public void ValidatorServiceHoursmin_InvalidServiceHourshourHaslowerCase_ReturnsFalse() {
        assertFalse(ValidatorServiceHoursmin.isValidServiceHoursmin("9s"));
    }
    @Test
    public void ValidatorServiceHoursmin_InvalidServiceHourshoursHasUpperCase_ReturnsFalse() {
        assertFalse(ValidatorServiceHoursmin.isValidServiceHoursmin("9A"));
    }
    @Test
    public void ValidatorServiceHoursmin_InvalidServiceHourshourNodigitCase_ReturnsFalse() {
        assertFalse(ValidatorServiceHoursmin.isValidServiceHoursmin("Se"));
    }
}

