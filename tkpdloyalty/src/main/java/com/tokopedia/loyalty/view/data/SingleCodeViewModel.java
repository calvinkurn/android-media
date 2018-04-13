package com.tokopedia.loyalty.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Aghny A. Putra on 23/03/18
 */

public class SingleCodeViewModel implements Parcelable {

    private String promoName;
    private String singleCode;

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public String getSingleCode() {
        return singleCode;
    }

    public void setSingleCode(String singleCode) {
        this.singleCode = singleCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.promoName);
        dest.writeString(this.singleCode);
    }

    public SingleCodeViewModel() {
    }

    protected SingleCodeViewModel(Parcel in) {
        this.promoName = in.readString();
        this.singleCode = in.readString();
    }

    public static final Creator<SingleCodeViewModel> CREATOR = new Creator<SingleCodeViewModel>() {
        @Override
        public SingleCodeViewModel createFromParcel(Parcel source) {
            return new SingleCodeViewModel(source);
        }

        @Override
        public SingleCodeViewModel[] newArray(int size) {
            return new SingleCodeViewModel[size];
        }
    };
}
