
package com.tokopedia.discovery.imagesearch.domain.usecase;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PicInfo implements Serializable, Parcelable {

    @SerializedName("Category")
    @Expose
    private String category;
    @SerializedName("Region")
    @Expose
    private String region;
    @SerializedName("AllCategory")
    @Expose
    private AllCategory allCategory;

    private List<Category> categoryList;

    public final static Creator<PicInfo> CREATOR = new Creator<PicInfo>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PicInfo createFromParcel(Parcel in) {
            return new PicInfo(in);
        }

        public PicInfo[] newArray(int size) {
            return (new PicInfo[size]);
        }

    };
    private final static long serialVersionUID = 3441251315256387694L;

    protected PicInfo(Parcel in) {
        this.category = ((String) in.readValue((String.class.getClassLoader())));
        this.region = ((String) in.readValue((String.class.getClassLoader())));
        this.allCategory = ((AllCategory) in.readValue((AllCategory.class.getClassLoader())));
        this.categoryList = ((List<Category>) in.readValue(Category.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     */
    public PicInfo() {
    }

    /**
     * @param region
     * @param category
     * @param allCategory
     */
    public PicInfo(String category, String region, AllCategory allCategory, List<Category> categoryList) {
        super();
        this.category = category;
        this.region = region;
        this.allCategory = allCategory;
        this.categoryList = categoryList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public PicInfo withCategory(String category) {
        this.category = category;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public PicInfo withRegion(String region) {
        this.region = region;
        return this;
    }

    public AllCategory getAllCategory() {
        return allCategory;
    }

    public void setAllCategory(AllCategory allCategory) {
        this.allCategory = allCategory;
    }

    public PicInfo withAllCategory(AllCategory allCategory) {
        this.allCategory = allCategory;
        return this;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(category);
        dest.writeValue(region);
        dest.writeValue(allCategory);
        dest.writeValue(categoryList);
    }

    public int describeContents() {
        return 0;
    }

}
