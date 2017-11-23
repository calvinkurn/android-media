package com.tokopedia.transaction.cart.model.cartdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 7/27/17. Tokopedia
 */

public class CartPromo implements Parcelable{

    @SerializedName("is_visible")
    private int isVisible;

    @SerializedName("text")
    private String promoText;

    @SerializedName("promo_code")
    private String promoCode;

    @SerializedName("cta")
    private String ctaText;

    @SerializedName("cta_color")
    private String ctaColor;

    public CartPromo() {

    }

    protected CartPromo(Parcel in) {
        isVisible = in.readInt();
        promoText = in.readString();
        promoCode = in.readString();
        ctaText = in.readString();
        ctaColor = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(isVisible);
        dest.writeString(promoText);
        dest.writeString(promoCode);
        dest.writeString(ctaText);
        dest.writeString(ctaColor);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public int isVisible() {
        return isVisible;
    }

    public void setVisible(int visible) {
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

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

}
