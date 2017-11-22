package com.tokopedia.ride.scrooge;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.tkpd.library.utils.CommonUtils;

import java.net.URLEncoder;
import java.util.Iterator;

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
    public static final int RESULT_CODE_SUCCESS = 206;
    public static final int RESULT_CODE_FAIL = 207;
    public static final String RESULT_EXTRA_MSG = "RESULT_EXTRA_MSG";

    /**
     * To launch scrooge activity
     *
     * @param activity
     * @param url
     * @param postparams
     */
    public static void openScroogePage(Activity activity, String url, boolean isPostRequest, String postparams, String title) {
        activity.startActivityForResult(ScroogeActivity.getCallingIntent(activity, url, isPostRequest, postparams, title), REQUEST_CODE_OPEN_SCROOGE_PAGE);
    }

    public static void openScroogePage(Activity activity, String url, boolean isPostRequest, Bundle postparams, String title) {
        activity.startActivityForResult(ScroogeActivity.getCallingIntent(activity, url, isPostRequest, getPostData(postparams), title), REQUEST_CODE_OPEN_SCROOGE_PAGE);
    }

    /**
     * To launch scrooge activity
     *
     * @param fragment
     * @param url
     * @param postparams
     */
    public static void openScroogePage(Fragment fragment, String url, boolean isPostRequest, String postparams, String title) {
        fragment.startActivityForResult(ScroogeActivity.getCallingIntent(fragment.getActivity(), url, isPostRequest, postparams, title), REQUEST_CODE_OPEN_SCROOGE_PAGE);
    }

    /**
     * To launch scrooge activity
     *
     * @param fragment
     * @param url
     * @param postparams
     */
    public static void openScroogePage(Fragment fragment, String url, boolean isPostRequest, Bundle postparams, String title) {
        fragment.startActivityForResult(ScroogeActivity.getCallingIntent(fragment.getActivity(), url, isPostRequest, getPostData(postparams), title), REQUEST_CODE_OPEN_SCROOGE_PAGE);
    }

    private static String getPostData(Bundle mPostParams) {
        try {
            CommonUtils.dumper("ScroogeActivity :: Extracting Strings from Bundle...");
            boolean isFirstKey = true;
            StringBuffer stringBuffer = new StringBuffer();
            Iterator iterator = mPostParams.keySet().iterator();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                if (mPostParams.getString(key) != null) {
                    if (!isFirstKey) {
                        stringBuffer.append("&");
                    } else {
                        isFirstKey = false;
                    }
                    stringBuffer.append(URLEncoder.encode(key, "UTF-8"));
                    stringBuffer.append("=");
                    stringBuffer.append(URLEncoder.encode(mPostParams.getString(key), "UTF-8"));
                }
            }

            CommonUtils.dumper("ScroogeActivity :: URL encoded String is " + stringBuffer.toString());
            return stringBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
