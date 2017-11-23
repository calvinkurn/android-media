package com.tokopedia.seller.common.datepicker.view.constant;

import java.util.Locale;

/**
 * Created by Nathaniel on 2/3/2017.
 */

public class DatePickerConstant {

    public static final int RESULT_CODE = 1;

    public static final String DATE_FORMAT = "dd MM yyyy";
    public static final Locale LOCALE = new Locale("in", "ID");
    public static final int DIFF_ONE_WEEK = 6;
    public static final int DIFF_ONE_MONTH = 29;

    public static final String EXTRA_START_DATE = "EXTRA_START_DATE";
    public static final String EXTRA_END_DATE = "EXTRA_END_DATE";
    public static final String EXTRA_SELECTION_TYPE = "EXTRA_SELECTION_TYPE";
    public static final String EXTRA_SELECTION_PERIOD = "EXTRA_SELECTION_PERIOD";
    public static final String EXTRA_MIN_START_DATE = "EXTRA_MIN_START_DATE";
    public static final String EXTRA_MAX_END_DATE = "EXTRA_MAX_END_DATE";
    public static final String EXTRA_MAX_DATE_RANGE = "EXTRA_MAX_DATE_RANGE";
    public static final String EXTRA_DATE_PERIOD_LIST = "EXTRA_DATE_PERIOD_LIST";
    public static final String EXTRA_PAGE_TITLE = "EXTRA_PAGE_TITLE";

    public static final String EXTRA_COMPARE_DATE = "EXTRA_COMPARE_DATE";

    public static final int SELECTION_TYPE_PERIOD_DATE = 0;
    public static final int SELECTION_TYPE_CUSTOM_DATE = 1;
    public static final int REQUEST_CODE_DATE = 50;
}
