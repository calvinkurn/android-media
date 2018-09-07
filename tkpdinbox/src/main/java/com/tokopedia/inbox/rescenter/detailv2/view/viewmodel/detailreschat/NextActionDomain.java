package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 10/10/17.
 */

public class NextActionDomain implements Parcelable {

    public static final Creator<NextActionDomain> CREATOR = new Creator<NextActionDomain>() {
        @Override
        public NextActionDomain createFromParcel(Parcel source) {
            return new NextActionDomain(source);
        }

        @Override
        public NextActionDomain[] newArray(int size) {
            return new NextActionDomain[size];
        }
    };
    private String last;
    private NextActionDetailDomain detail;
    private String problem;
    private boolean isSuccess;

    public NextActionDomain(String last, NextActionDetailDomain detail, String problem) {
        this.last = last;
        this.detail = detail;
        this.problem = problem;
    }

    protected NextActionDomain(Parcel in) {
        this.last = in.readString();
        this.detail = in.readParcelable(NextActionDetailDomain.class.getClassLoader());
        this.problem = in.readString();
        this.isSuccess = in.readByte() != 0;
    }

    public static Creator<NextActionDomain> getCREATOR() {
        return CREATOR;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public NextActionDetailDomain getDetail() {
        return detail;
    }

    public void setDetail(NextActionDetailDomain detail) {
        this.detail = detail;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.last);
        dest.writeParcelable(this.detail, flags);
        dest.writeString(this.problem);
        dest.writeByte(this.isSuccess ? (byte) 1 : (byte) 0);
    }
}
