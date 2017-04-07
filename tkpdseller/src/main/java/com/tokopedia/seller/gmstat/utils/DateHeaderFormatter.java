package com.tokopedia.seller.gmstat.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.tokopedia.seller.gmstat.utils.GoldMerchantDateUtils.getDateFormatForInput;
import static com.tokopedia.seller.gmstat.utils.GoldMerchantDateUtils.getDateRaw;
import static com.tokopedia.seller.gmstat.utils.GoldMerchantDateUtils.getDateWithYear;

/**
 * Created by normansyahputa on 3/29/17.
 */

public class DateHeaderFormatter {
    public static final String YYYY_M_MDD = "yyyyMMdd";
    public static final String DD_MM = "dd MM";
    private static final Locale locale = new Locale("in", "ID");
    String[] monthNames;
    DateFormat dateFormat = new SimpleDateFormat(YYYY_M_MDD, locale);

    public DateHeaderFormatter(String[] monthNames) {
        this.monthNames = monthNames;
    }

    public String getStartDateFormat(long sDate) {
        validateMonths();

        String dateFormatForInput = getDateFormatForInput(sDate, DD_MM);
        return getDateRaw(dateFormatForInput, monthNames);
    }

    protected void validateMonths() {
        if (monthNames == null || monthNames.length == 0)
            throw new IllegalStateException("need to supply valid month name !!");
    }

    public String getEndDateFormat(long eDate) {
        validateMonths();

        return getDateWithYear(getDateFormatForInput(eDate, YYYY_M_MDD), monthNames);
    }
}
