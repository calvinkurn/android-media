package com.tokopedia.seller.gmstat.views.models;

import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.tokopedia.seller.gmstat.views.models.StartOrEndPeriodModel.YESTERDAY;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class PeriodRangeModel extends BasePeriodModel implements Parcelable {

    public static final int TYPE = 1;
    public static final Parcelable.Creator<PeriodRangeModel> CREATOR = new Parcelable.Creator<PeriodRangeModel>() {
        @Override
        public PeriodRangeModel createFromParcel(android.os.Parcel source) {
            return new PeriodRangeModel(source);
        }

        @Override
        public PeriodRangeModel[] newArray(int size) {
            return new PeriodRangeModel[size];
        }
    };
    private static final Locale locale = new Locale("in", "ID");
    public boolean isChecked;
    public String headerText;
    public boolean isRange;
    public int range;
    public long startDate = -1, endDate = -1;
    private String formatText = "%s - %s";

    public PeriodRangeModel() {
        super(TYPE);
    }

    public PeriodRangeModel(boolean isRange, int range) {
        this();
        this.isRange = isRange;
        this.range = range;
    }

    protected PeriodRangeModel(android.os.Parcel in) {
        super(in);
        this.isChecked = in.readByte() != 0;
        this.headerText = in.readString();
        this.formatText = in.readString();
        this.isRange = in.readByte() != 0;
        this.range = in.readInt();
        this.startDate = in.readLong();
        this.endDate = in.readLong();
    }

    public String getDescription() {
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", locale);
        if (isRange) {
            Calendar sDate = calculateCalendar(YESTERDAY);
            endDate = sDate.getTimeInMillis();
            String yesterday = dateFormat.format(sDate.getTime());

            Calendar eDate = calculateCalendar(-range);
            startDate = eDate.getTimeInMillis();
            String startDate = dateFormat.format(eDate.getTime());

            return headerText = String.format(formatText, startDate, yesterday);
        } else {
            Calendar sDate = calculateCalendar(-range);
            startDate = sDate.getTimeInMillis();
            endDate = sDate.getTimeInMillis();
            return dateFormat.format(sDate.getTime());
        }
    }

    private Calendar calculateCalendar(int daysAgo) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, daysAgo);
        return cal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        dest.writeString(this.headerText);
        dest.writeString(this.formatText);
        dest.writeByte(this.isRange ? (byte) 1 : (byte) 0);
        dest.writeInt(this.range);
        dest.writeLong(this.startDate);
        dest.writeLong(this.endDate);
    }
}
