package com.tokopedia.inbox.rescenter.historyaction.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 18/12/17.
 */

public class HistoryActionAdapterModel implements Parcelable {
    private HistoryActionViewItem item;
    private boolean showGlowingView;
    private boolean showLastDayAndMonth;
    private boolean showDateSeparator;

    public HistoryActionViewItem getItem() {
        return item;
    }

    public void setItem(HistoryActionViewItem item) {
        this.item = item;
    }

    public boolean isShowGlowingView() {
        return showGlowingView;
    }

    public void setShowGlowingView(boolean showGlowingView) {
        this.showGlowingView = showGlowingView;
    }

    public boolean isShowLastDayAndMonth() {
        return showLastDayAndMonth;
    }

    public void setShowLastDayAndMonth(boolean showLastDayAndMonth) {
        this.showLastDayAndMonth = showLastDayAndMonth;
    }

    public boolean isShowDateSeparator() {
        return showDateSeparator;
    }

    public void setShowDateSeparator(boolean showDateSeparator) {
        this.showDateSeparator = showDateSeparator;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.item, flags);
        dest.writeByte(this.showGlowingView ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showLastDayAndMonth ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showDateSeparator ? (byte) 1 : (byte) 0);
    }

    public HistoryActionAdapterModel() {
    }

    protected HistoryActionAdapterModel(Parcel in) {
        this.item = in.readParcelable(HistoryActionViewItem.class.getClassLoader());
        this.showGlowingView = in.readByte() != 0;
        this.showLastDayAndMonth = in.readByte() != 0;
        this.showDateSeparator = in.readByte() != 0;
    }

    public static final Parcelable.Creator<HistoryActionAdapterModel> CREATOR = new Parcelable.Creator<HistoryActionAdapterModel>() {
        @Override
        public HistoryActionAdapterModel createFromParcel(Parcel source) {
            return new HistoryActionAdapterModel(source);
        }

        @Override
        public HistoryActionAdapterModel[] newArray(int size) {
            return new HistoryActionAdapterModel[size];
        }
    };
}
