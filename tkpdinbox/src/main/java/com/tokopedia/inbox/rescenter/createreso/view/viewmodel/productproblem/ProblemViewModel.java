package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ProblemViewModel implements Parcelable {
    private int type;
    private String name;

    public ProblemViewModel(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.name);
    }

    protected ProblemViewModel(Parcel in) {
        this.type = in.readInt();
        this.name = in.readString();
    }

    public static final Creator<ProblemViewModel> CREATOR = new Creator<ProblemViewModel>() {
        @Override
        public ProblemViewModel createFromParcel(Parcel source) {
            return new ProblemViewModel(source);
        }

        @Override
        public ProblemViewModel[] newArray(int size) {
            return new ProblemViewModel[size];
        }
    };
}
