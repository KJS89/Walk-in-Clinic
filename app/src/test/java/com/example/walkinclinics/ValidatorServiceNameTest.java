package com.example.walkinclinics;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidatorServiceNameTest
{
    @Test
    public void ServiceNameValidator_Validator_CorrectServiceNameSimple_ReturnsTrue() {
        assertTrue(ValidatorServiceName.isValidServiceName("Doctor"));
    }
    @Test
    public void ServiceNameValidator_InvalidServiceNameNull_ReturnsFalse() {
        assertFalse(ValidatorServiceName.isValidServiceName(" "));
    }
    @Test
    public void ServiceNameValidator_InvalidServiceNameHasSpecailChar_ReturnsFalse() {
        assertFalse(ValidatorServiceName.isValidServiceName("Doctor."));
    }
    @Test
    public void ServiceNameValidator_InvalidServiceNameHasdigitCase_ReturnsFalse() {
        assertFalse(ValidatorServiceName.isValidServiceName("Doctor888"));
    }

}
