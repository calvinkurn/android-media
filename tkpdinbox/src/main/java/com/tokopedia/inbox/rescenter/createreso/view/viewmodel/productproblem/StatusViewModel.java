package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yoasfs on 14/08/17.
 */

public class StatusViewModel implements Parcelable {
    private boolean delivered;
    private String name;
    private List<StatusTroubleViewModel> trouble;
    private StatusInfoViewModel info;

    public StatusViewModel(boolean delivered, String name, List<StatusTroubleViewModel> trouble, StatusInfoViewModel info) {
        this.delivered = delivered;
        this.name = name;
        this.trouble = trouble;
        this.info = info;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StatusTroubleViewModel> getTrouble() {
        return trouble;
    }

    public void setTrouble(List<StatusTroubleViewModel> trouble) {
        this.trouble = trouble;
    }

    public StatusInfoViewModel getInfo() {
        return info;
    }

    public void setInfo(StatusInfoViewModel info) {
        this.info = info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.delivered ? (byte) 1 : (byte) 0);
        dest.writeString(this.name);
        dest.writeTypedList(this.trouble);
        dest.writeParcelable(this.info, flags);
    }

    protected StatusViewModel(Parcel in) {
        this.delivered = in.readByte() != 0;
        this.name = in.readString();
        this.trouble = in.createTypedArrayList(StatusTroubleViewModel.CREATOR);
        this.info = in.readParcelable(StatusInfoViewModel.class.getClassLoader());
    }

    public static final Creator<StatusViewModel> CREATOR = new Creator<StatusViewModel>() {
        @Override
        public StatusViewModel createFromParcel(Parcel source) {
            return new StatusViewModel(source);
        }

        @Override
        public StatusViewModel[] newArray(int size) {
            return new StatusViewModel[size];
        }
    };
}
