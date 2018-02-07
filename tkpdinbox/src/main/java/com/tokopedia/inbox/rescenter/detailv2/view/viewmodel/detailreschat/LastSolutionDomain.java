package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 10/10/17.
 */

public class LastSolutionDomain implements Parcelable {
    public static final Parcelable.Creator<LastSolutionDomain> CREATOR = new Parcelable.Creator<LastSolutionDomain>() {
        @Override
        public LastSolutionDomain createFromParcel(Parcel source) {
            return new LastSolutionDomain(source);
        }

        @Override
        public LastSolutionDomain[] newArray(int size) {
            return new LastSolutionDomain[size];
        }
    };
    private int id;
    private String name;
    private int amount;

    public LastSolutionDomain(int id, String name, int amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    protected LastSolutionDomain(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.amount = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.amount);
    }
}
