package com.tokopedia.seller.topads.interactor;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.tokopedia.seller.BaseAndroidJUnitTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nathaniel on 11/16/2016.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class DashboardTopAdsInteractorTest extends BaseAndroidJUnitTest {

    private final String TAG = DashboardTopAdsInteractorTest.class.getSimpleName();

    private DashboardTopadsInteractorImpl dashboardTopadsInteractor;
    private CountDownLatch mCountDownLatch;

    @Before
    public void initialSystem() {
        dashboardTopadsInteractor = new DashboardTopadsInteractorImpl(getContext());
    }

    @Test
    public void testGetDashboardProduct() throws Exception {
        mCountDownLatch = new CountDownLatch(1);
        mCountDownLatch.await(10, TimeUnit.SECONDS);
    }
}