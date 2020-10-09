package com.example.walkinclinics;

import android.widget.EditText;

import androidx.test.rule.ActivityTestRule;
import static org.testng.Assert.assertNotNull;

//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;

//import static org.junit.Assert.*;

public class WelcomeActivityTest {

    //@Rule
    public ActivityTestRule<WelcomeActivity> wActivityRule = new ActivityTestRule<WelcomeActivity>(WelcomeActivity.class);
    private WelcomeActivity wActivity = null;

    //@Before
    public void setUp() throws Exception {
        wActivity = wActivityRule.getActivity();
    }

    //@Test
    public void testLaunch(){
        EditText text1 = wActivity.findViewById(R.id.welcome);
        EditText text2 = wActivity.findViewById(R.id.welcome2);
        assertNotNull(text1);
        assertNotNull(text2);

    }

    //@After
    public void tearDown() throws Exception {
        wActivity = null;
    }

}