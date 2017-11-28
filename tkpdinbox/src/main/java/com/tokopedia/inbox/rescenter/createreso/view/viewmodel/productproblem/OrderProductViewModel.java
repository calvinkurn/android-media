package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 14/08/17.
 */

public class OrderProductViewModel implements Parcelable {
    private String name;
    private String thumb;
    private String variant;
    private int quantity;
    private AmountViewModel amount;

    public OrderProductViewModel(String name, String thumb, String variant, int quantity, AmountViewModel amount) {
        this.name = name;
        this.thumb = thumb;
        this.variant = variant;
        this.quantity = quantity;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public AmountViewModel getAmount() {
        return amount;
    }

    public void setAmount(AmountViewModel amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.thumb);
        dest.writeString(this.variant);
        dest.writeInt(this.quantity);
        dest.writeParcelable(this.amount, flags);
    }

    protected OrderProductViewModel(Parcel in) {
        this.name = in.readString();
        this.thumb = in.readString();
        this.variant = in.readString();
        this.quantity = in.readInt();
        this.amount = in.readParcelable(AmountViewModel.class.getClassLoader());
    }

    public static final Creator<OrderProductViewModel> CREATOR = new Creator<OrderProductViewModel>() {
        @Override
        public OrderProductViewModel createFromParcel(Parcel source) {
            return new OrderProductViewModel(source);
        }

        @Override
        public OrderProductViewModel[] newArray(int size) {
            return new OrderProductViewModel[size];
        }
    };
}
