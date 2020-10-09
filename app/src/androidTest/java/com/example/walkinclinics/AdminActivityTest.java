package com.example.walkinclinics;

import android.widget.ListView;
import android.widget.TextView;

import androidx.test.rule.ActivityTestRule;

import static org.testng.Assert.assertNotNull;

//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;

//import static org.junit.Assert.assertNotNull;

public class AdminActivityTest {
    //@Rule
    public ActivityTestRule<AdminActivity> aActivityRule = new ActivityTestRule<AdminActivity>(AdminActivity.class);
    private AdminActivity aActivity = null;

    //@Before
    public void setUp() throws Exception {
        aActivity = aActivityRule.getActivity();
    }

    //@Test
    public void testLaunch(){
        ListView view1 = aActivity.findViewById(R.id.listViewServices);
        ListView view2 = aActivity.findViewById(R.id.listViewPatients);
        ListView view3 = aActivity.findViewById(R.id.listViewEmployee);
        TextView text = aActivity.findViewById(R.id.textView);
        TextView text2 = aActivity.findViewById(R.id.textView2);
        TextView text3 = aActivity.findViewById(R.id.textView3);
        TextView text5 = aActivity.findViewById(R.id.textView5);

        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(text);
        assertNotNull(text2);
        assertNotNull(text3);
        assertNotNull(text5);
    }

    //@After
    public void tearDown() throws Exception {
        aActivity = null;
    }
}