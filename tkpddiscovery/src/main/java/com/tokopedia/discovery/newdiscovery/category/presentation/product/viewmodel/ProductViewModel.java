package com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 10/9/17.
 */

public class ProductViewModel implements Parcelable {
    private CategoryHeaderModel categoryHeaderModel;
    private List<ProductItem> productList;
    private boolean hasCatalog;
    private String shareUrl;

    public List<ProductItem> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductItem> productList) {
        this.productList = productList;
    }

    public boolean isHasCatalog() {
        return hasCatalog;
    }

    public void setHasCatalog(boolean hasCatalog) {
        this.hasCatalog = hasCatalog;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public CategoryHeaderModel getCategoryHeaderModel() {
        return categoryHeaderModel;
    }

    public void setCategoryHeaderModel(CategoryHeaderModel categoryHeaderModel) {
        this.categoryHeaderModel = categoryHeaderModel;
    }

    public ProductViewModel() {
    }

    protected ProductViewModel(Parcel in) {
        categoryHeaderModel = (CategoryHeaderModel) in.readValue(CategoryHeaderModel.class.getClassLoader());
        if (in.readByte() == 0x01) {
            productList = new ArrayList<ProductItem>();
            in.readList(productList, ProductItem.class.getClassLoader());
        } else {
            productList = null;
        }
        hasCatalog = in.readByte() != 0x00;
        shareUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(categoryHeaderModel);
        if (productList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(productList);
        }
        dest.writeByte((byte) (hasCatalog ? 0x01 : 0x00));
        dest.writeString(shareUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProductViewModel> CREATOR = new Parcelable.Creator<ProductViewModel>() {
        @Override
        public ProductViewModel createFromParcel(Parcel in) {
            return new ProductViewModel(in);
        }

        @Override
        public ProductViewModel[] newArray(int size) {
            return new ProductViewModel[size];
        }
    };


}
