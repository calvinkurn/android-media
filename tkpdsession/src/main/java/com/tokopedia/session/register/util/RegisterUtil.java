package com.tokopedia.session.register.util;

/**
 * Created by nisie on 1/27/17.
 */

public class RegisterUtil {

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
            case 1: bulan = "Januari"; break;
            case 2: bulan = "Februari"; break;
            case 3: bulan = "Maret"; break;
            case 4: bulan = "April"; break;
            case 5: bulan = "May"; break;
            case 6: bulan = "Juni"; break;
            case 7: bulan = "Juli"; break;
            case 8: bulan = "Agustus"; break;
            case 9: bulan = "September"; break;
            case 10: bulan = "Oktober"; break;
            case 11: bulan = "November"; break;
            case 12: bulan = "Desember"; break;
            default: bulan = "inputan salah"; break;
        }
        return String.format("%d %s %d", mDateDay, bulan, mDateYear);
    }
}
