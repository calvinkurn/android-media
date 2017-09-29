package com.tokopedia.seller.product.manage.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.product.manage.constant.CatalogProductOption;
import com.tokopedia.seller.product.manage.constant.ConditionProductOption;
import com.tokopedia.seller.product.manage.constant.EtalaseProductOption;
import com.tokopedia.seller.product.manage.constant.PictureStatusProductOption;

/**
 * Created by zulfikarrahman on 9/29/17.
 */

public class FilterManageProductModel implements Parcelable {

    @EtalaseProductOption String etalaseProductOption;
    @PictureStatusProductOption String pictureStatusOption;
    @ConditionProductOption String conditionProductOption;
    @CatalogProductOption String catalogProductOption;
    String category;

    public String getEtalaseProductOption() {
        return etalaseProductOption;
    }

    public void setEtalaseProductOption(String etalaseProductOption) {
        this.etalaseProductOption = etalaseProductOption;
    }

    public String getPictureStatusOption() {
        return pictureStatusOption;
    }

    public void setPictureStatusOption(String pictureStatusOption) {
        this.pictureStatusOption = pictureStatusOption;
    }

    public String getConditionProductOption() {
        return conditionProductOption;
    }

    public void setConditionProductOption(String conditionProductOption) {
        this.conditionProductOption = conditionProductOption;
    }

    public String getCatalogProductOption() {
        return catalogProductOption;
    }

    public void setCatalogProductOption(String catalogProductOption) {
        this.catalogProductOption = catalogProductOption;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public FilterManageProductModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.etalaseProductOption);
        dest.writeString(this.pictureStatusOption);
        dest.writeString(this.conditionProductOption);
        dest.writeString(this.catalogProductOption);
        dest.writeString(this.category);
    }

    protected FilterManageProductModel(Parcel in) {
        this.etalaseProductOption = in.readString();
        this.pictureStatusOption = in.readString();
        this.conditionProductOption = in.readString();
        this.catalogProductOption = in.readString();
        this.category = in.readString();
    }

    public static final Creator<FilterManageProductModel> CREATOR = new Creator<FilterManageProductModel>() {
        @Override
        public FilterManageProductModel createFromParcel(Parcel source) {
            return new FilterManageProductModel(source);
        }

        @Override
        public FilterManageProductModel[] newArray(int size) {
            return new FilterManageProductModel[size];
        }
    };
}
