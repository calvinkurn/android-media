package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationSolutionDomain implements Parcelable {

    public static final Parcelable.Creator<ConversationSolutionDomain> CREATOR = new Parcelable.Creator<ConversationSolutionDomain>() {
        @Override
        public ConversationSolutionDomain createFromParcel(Parcel source) {
            return new ConversationSolutionDomain(source);
        }

        @Override
        public ConversationSolutionDomain[] newArray(int size) {
            return new ConversationSolutionDomain[size];
        }
    };
    private int id;
    private String name;
    private int amount;
    private String string;

    public ConversationSolutionDomain(int id, String name, int amount, String string) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.string = string;
    }

    protected ConversationSolutionDomain(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.amount = in.readInt();
        this.string = in.readString();
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

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
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
        dest.writeString(this.string);
    }
}
