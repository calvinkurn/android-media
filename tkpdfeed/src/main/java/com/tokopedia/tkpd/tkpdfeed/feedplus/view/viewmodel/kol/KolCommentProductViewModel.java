package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 11/2/17.
 */

public class KolCommentProductViewModel implements Parcelable{

    private String imageUrl;
    private String name;
    private String price;
    private boolean isWishlisted;

    public KolCommentProductViewModel(String imageUrl, String name, String price, boolean
            isWishlisted) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.isWishlisted = isWishlisted;
    }

    protected KolCommentProductViewModel(Parcel in) {
        imageUrl = in.readString();
        name = in.readString();
        price = in.readString();
        isWishlisted = in.readByte() != 0;
    }

    public static final Creator<KolCommentProductViewModel> CREATOR = new Creator<KolCommentProductViewModel>() {
        @Override
        public KolCommentProductViewModel createFromParcel(Parcel in) {
            return new KolCommentProductViewModel(in);
        }

        @Override
        public KolCommentProductViewModel[] newArray(int size) {
            return new KolCommentProductViewModel[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public boolean isWishlisted() {
        return isWishlisted;
    }

    public void setWishlist(boolean isWishlisted) {
        this.isWishlisted = isWishlisted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeByte((byte) (isWishlisted ? 1 : 0));
    }
}
