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

public class ProductManageFilterModel implements Parcelable {

    private String etalaseProductOption = EtalaseProductOption.ALL_SHOWCASE;
    private String etalaseProductOptionName;
    @PictureStatusProductOption String pictureStatusOption = PictureStatusProductOption.NOT_USED;
    @ConditionProductOption String conditionProductOption = ConditionProductOption.NOT_USED;
    @CatalogProductOption String catalogProductOption = CatalogProductOption.NOT_USED;
    private String categoryId;
    private String categoryName;

    public String getEtalaseProductOptionName() {
        return etalaseProductOptionName;
    }

    public void setEtalaseProductOptionName(String etalaseProductOptionName) {
        this.etalaseProductOptionName = etalaseProductOptionName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getEtalaseProductOption() {
        return etalaseProductOption;
    }

    public void setEtalaseProductOption(String etalaseProductOption) {
        this.etalaseProductOption = etalaseProductOption;
    }

    public @PictureStatusProductOption String getPictureStatusOption() {
        return pictureStatusOption;
    }

    public void setPictureStatusOption(@PictureStatusProductOption String pictureStatusOption) {
        this.pictureStatusOption = pictureStatusOption;
    }

    public @ConditionProductOption String getConditionProductOption() {
        return conditionProductOption;
    }

    public void setConditionProductOption(@ConditionProductOption String conditionProductOption) {
        this.conditionProductOption = conditionProductOption;
    }

    public @CatalogProductOption String getCatalogProductOption() {
        return catalogProductOption;
    }

    public void setCatalogProductOption(@CatalogProductOption String catalogProductOption) {
        this.catalogProductOption = catalogProductOption;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public ProductManageFilterModel() {
    }

    public void reset() {
        setEtalaseProductOption(EtalaseProductOption.ALL_SHOWCASE);
        setEtalaseProductOptionName("");
        setCategoryId("");
        setCategoryName("");
        setPictureStatusOption(PictureStatusProductOption.NOT_USED);
        setCatalogProductOption(CatalogProductOption.NOT_USED);
        setConditionProductOption(ConditionProductOption.NOT_USED);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.etalaseProductOption);
        dest.writeString(this.etalaseProductOptionName);
        dest.writeString(this.pictureStatusOption);
        dest.writeString(this.conditionProductOption);
        dest.writeString(this.catalogProductOption);
        dest.writeString(this.categoryId);
        dest.writeString(this.categoryName);
    }

    protected ProductManageFilterModel(Parcel in) {
        this.etalaseProductOption = in.readString();
        this.etalaseProductOptionName = in.readString();
        this.pictureStatusOption = in.readString();
        this.conditionProductOption = in.readString();
        this.catalogProductOption = in.readString();
        this.categoryId = in.readString();
        this.categoryName = in.readString();
    }

    public static final Creator<ProductManageFilterModel> CREATOR = new Creator<ProductManageFilterModel>() {
        @Override
        public ProductManageFilterModel createFromParcel(Parcel source) {
            return new ProductManageFilterModel(source);
        }

        @Override
        public ProductManageFilterModel[] newArray(int size) {
            return new ProductManageFilterModel[size];
        }
    };
}
