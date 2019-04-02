package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 08/11/17.
 */
public class LastSolutionData implements Parcelable {

    public static final Parcelable.Creator<LastSolutionData> CREATOR = new Parcelable.Creator<LastSolutionData>() {
        @Override
        public LastSolutionData createFromParcel(Parcel source) {
            return new LastSolutionData(source);
        }

        @Override
        public LastSolutionData[] newArray(int size) {
            return new LastSolutionData[size];
        }
    };
    private int id;
    private String name;
    private String nameCustom;
    private int actionBy;
    private int receivedFlag;
    private AmountData amount;
    private String createTime;
    private String createTimeStr;

    public LastSolutionData(int id, String name, String nameCustom, int actionBy, int receivedFlag, AmountData amount, String createTime, String createTimeStr) {
        this.id = id;
        this.name = name;
        this.nameCustom = nameCustom;
        this.actionBy = actionBy;
        this.receivedFlag = receivedFlag;
        this.amount = amount;
        this.createTime = createTime;
        this.createTimeStr = createTimeStr;
    }

    protected LastSolutionData(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.nameCustom = in.readString();
        this.actionBy = in.readInt();
        this.amount = in.readParcelable(AmountData.class.getClassLoader());
        this.createTime = in.readString();
        this.createTimeStr = in.readString();
    }

    public int getReceivedFlag() {
        return receivedFlag;
    }

    public void setReceivedFlag(int receivedFlag) {
        this.receivedFlag = receivedFlag;
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

    public String getNameCustom() {
        return nameCustom;
    }

    public void setNameCustom(String nameCustom) {
        this.nameCustom = nameCustom;
    }

    public int getActionBy() {
        return actionBy;
    }

    public void setActionBy(int actionBy) {
        this.actionBy = actionBy;
    }

    public AmountData getAmount() {
        return amount;
    }

    public void setAmount(AmountData amount) {
        this.amount = amount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.nameCustom);
        dest.writeInt(this.actionBy);
        dest.writeParcelable(this.amount, flags);
        dest.writeString(this.createTime);
        dest.writeString(this.createTimeStr);
    }
}
