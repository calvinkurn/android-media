package com.tokopedia.seller.product.manage.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.common.category.view.model.CategoryViewModel;
import com.tokopedia.seller.base.view.adapter.ItemType;

/**
 * Created by zulfikarrahman on 10/10/17.
 */

public class ProductManageCategoryViewModel extends CategoryViewModel implements ItemType, Parcelable {

    public static final int TYPE = 8432;

    public ProductManageCategoryViewModel(String name, long id, boolean hasChild) {
        super(name, id, hasChild);
    }

    @Override
    public int getType() {
        return TYPE;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected ProductManageCategoryViewModel(Parcel in) {
        super(in);
    }

    public static final Creator<ProductManageCategoryViewModel> CREATOR = new Creator<ProductManageCategoryViewModel>() {
        @Override
        public ProductManageCategoryViewModel createFromParcel(Parcel source) {
            return new ProductManageCategoryViewModel(source);
        }

        @Override
        public ProductManageCategoryViewModel[] newArray(int size) {
            return new ProductManageCategoryViewModel[size];
        }
    };
}
