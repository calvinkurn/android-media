package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 14/08/17.
 */

public class OrderViewModel implements Parcelable {
    private OrderDetailViewModel detail;
    private OrderProductViewModel product;
    private ShippingViewModel shipping;


    public OrderViewModel(OrderDetailViewModel detail, OrderProductViewModel product, ShippingViewModel shipping) {
        this.detail = detail;
        this.product = product;
        this.shipping = shipping;
    }


    public OrderDetailViewModel getDetail() {
        return detail;
    }

    public void setDetail(OrderDetailViewModel detail) {
        this.detail = detail;
    }

    public OrderProductViewModel getProduct() {
        return product;
    }

    public void setProduct(OrderProductViewModel product) {
        this.product = product;
    }

    public ShippingViewModel getShipping() {
        return shipping;
    }

    public void setShipping(ShippingViewModel shipping) {
        this.shipping = shipping;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.detail, flags);
        dest.writeParcelable(this.product, flags);
        dest.writeParcelable(this.shipping, flags);
    }

    protected OrderViewModel(Parcel in) {
        this.detail = in.readParcelable(OrderDetailViewModel.class.getClassLoader());
        this.product = in.readParcelable(OrderProductViewModel.class.getClassLoader());
        this.shipping = in.readParcelable(ShippingViewModel.class.getClassLoader());
    }

    public static final Creator<OrderViewModel> CREATOR = new Creator<OrderViewModel>() {
        @Override
        public OrderViewModel createFromParcel(Parcel source) {
            return new OrderViewModel(source);
        }

        @Override
        public OrderViewModel[] newArray(int size) {
            return new OrderViewModel[size];
        }
    };
}
