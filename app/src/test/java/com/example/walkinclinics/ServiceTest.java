package com.example.walkinclinics;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ServiceTest {
    @Test
    public void service_CorrectServiceSimple_ReturnsTrue() {
        assertTrue(Service.isValidService("name"));
    }

    @Test
    public void service_EmptyString_ReturnsFalse() {
        assertFalse(Service.isValidService(""));
    }
    @Test
    public void service_NullService_ReturnsFalse() {
        assertFalse(Service.isValidService(null));
    }
}
