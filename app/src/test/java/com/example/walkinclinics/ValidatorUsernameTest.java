package com.example.walkinclinics;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidatorUsernameTest {

        @Test
        public void UsernameValidator_CorrectUsernameSimple_ReturnsTrue() {
            assertTrue(ValidatorUsername.isValidUsername("Aba"));
        }
        @Test
        public void UernameValidator_InvalidUsernameNull_ReturnsFalse() {
            assertFalse(ValidatorUsername.isValidUsername(""));
        }
        @Test
        public void UsernameValidator_InvalidUsernameLongLength_ReturnsFalse() {
            assertFalse(ValidatorUsername.isValidUsername("dhfsviwSfuvuyeu"));
        }
        @Test
        public void UsernameValidator_InvalidUsernameNoUpperCase_ReturnsFalse() {
            assertFalse(ValidatorUsername.isValidUsername("sdfgg"));
        }
        @Test
        public void passwordValidator_InvalidUsernameNolowerCase_ReturnsFalse() {
            assertFalse(ValidatorUsername.isValidUsername("SQWRIEG"));
        }




}
