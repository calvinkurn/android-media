package com.tokopedia.session.register.view.util;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.session.R;

/**
 * Created by nisie on 1/27/17.
 */

public class RegisterUtil {
    public static final int MAX_PHONE_NUMBER = 13;
    public static final int MIN_PHONE_NUMBER = 6;

    public static boolean checkRegexNameLocal(String param){
        String regex = "[A-Za-z]+";
        return !param.replaceAll("\\s","").matches(regex);
    }
    public static boolean isExceedMaxCharacter(String text) {
        return text.length()>35;
    }

    public static String formatDateText(int mDateDay, int mDateMonth, int mDateYear) {
        return String.format("%d / %d / %d", mDateDay, mDateMonth, mDateYear);
    }

    public static String formatDateTextString(int mDateDay, int mDateMonth, int mDateYear) {
        String bulan;
        switch(mDateMonth) {
            case 1: bulan = MainApplication.getAppContext().getString(R.string.january); break;
            case 2: bulan = MainApplication.getAppContext().getString(R.string.february); break;
            case 3: bulan = MainApplication.getAppContext().getString(R.string.march); break;
            case 4: bulan = MainApplication.getAppContext().getString(R.string.april); break;
            case 5: bulan = MainApplication.getAppContext().getString(R.string.may); break;
            case 6: bulan = MainApplication.getAppContext().getString(R.string.june); break;
            case 7: bulan = MainApplication.getAppContext().getString(R.string.july); break;
            case 8: bulan = MainApplication.getAppContext().getString(R.string.august); break;
            case 9: bulan = MainApplication.getAppContext().getString(R.string.september); break;
            case 10: bulan = MainApplication.getAppContext().getString(R.string.october); break;
            case 11: bulan = MainApplication.getAppContext().getString(R.string.november); break;
            case 12: bulan = MainApplication.getAppContext().getString(R.string.december); break;
            default: bulan = MainApplication.getAppContext().getString(R.string.wrong_input); break;
        }
        return String.format("%d %s %d", mDateDay, bulan, mDateYear);
    }

    public static boolean isValidPhoneNumber(String phoneNo) {
        for (int i = MIN_PHONE_NUMBER; i <= MAX_PHONE_NUMBER; i++) {
            if (phoneNo.matches("\\d{" + i + "}")) return true;
        }
        return false;

    }
}
