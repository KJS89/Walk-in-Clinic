package com.example.walkinclinics;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
public class ValidatorLastnameTest
{
    @Test
    public void LastnameValidator_CorrectLasrnameSimple_ReturnsTrue() {
        assertTrue(ValidatorLastname.isValidLastname("Seg"));
    }
    @Test
    public void LastnameValidator_InvalidFirstnameNull_ReturnsFalse() {
        assertFalse(ValidatorLastname.isValidLastname(""));
    }
    @Test
    public void LastnameValidator_InvalidFirstnameLongLength_ReturnsFalse() {
        assertFalse(ValidatorLastname.isValidLastname("SegprojectSegprojectSegproject"));
    }
    @Test
    public void LastnameValidator_InvalidFirstnameHasSpecailChar_ReturnsFalse() {
        assertFalse(ValidatorLastname.isValidLastname("Seg."));
    }
    @Test
    public void LastnameValidator_InvalidFirstnameNolowerCase_ReturnsFalse() {
        assertFalse(ValidatorLastname.isValidLastname("SEGPRO"));
    }
    @Test
    public void LastnameValidator_InvalidFirstnameNoUpperCase_ReturnsFalse() {
        assertFalse(ValidatorLastname.isValidLastname("segpro"));
    }
    @Test
    public void LastnameValidator_InvalidFirstnameHasdigitCase_ReturnsFalse() {
        assertFalse(ValidatorLastname.isValidLastname("Seg9988"));
    }

}
