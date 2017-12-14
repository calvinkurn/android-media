package com.tokopedia.inbox.rescenter.utils;

import android.widget.TextView;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;

/**
 * Created by yfsx on 24/11/17.
 */

public class ChatTitleColorUtil {

    public static void adminColorTitle(TextView tvUserTitle, TextView tvUsername) {
        tvUserTitle.setBackground(MethodChecker.getDrawable(MainApplication.getAppContext(), R.drawable.bg_title_admin));
        tvUserTitle.setTextColor(MethodChecker.getColor(MainApplication.getAppContext(), R.color.white));
        tvUsername.setTextColor(MethodChecker.getColor(MainApplication.getAppContext(), R.color.color_chat_admin));
    }

    public static void systemColorTitle(TextView tvUserTitle, TextView tvUsername) {
        tvUserTitle.setBackground(MethodChecker.getDrawable(MainApplication.getAppContext(), R.drawable.bg_title_system));
        tvUserTitle.setTextColor(MethodChecker.getColor(MainApplication.getAppContext(), R.color.black_38));
        tvUsername.setTextColor(MethodChecker.getColor(MainApplication.getAppContext(), R.color.color_chat_system));
    }

    public static void sellerColorTitle(TextView tvUserTitle, TextView tvUsername) {
        tvUserTitle.setBackground(MethodChecker.getDrawable(MainApplication.getAppContext(), R.drawable.bg_title_seller));
        tvUserTitle.setTextColor(MethodChecker.getColor(MainApplication.getAppContext(), R.color.white));
        tvUsername.setTextColor(MethodChecker.getColor(MainApplication.getAppContext(), R.color.color_chat_seller));
    }

    public static void buyerColorTitle(TextView tvUserTitle, TextView tvUsername) {
        tvUserTitle.setBackground(MethodChecker.getDrawable(MainApplication.getAppContext(), R.drawable.bg_title_buyer));
        tvUserTitle.setTextColor(MethodChecker.getColor(MainApplication.getAppContext(), R.color.white));
        tvUsername.setTextColor(MethodChecker.getColor(MainApplication.getAppContext(), R.color.color_chat_buyer));
    }
}
