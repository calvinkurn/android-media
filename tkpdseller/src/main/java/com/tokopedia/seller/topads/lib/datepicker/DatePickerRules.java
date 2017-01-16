package com.tokopedia.seller.topads.lib.datepicker;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Nathaniel on 1/16/2017.
 */

public class DatePickerRules {
    private static final Locale locale = new Locale("in", "ID");
    public long maxSDate;
    private long maxLimit;
    private long minLimit;
    private int rangeLimit;
    public long maxEDate;

    public long sDate = -1;
    public long eDate = -1;

    public interface DatePickerRulesListener {
        void exceedSDate();

        void exceedEDate();

        void resetToSDate(long sDate, long eDate);

        void resetToEDate(long sDate, long eDate);

        void successSDate(long sDate, long eDate);

        void successEDate(long sDate, long eDate);

        void promptUserExceedLimit();
    }

    DatePickerRulesListener datePickerRulesListener;
    DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", locale);

    public DatePickerRules(long maxLimit, long minLimit, int rangeLimit) {
        this.maxLimit = maxLimit;
        this.minLimit = minLimit;
        this.rangeLimit = rangeLimit;
        this.maxEDate = maxLimit;

        Calendar instance = getInstance();
        instance.setTimeInMillis(maxEDate);
        instance.add(Calendar.DATE, (-1 * rangeLimit));
        this.maxSDate = instance.getTimeInMillis();
    }

    public void setDatePickerRulesListener(DatePickerRulesListener datePickerRulesListener) {
        this.datePickerRulesListener = datePickerRulesListener;
    }

    private Calendar getInstance() {
        return Calendar.getInstance();
    }

    public long getsDate() {
        return sDate;
    }

    public void setsDate(long sDate) {
        Log.d("MNORMANSYAH", "# " + SetDateFragment.getDateFormat(sDate) + " & " + SetDateFragment.getDateFormat(maxLimit) + " & " + SetDateFragment.getDateFormat(minLimit));
        if (sDate > maxLimit || sDate < minLimit) {
            if (datePickerRulesListener != null) {
                datePickerRulesListener.promptUserExceedLimit();
            }
            return;
        }

        if (sDate < maxSDate) {
            maxSDate = sDate;
            this.sDate = sDate;

            Calendar instance = getInstance();
            instance.setTimeInMillis(sDate);
            instance.add(Calendar.DATE, rangeLimit);

            if (instance.getTimeInMillis() >= maxLimit) {
                instance.setTimeInMillis(maxLimit);
            }

            if (instance.getTimeInMillis() <= minLimit) {
                instance.setTimeInMillis(minLimit);
            }

            maxEDate = instance.getTimeInMillis();
            eDate = maxEDate;

            if (datePickerRulesListener != null) {
                datePickerRulesListener.resetToSDate(sDate, eDate);
            }
            return;
        }

        if (isEqual(sDate, eDate)) {
            this.eDate = sDate;
            this.sDate = sDate;
            if (datePickerRulesListener != null) {
                datePickerRulesListener.successSDate(sDate, eDate);
            }
            return;
        }

        if (eDate != -1) {
            Log.d("MNORMANSYAH", String.format("eDate > sDate %s > %s", dateFormat.format(eDate) + "", dateFormat.format(sDate) + ""));
            if (eDate >= sDate) {
                this.sDate = sDate;
                if (datePickerRulesListener != null) {
                    datePickerRulesListener.successSDate(sDate, eDate);
                }
            } else {
                // when start date bigger than end date
                // then start date and upper become new range
                Calendar instance = getInstance();
                instance.setTimeInMillis(sDate);
                instance.add(Calendar.DATE, rangeLimit);
                long eDates = instance.getTimeInMillis();

                instance = getInstance();
                instance.setTimeInMillis(eDates);

                Calendar instance2 = getInstance();
                instance2.setTimeInMillis(maxLimit);
                Log.d("MNORMANSYAH", dateFormat.format(instance.getTime()) + " & " + dateFormat.format(instance2.getTime()));

                if (eDates > maxLimit) {
                    instance = getInstance();
                    instance.setTimeInMillis(maxLimit);
                    instance.add(Calendar.DATE, -1);
                    Log.d("MNORMANSYAH", "set end date exceed ## " + dateFormat.format(instance.getTimeInMillis()));
                    eDate = instance.getTimeInMillis();
                    maxEDate = eDate;
                } else {
                    Log.d("MNORMANSYAH", "set end date normal ## " + dateFormat.format(eDates));
                    eDate = eDates;
                    maxEDate = eDates;
                }

                this.sDate = sDate;
                this.maxSDate = sDate;
            }
        }

//            if(sDate > eDate && eDate != -1){
//                if(datePickerRulesListener != null){
//                    datePickerRulesListener.exceedSDate();
//                }
//                return;
//            }else

        Log.d("MNORMANSYAH ", "eDate " + SetDateFragment.getDateFormat(eDate) + " maxSDate " + SetDateFragment.getDateFormat(maxSDate) + " eDate" + SetDateFragment.getDateFormat(maxEDate));
        if (sDate >= maxSDate && sDate <= maxEDate) {
            this.sDate = sDate;
            if (datePickerRulesListener != null) {
                datePickerRulesListener.successSDate(sDate, eDate);
            }
        }
    }

