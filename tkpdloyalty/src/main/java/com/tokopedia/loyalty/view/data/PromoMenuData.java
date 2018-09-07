package com.tokopedia.loyalty.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public class PromoMenuData implements Parcelable {
    private String title;
    private String menuId;
    private String iconActive;
    private String iconNormal;
    private List<PromoSubMenuData> promoSubMenuDataList = new ArrayList<>();
    private String allSubCategoryId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public PromoMenuData() {
    }

    public String getIconActive() {
        return iconActive;
    }

    public void setIconActive(String iconActive) {
        this.iconActive = iconActive;
    }

    public String getIconNormal() {
        return iconNormal;
    }

    public void setIconNormal(String iconNormal) {
        this.iconNormal = iconNormal;
    }

    public List<PromoSubMenuData> getPromoSubMenuDataList() {
        return promoSubMenuDataList;
    }

    public void setPromoSubMenuDataList(List<PromoSubMenuData> promoSubMenuDataList) {
        this.promoSubMenuDataList = promoSubMenuDataList;
    }

    public String getAllSubCategoryId() {
        return allSubCategoryId;
    }

    public void setAllSubCategoryId(String allSubCategoryId) {
        this.allSubCategoryId = allSubCategoryId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.menuId);
        dest.writeString(this.iconActive);
        dest.writeString(this.iconNormal);
        dest.writeTypedList(this.promoSubMenuDataList);
        dest.writeString(this.allSubCategoryId);
    }

    protected PromoMenuData(Parcel in) {
        this.title = in.readString();
        this.menuId = in.readString();
        this.iconActive = in.readString();
        this.iconNormal = in.readString();
        this.promoSubMenuDataList = in.createTypedArrayList(PromoSubMenuData.CREATOR);
        this.allSubCategoryId = in.readString();
    }

    public static final Creator<PromoMenuData> CREATOR = new Creator<PromoMenuData>() {
        @Override
        public PromoMenuData createFromParcel(Parcel source) {
            return new PromoMenuData(source);
        }

        @Override
        public PromoMenuData[] newArray(int size) {
            return new PromoMenuData[size];
        }
    };
}
