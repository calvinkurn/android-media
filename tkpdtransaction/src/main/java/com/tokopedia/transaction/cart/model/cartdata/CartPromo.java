package com.tokopedia.transaction.cart.model.cartdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 7/27/17. Tokopedia
 */

public class CartPromo implements Parcelable{

    @SerializedName("is_visible")
    private boolean isVisible;

    @SerializedName("promo_text")
    private String promoText;

    @SerializedName("cta_text")
    private String ctaText;

    @SerializedName("cta_color")
    private String ctaColor;

    public CartPromo() {

    }

    protected CartPromo(Parcel in) {
        isVisible = in.readByte() != 0;
        promoText = in.readString();
        ctaText = in.readString();
        ctaColor = in.readString();
    }

    public static final Creator<CartPromo> CREATOR = new Creator<CartPromo>() {
        @Override
        public CartPromo createFromParcel(Parcel in) {
            return new CartPromo(in);
        }

        @Override
        public CartPromo[] newArray(int size) {
            return new CartPromo[size];
        }
    };

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public String getPromoText() {
        return promoText;
    }

    public void setPromoText(String promoText) {
        this.promoText = promoText;
    }

    public String getCtaText() {
        return ctaText;
    }

    public void setCtaText(String ctaText) {
        this.ctaText = ctaText;
    }

    public String getCtaColor() {
        return ctaColor;
    }

    public void setCtaColor(String ctaColor) {
        this.ctaColor = ctaColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isVisible ? 1 : 0));
        dest.writeString(promoText);
        dest.writeString(ctaText);
        dest.writeString(ctaColor);
    }
}
