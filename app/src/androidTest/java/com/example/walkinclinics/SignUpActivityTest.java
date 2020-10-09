package com.example.walkinclinics;

import android.widget.EditText;

import androidx.test.rule.ActivityTestRule;
import static org.testng.Assert.assertNotNull;

//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;

//import static org.junit.Assert.*;

public class SignUpActivityTest {
    //@Rule
    public ActivityTestRule<SignUpActivity> sActivityRule = new ActivityTestRule<SignUpActivity>(SignUpActivity.class);
    private SignUpActivity sActivity = null;

    //@Before
    public void setUp() throws Exception {
        sActivity = sActivityRule.getActivity();
    }

    //@Test
    public void testLaunch(){
        EditText text1 = sActivity.findViewById(R.id.new_name);
        EditText text2 = sActivity.findViewById(R.id.new_user_id);
        EditText text3 = sActivity.findViewById(R.id.new_email_id);
        EditText text4 = sActivity.findViewById(R.id.new_address);

        assertNotNull(text1);
        assertNotNull(text2);
        assertNotNull(text3);
        assertNotNull(text4);


    }

    //@After
    public void tearDown() throws Exception {
        sActivity = null;
    }

}