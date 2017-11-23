package com.tokopedia.seller.common.datepicker.view.model;

import android.content.Context;
import android.os.Parcelable;

import com.tokopedia.seller.R;
import com.tokopedia.seller.common.datepicker.utils.DatePickerUtils;

/**
 * Created by Nathaniel on 1/16/2017.
 */

public class PeriodRangeModel implements Parcelable {

    private long startDate;
    private long endDate;
    private String label;

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public PeriodRangeModel() {

    }

    public PeriodRangeModel(long startDate, long endDate) {
        this();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public PeriodRangeModel(long startDate, long endDate, String label) {
        this();
        this.startDate = startDate;
        this.endDate = endDate;
        this.label = label;
    }

    public String getDescription(Context context) {
        String startDateText = DatePickerUtils.getReadableDate(context, startDate);
        String endDateText = DatePickerUtils.getReadableDate(context, endDate);
        if (startDateText.equalsIgnoreCase(endDateText)) {
            return startDateText;
        } else {
            return context.getString(R.string.date_picker_date_range_format_text, startDateText, endDateText);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeLong(startDate);
        dest.writeLong(endDate);
        dest.writeString(label);
    }

    protected PeriodRangeModel(android.os.Parcel in) {
        this.startDate = in.readLong();
        this.endDate = in.readLong();
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