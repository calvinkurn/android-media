
package com.tokopedia.tokocash.qrpayment.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 11/15/17.
 */

public class ActionBalance implements Parcelable {

    private String redirectUrl;
    private String labelAction;
    private String applinks;
    private String visibility;

    public ActionBalance() {
    }

    protected ActionBalance(Parcel in) {
        redirectUrl = in.readString();
        labelAction = in.readString();
        applinks = in.readString();
        visibility = in.readString();
    }

    public static final Creator<ActionBalance> CREATOR = new Creator<ActionBalance>() {
        @Override
        public ActionBalance createFromParcel(Parcel in) {
            return new ActionBalance(in);
        }

        @Override
        public ActionBalance[] newArray(int size) {
            return new ActionBalance[size];
        }
    };

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getLabelAction() {
        return labelAction;
    }

    public void setLabelAction(String labelAction) {
        this.labelAction = labelAction;
    }

    public String getApplinks() {
        return applinks;
    }

    public void setApplinks(String applinks) {
        this.applinks = applinks;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(redirectUrl);
        parcel.writeString(labelAction);
        parcel.writeString(applinks);
        parcel.writeString(visibility);
    }
}
