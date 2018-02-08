package com.tokopedia.seller.product.manage.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.product.manage.constant.CatalogProductOption;
import com.tokopedia.seller.product.manage.constant.ConditionProductOption;
import com.tokopedia.seller.product.manage.constant.PictureStatusProductOption;
import com.tokopedia.seller.product.manage.constant.ProductManageConstant;

/**
 * Created by zulfikarrahman on 9/29/17.
 */

public class ProductManageFilterModel implements Parcelable {

    private int etalaseProductOption = ProductManageConstant.FILTER_ALL_PRODUK;
    private String etalaseProductOptionName;
    @PictureStatusProductOption String pictureStatusOption = PictureStatusProductOption.WITH_AND_WITHOUT;
    @ConditionProductOption String conditionProductOption = ConditionProductOption.ALL_CONDITION;
    @CatalogProductOption String catalogProductOption = CatalogProductOption.WITH_AND_WITHOUT;
    private String categoryId = String.valueOf(ProductManageConstant.FILTER_ALL_CATEGORY);
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

    public int getEtalaseProductOption() {
        return etalaseProductOption;
    }

    public void setEtalaseProductOption(int etalaseProductOption) {
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
        setEtalaseProductOption(ProductManageConstant.FILTER_ALL_PRODUK);
        setEtalaseProductOptionName("");
        setCategoryId(String.valueOf(ProductManageConstant.FILTER_ALL_CATEGORY));
        setCategoryName("");
        setPictureStatusOption(PictureStatusProductOption.WITH_AND_WITHOUT);
        setCatalogProductOption(CatalogProductOption.WITH_AND_WITHOUT);
        setConditionProductOption(ConditionProductOption.ALL_CONDITION);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.etalaseProductOption);
        dest.writeString(this.etalaseProductOptionName);
        dest.writeString(this.pictureStatusOption);
        dest.writeString(this.conditionProductOption);
        dest.writeString(this.catalogProductOption);
        dest.writeString(this.categoryId);
        dest.writeString(this.categoryName);
    }

    protected ProductManageFilterModel(Parcel in) {
        this.etalaseProductOption = in.readInt();
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
