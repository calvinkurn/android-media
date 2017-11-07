package com.tokopedia.ride.scrooge;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Vishal Gupta on 10/27/17.
 * <p>
 * <p>
 * This class contains utility functions to launch scrooge screens
 */

public class ScroogePGUtil {

    public static final int REQUEST_CODE_OPEN_SCROOGE_PAGE = 11;
    public static final int RESULT_CODE_ADD_CC_SUCCESS = 201;
    public static final int RESULT_CODE_ADD_CC_FAIL = 202;
    public static final int RESULT_CODE_DELETE_CC_FAIL = 203;
    public static final int RESULT_CODE_DELETE_CC_SUCCESS = 204;
    public static final int RESULT_CODE_RECIEVED_ERROR = 205;
    public static final String RESULT_EXTRA_MSG = "RESULT_EXTRA_MSG";

    /**
     * To launch screens
     *
     * @param activity
     * @param url
     * @param postparams
     */
    public static void openScroogePage(Activity activity, String url, boolean isPostRequest, Bundle postparams) {
        activity.startActivityForResult(ScroogeActivity.getCallingIntent(activity, url, isPostRequest, postparams), REQUEST_CODE_OPEN_SCROOGE_PAGE);
    }
}
