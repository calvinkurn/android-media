
package com.tokopedia.discovery.imagesearch.domain.model;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AllCategory implements Serializable {

    @SerializedName("Category")
    @Expose
    private List<Category> category = null;

    private final static long serialVersionUID = 1532708098451419544L;

    protected AllCategory(Parcel in) {
        in.readList(this.category, (Category.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     */
    public AllCategory() {
    }

    /**
     * @param category
     */
    public AllCategory(List<Category> category) {
        super();
        this.category = category;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public AllCategory withCategory(List<Category> category) {
        this.category = category;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(category);
    }

    public int describeContents() {
        return 0;
    }

}
