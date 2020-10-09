package com.example.walkinclinics;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
public class ValidatorFirstnameTest
{
    @Test
    public void FirstnameValidator_CorrectFirstnameSimple_ReturnsTrue() {
        assertTrue(ValidatorFirstname.isValidFirstname("Seg"));
    }
    @Test
    public void FirstnameValidator_InvalidFirstnameNull_ReturnsFalse() {
        assertFalse(ValidatorFirstname.isValidFirstname(""));
    }
    @Test
    public void FirstnameValidator_InvalidFirstnameLongLength_ReturnsFalse() {
        assertFalse(ValidatorFirstname.isValidFirstname("SegprojectSegprojectSegproject"));
    }
    @Test
    public void FirstnameValidator_InvalidFirstnameHasSpecailChar_ReturnsFalse() {
        assertFalse(ValidatorFirstname.isValidFirstname("Seg."));
    }
    @Test
    public void FirstnameValidator_InvalidFirstnameNolowerCase_ReturnsFalse() {
        assertFalse(ValidatorFirstname.isValidFirstname("SEGPRO"));
    }
    @Test
    public void FirstnameValidator_InvalidFirstnameNoUpperCase_ReturnsFalse() {
        assertFalse(ValidatorFirstname.isValidFirstname("segpro"));
    }
    @Test
    public void FirstnameValidator_InvalidFirstnameHasdigitCase_ReturnsFalse() {
        assertFalse(ValidatorFirstname.isValidFirstname("Seg9988"));
    }

}