    public long geteDate() {
        return eDate;
    }

    public void seteDate(long eDate) {
        if (eDate > maxLimit || eDate < minLimit) {
            if (datePickerRulesListener != null) {
                datePickerRulesListener.promptUserExceedLimit();
            }
            return;
        }

        if (eDate > maxEDate) {
            maxEDate = eDate;
            this.eDate = eDate;

            Calendar instance = getInstance();
            instance.setTimeInMillis(eDate);
            instance.add(Calendar.DATE, -1 * rangeLimit);

            if (instance.getTimeInMillis() >= maxLimit) {
                instance.setTimeInMillis(maxLimit);
            }

            if (instance.getTimeInMillis() <= minLimit) {
                instance.setTimeInMillis(minLimit);
            }

            maxSDate = instance.getTimeInMillis();
            sDate = maxSDate;

            if (datePickerRulesListener != null) {
                datePickerRulesListener.resetToEDate(sDate, eDate);
            }
            return;
        }

        if (isEqual(sDate, eDate)) {
            this.eDate = eDate;
            this.sDate = eDate;
            if (datePickerRulesListener != null) {
                datePickerRulesListener.successEDate(sDate, eDate);
            }
            return;
        }

        if (sDate != -1) {
            if (eDate > sDate) {
                this.eDate = eDate;
                if (datePickerRulesListener != null) {
                    datePickerRulesListener.successEDate(sDate, eDate);
                }
            } else {
                // when end date lower than start date
                // then end date and lower become new range
                Calendar instance = getInstance();
                instance.setTimeInMillis(eDate);
                instance.add(Calendar.DATE, -rangeLimit);
                long sDates = instance.getTimeInMillis();

                instance = getInstance();
                instance.setTimeInMillis(sDates);

                Calendar instance2 = getInstance();
                instance2.setTimeInMillis(minLimit);
                DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", locale);
                Log.d("MNORMANSYAH", dateFormat.format(instance.getTime()) + " & " + dateFormat.format(instance2.getTime()));

                if (sDates < minLimit) {
                    instance = getInstance();
                    instance.setTimeInMillis(minLimit);
//                        instance.add(Calendar.DATE, 1);
                    sDate = instance.getTimeInMillis();
                    maxSDate = sDate;
                } else {
                    sDate = sDates;
                    maxSDate = sDates;
                }

                this.eDate = eDate;
                this.maxEDate = eDate;
            }
        }

//            if(sDate > eDate && sDate != -1){
//                if(datePickerRulesListener != null){
//                    datePickerRulesListener.exceedEDate();
//                }
//                return;
//            }else

        Log.d("MNORMANSYAH ", "eDate " + SetDateFragment.getDateFormat(eDate) + " maxSDate " + SetDateFragment.getDateFormat(maxSDate) + " eDate" + SetDateFragment.getDateFormat(maxEDate));
        if (eDate >= maxSDate && eDate <= maxEDate) {
            this.eDate = eDate;
            if (datePickerRulesListener != null) {
                datePickerRulesListener.successEDate(sDate, eDate);
            }
        }
    }

    boolean isEqual(long sDate, long eDate) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTimeInMillis(sDate);
        c2.setTimeInMillis(eDate);

        int yearDiff = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
        int monthDiff = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
        int dayDiff = c1.get(Calendar.DAY_OF_MONTH) - c2.get(Calendar.DAY_OF_MONTH);
        return yearDiff == 0 && monthDiff == 0 && dayDiff == 0;
    }
}
