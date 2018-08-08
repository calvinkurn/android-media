package com.tokopedia.session.register.registerphonenumber.domain.model;

/**
 * @author by yfsx on 28/02/18.
 */

public class CheckMsisdnDomain {

    private boolean isExist;

    private String phoneText;

    public CheckMsisdnDomain(boolean isExist, String phoneText) {
        this.isExist = isExist;
        this.phoneText = phoneText;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }

    public String getPhoneText() {
        return phoneText;
    }

    public void setPhoneText(String phoneText) {
        this.phoneText = phoneText;
    }
}
