package com.example.walkinclinics;


import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ValidatorClinicNameTest
{
    @Test
    public void ClinicnameValidator_CorrectClinicnameSimple_ReturnsTrue() {
        assertTrue(ValidatorClinicName.isValidClinicname("Clinic one"));
    }

    @Test
    public void ClinicnameValidator_CorrectClinicnameSimple2_ReturnsTrue() {
        assertTrue(ValidatorClinicName.isValidClinicname("Clinic's branch"));
    }

    @Test
    public void ClinicnameValidator_InvalidClinicnameNull_ReturnsFalse() {
        assertTrue(ValidatorClinicName.isValidClinicname(" "));
    }

    @Test
    public void ClinicnameValidator_InvalidClinicnameHasSpecailChar_ReturnsFalse() {
        assertFalse(ValidatorClinicName.isValidClinicname("Seg."));
    }

    @Test
    public void ClinicnameValidator_InvalidClinicnameHasTwoSpecailChar_ReturnsFalse() {
        assertFalse(ValidatorClinicName.isValidClinicname("Seg./"));
    }


}
