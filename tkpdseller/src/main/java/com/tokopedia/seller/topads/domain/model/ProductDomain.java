package com.tokopedia.seller.topads.domain.model;

import android.os.Parcel;

/**
 * @author normansyahputa on 2/20/17.
 */

public class ProductDomain extends GenericClass {
    private int id;
    private String name;
    private String imageUrl;
    private boolean isPromoted;
    private int adId;
    private String groupName;


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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPromoted() {
        return isPromoted;
    }

    public void setPromoted(boolean promoted) {
        isPromoted = promoted;
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.imageUrl);
        dest.writeByte(this.isPromoted ? (byte) 1 : (byte) 0);
        dest.writeInt(this.adId);
        dest.writeString(this.groupName);
        dest.writeString(super.getClassName());
    }

    public ProductDomain() {
        super(ProductDomain.class.getName());
    }

    protected ProductDomain(Parcel in) {
        super(in);
        this.id = in.readInt();
        this.name = in.readString();
        this.imageUrl = in.readString();
        this.isPromoted = in.readByte() != 0;
        this.adId = in.readInt();
        this.groupName = in.readString();
        setClassName(in.readString());
    }

    public static final Creator<ProductDomain> CREATOR = new Creator<ProductDomain>() {
        @Override
        public ProductDomain createFromParcel(Parcel source) {
            return new ProductDomain(source);
        }

        @Override
        public ProductDomain[] newArray(int size) {
            return new ProductDomain[size];
        }
    };
}
