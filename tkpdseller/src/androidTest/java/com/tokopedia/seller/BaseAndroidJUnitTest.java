package com.tokopedia.seller;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

/**
 * Created by Nathaniel on 11/16/2016.
 */

public abstract class BaseAndroidJUnitTest {

    protected Context getContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }
}
