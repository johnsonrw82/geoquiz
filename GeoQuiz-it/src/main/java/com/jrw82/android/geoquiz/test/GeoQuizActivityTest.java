package com.jrw82.android.geoquiz.test;

import android.test.ActivityInstrumentationTestCase2;
import com.jrw82.android.geoquiz.*;

public class GeoQuizActivityTest extends ActivityInstrumentationTestCase2<GeoQuizActivity> {

    public GeoQuizActivityTest() {
        super(GeoQuizActivity.class);
    }

    public void testActivity() {
        GeoQuizActivity activity = getActivity();
        assertNotNull(activity);
    }
}

