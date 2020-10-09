package com.example.walkinclinics;

import android.widget.EditText;

import androidx.test.rule.ActivityTestRule;
import static org.testng.Assert.assertNotNull;

//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;

//import static org.junit.Assert.assertNotNull;

public class MainActivityTest {

    //@Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);
    private MainActivity mActivity = null;

    //@Before
    public void setUp() throws Exception {
        mActivity = mActivityRule.getActivity();
    }

    //@Test
    public void testLaunch(){
        EditText text1 = mActivity.findViewById(R.id.user_id);
        assertNotNull(text1);

    }

    //@After
    public void tearDown() throws Exception {
        mActivity = null;
    }

}