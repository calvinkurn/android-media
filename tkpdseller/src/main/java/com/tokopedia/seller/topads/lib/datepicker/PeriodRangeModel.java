package com.tokopedia.seller.topads.lib.datepicker;

import android.os.Parcelable;

import org.parceler.Parcel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Nathaniel on 1/16/2017.
 */
public class PeriodRangeModel extends SetDateFragment.BasePeriodModel implements Parcelable {
    private static final Locale locale = new Locale("in", "ID");
    public static final int TYPE = 1;
    public boolean isChecked;
    public long startDate = -1;
    public long endDate = -1;

    private String formatText = "%s - %s";
    private String label;

    public String getLabel() {
        return label;
    }

    public PeriodRangeModel() {
        super(TYPE);
    }

    public PeriodRangeModel(long startDate, long endDate, String label) {
        this();
        this.startDate = startDate;
        this.endDate = endDate;
        this.label = label;
    }

    public String getDescription() {
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", locale);
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(startDate);
        String startDateText = dateFormat.format(startCalendar.getTime());

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(endDate);
        String endDateText = dateFormat.format(endCalendar.getTime());

        if (startDateText.equalsIgnoreCase(endDateText)) {
            return startDateText;
        } else {
            return String.format(formatText, startDateText, endDateText);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        dest.writeLong(this.startDate);
        dest.writeLong(this.endDate);
        dest.writeString(this.formatText);
        dest.writeString(this.label);
    }

    public PeriodRangeModel(android.os.Parcel in) {
        super();
        this.isChecked = in.readByte() != 0;
        this.startDate = in.readLong();
        this.endDate = in.readLong();
        this.formatText = in.readString();
        this.label = in.readString();
    }

    public static final Creator<PeriodRangeModel> CREATOR = new Creator<PeriodRangeModel>() {
        @Override
        public PeriodRangeModel createFromParcel(android.os.Parcel source) {
            return new PeriodRangeModel(source);
        }

        @Override
        public PeriodRangeModel[] newArray(int size) {
            return new PeriodRangeModel[size];
        }
    };
}