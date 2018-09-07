package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hangnadi on 3/10/17.
 */

public class HistoryData implements Parcelable {
    public static final Parcelable.Creator<HistoryData> CREATOR = new Parcelable.Creator<HistoryData>() {
        @Override
        public HistoryData createFromParcel(Parcel source) {
            return new HistoryData(source);
        }

        @Override
        public HistoryData[] newArray(int size) {
            return new HistoryData[size];
        }
    };
    private List<HistoryItem> historyList;

    public HistoryData() {
    }

    protected HistoryData(Parcel in) {
        this.historyList = in.createTypedArrayList(HistoryItem.CREATOR);
    }

    public List<HistoryItem> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<HistoryItem> historyList) {
        this.historyList = historyList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.historyList);
    }
}
