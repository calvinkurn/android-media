
package com.tokopedia.discovery.imagesearch.domain.usecase;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AllCategory implements Serializable, Parcelable {

    @SerializedName("Category")
    @Expose
    private List<Category> category = null;
    public final static Creator<AllCategory> CREATOR = new Creator<AllCategory>() {


        @SuppressWarnings({
                "unchecked"
        })
        public AllCategory createFromParcel(Parcel in) {
            return new AllCategory(in);
        }

        public AllCategory[] newArray(int size) {
            return (new AllCategory[size]);
        }

    };
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
