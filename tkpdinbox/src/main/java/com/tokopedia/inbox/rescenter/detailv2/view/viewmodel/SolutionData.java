package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 3/10/17.
 */

public class SolutionData implements Parcelable {
    private String solutionText;
    private String solutionDate;
    private String solutionProvider;
    private boolean editAble;

    public String getSolutionText() {
        return solutionText;
    }

    public void setSolutionText(String solutionText) {
        this.solutionText = solutionText;
    }

    public String getSolutionDate() {
        return solutionDate;
    }

    public void setSolutionDate(String solutionDate) {
        this.solutionDate = solutionDate;
    }

    public String getSolutionProvider() {
        return solutionProvider;
    }

    public void setSolutionProvider(String solutionProvider) {
        this.solutionProvider = solutionProvider;
    }

    public boolean isEditAble() {
        return editAble;
    }

    public void setEditAble(boolean editAble) {
        this.editAble = editAble;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.solutionText);
        dest.writeString(this.solutionDate);
        dest.writeString(this.solutionProvider);
        dest.writeByte(this.editAble ? (byte) 1 : (byte) 0);
    }

    public SolutionData() {
    }

    protected SolutionData(Parcel in) {
        this.solutionText = in.readString();
        this.solutionDate = in.readString();
        this.solutionProvider = in.readString();
        this.editAble = in.readByte() != 0;
    }

    public static final Parcelable.Creator<SolutionData> CREATOR = new Parcelable.Creator<SolutionData>() {
        @Override
        public SolutionData createFromParcel(Parcel source) {
            return new SolutionData(source);
        }

        @Override
        public SolutionData[] newArray(int size) {
            return new SolutionData[size];
        }
    };
}
