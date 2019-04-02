package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionProblemModel implements Parcelable {

    private int type;
    private String name;
    private int trouble;
    private SolutionProblemAmountModel amount;
    private SolutionProblemAmountModel maxAmount;
    private int qty;
    private String remark;

    public SolutionProblemModel(int type,
                                String name,
                                int trouble,
                                SolutionProblemAmountModel amount,
                                SolutionProblemAmountModel maxAmount,
                                int qty,
                                String remark) {
        this.type = type;
        this.name = name;
        this.trouble = trouble;
        this.amount = amount;
        this.maxAmount = maxAmount;
        this.qty = qty;
        this.remark = remark;
    }

    public int getTrouble() {
        return trouble;
    }

    public void setTrouble(int trouble) {
        this.trouble = trouble;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public SolutionProblemAmountModel getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(SolutionProblemAmountModel maxAmount) {
        this.maxAmount = maxAmount;
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

    public SolutionProblemAmountModel getAmount() {
        return amount;
    }

    public void setAmount(SolutionProblemAmountModel amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.name);
        dest.writeInt(this.trouble);
        dest.writeParcelable(this.amount, flags);
        dest.writeParcelable(this.maxAmount, flags);
        dest.writeInt(this.qty);
        dest.writeString(this.remark);
    }

    protected SolutionProblemModel(Parcel in) {
        this.type = in.readInt();
        this.name = in.readString();
        this.trouble = in.readInt();
        this.amount = in.readParcelable(SolutionProblemAmountModel.class.getClassLoader());
        this.maxAmount = in.readParcelable(SolutionProblemAmountModel.class.getClassLoader());
        this.qty = in.readInt();
        this.remark = in.readString();
    }

    public static final Creator<SolutionProblemModel> CREATOR = new Creator<SolutionProblemModel>() {
        @Override
        public SolutionProblemModel createFromParcel(Parcel source) {
            return new SolutionProblemModel(source);
        }

        @Override
        public SolutionProblemModel[] newArray(int size) {
            return new SolutionProblemModel[size];
        }
    };
}
