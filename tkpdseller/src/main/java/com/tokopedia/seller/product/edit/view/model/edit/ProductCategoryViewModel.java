
package com.tokopedia.seller.product.edit.view.model.edit;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductCategoryViewModel implements Parcelable{

    @SerializedName("category_id")
    @Expose
    private long categoryId;
    @SerializedName("category_full_name")
    @Expose
    private String categoryFullName;
    @SerializedName("category_full_title")
    @Expose
    private String categoryFullTitle;

    @SerializedName("category_breadcrumb_url")
    @Expose
    private String categoryBreadcrumbUrl;
    @SerializedName("category_detail")
    @Expose
    private List<ProductCategoryDetailViewModel> categoryDetail = new ArrayList<>();

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryFullName() {
        return categoryFullName;
    }

    public void setCategoryFullName(String categoryFullName) {
        this.categoryFullName = categoryFullName;
    }

    public String getCategoryFullTitile() {
        return categoryFullTitle;
    }

    public void setCategoryFullTitile(String categoryFullTitile) {
        this.categoryFullTitle = categoryFullTitile;
    }

    public String getCategoryBreadcrumbUrl() {
        return categoryBreadcrumbUrl;
    }

    public void setCategoryBreadcrumbUrl(String categoryBreadcrumbUrl) {
        this.categoryBreadcrumbUrl = categoryBreadcrumbUrl;
    }

    public List<ProductCategoryDetailViewModel> getCategoryDetail() {
        return categoryDetail;
    }

    public void setCategoryDetail(List<ProductCategoryDetailViewModel> categoryDetail) {
        this.categoryDetail = categoryDetail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.categoryId);
        dest.writeString(this.categoryFullName);
        dest.writeString(this.categoryFullTitle);
        dest.writeString(this.categoryBreadcrumbUrl);
        dest.writeList(this.categoryDetail);
    }

    public ProductCategoryViewModel() {
    }

    protected ProductCategoryViewModel(Parcel in) {
        this.categoryId = in.readLong();
        this.categoryFullName = in.readString();
        this.categoryFullTitle = in.readString();
        this.categoryBreadcrumbUrl = in.readString();
        this.categoryDetail = new ArrayList<ProductCategoryDetailViewModel>();
        in.readList(this.categoryDetail, ProductCategoryDetailViewModel.class.getClassLoader());
    }

    public static final Creator<ProductCategoryViewModel> CREATOR = new Creator<ProductCategoryViewModel>() {
        @Override
        public ProductCategoryViewModel createFromParcel(Parcel source) {
            return new ProductCategoryViewModel(source);
        }

        @Override
        public ProductCategoryViewModel[] newArray(int size) {
            return new ProductCategoryViewModel[size];
        }
    };
}
