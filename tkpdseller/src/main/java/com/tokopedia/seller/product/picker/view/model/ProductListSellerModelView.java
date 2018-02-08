package com.tokopedia.seller.product.picker.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by zulfikarrahman on 9/8/17.
 */

public class ProductListSellerModelView implements Parcelable {
    boolean hasNextPage;
    List<ProductListPickerViewModel> productListPickerViewModels;

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public List<ProductListPickerViewModel> getProductListPickerViewModels() {
        return productListPickerViewModels;
    }

    public void setProductListPickerViewModels(List<ProductListPickerViewModel> productListPickerViewModels) {
        this.productListPickerViewModels = productListPickerViewModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.hasNextPage ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.productListPickerViewModels);
    }

    public ProductListSellerModelView() {
    }

    protected ProductListSellerModelView(Parcel in) {
        this.hasNextPage = in.readByte() != 0;
        this.productListPickerViewModels = in.createTypedArrayList(ProductListPickerViewModel.CREATOR);
    }

    public static final Creator<ProductListSellerModelView> CREATOR = new Creator<ProductListSellerModelView>() {
        @Override
        public ProductListSellerModelView createFromParcel(Parcel source) {
            return new ProductListSellerModelView(source);
        }

        @Override
        public ProductListSellerModelView[] newArray(int size) {
            return new ProductListSellerModelView[size];
        }
    };
}
