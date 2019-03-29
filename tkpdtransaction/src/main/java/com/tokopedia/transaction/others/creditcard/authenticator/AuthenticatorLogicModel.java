package com.tokopedia.transaction.others.creditcard.authenticator;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kris on 10/10/17. Tokopedia
 */

public class AuthenticatorLogicModel {

    private boolean selected;

    private String auhtenticationTitle;

    private String authenticationDescription;

    private int stateWhenSelected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getAuhtenticationTitle() {
        return auhtenticationTitle;
    }

    public void setAuhtenticationTitle(String auhtenticationTitle) {
        this.auhtenticationTitle = auhtenticationTitle;
    }

    public String getAuthenticationDescription() {
        return authenticationDescription;
    }

    public void setAuthenticationDescription(String authenticationDescription) {
        this.authenticationDescription = authenticationDescription;
    }

    public int getStateWhenSelected() {
        return stateWhenSelected;
    }

    public void setStateWhenSelected(int stateWhenSelected) {
        this.stateWhenSelected = stateWhenSelected;
    }
}
