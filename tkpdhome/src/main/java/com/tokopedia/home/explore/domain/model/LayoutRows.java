package com.tokopedia.home.explore.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.tagmanager.DataLayer;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by errysuprayogi on 2/2/18.
 */
public class LayoutRows implements Parcelable {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name = "";
    @SerializedName("url")
    private String url = "";
    @SerializedName("imageUrl")
    private String imageUrl = "";
    @SerializedName("applinks")
    private String applinks = "";
    @SerializedName("type")
    private String type = "";
    @SerializedName("categoryId")
    private int categoryId;
    @SerializedName("categoryLabel")
    private String categoryLabel = "";
    @SerializedName("score")
    private int score;

    protected LayoutRows(Parcel in) {
        id = in.readInt();
        name = in.readString();
        url = in.readString();
        imageUrl = in.readString();
        applinks = in.readString();
        type = in.readString();
        categoryId = in.readInt();
        categoryLabel = in.readString();
        score = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(imageUrl);
        dest.writeString(applinks);
        dest.writeString(type);
        dest.writeInt(categoryId);
        dest.writeString(categoryLabel);
        dest.writeInt(score);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LayoutRows> CREATOR = new Creator<LayoutRows>() {
        @Override
        public LayoutRows createFromParcel(Parcel in) {
            return new LayoutRows(in);
        }

        @Override
        public LayoutRows[] newArray(int size) {
            return new LayoutRows[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getApplinks() {
        return applinks;
    }

    public void setApplinks(String applinks) {
        this.applinks = applinks;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryLabel() {
        return categoryLabel;
    }

    public void setCategoryLabel(String categoryLabel) {
        this.categoryLabel = categoryLabel;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Map<String, Object> getHomePageEnhanceDataLayer(int position, String customName) {
        return DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", "homepage",
                "eventAction", "beli ini itu favorite category click",
                "eventLabel", String.format("%s - %s", getCategoryId(), getName()),
                "ecommerce", DataLayer.mapOf(
                        "promoClick", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", getId(),
                                                "name", customName,
                                                "creative", getName(),
                                                "position", String.valueOf(position)
                                        )
                                )
                        )
                )
        );
    }
}
