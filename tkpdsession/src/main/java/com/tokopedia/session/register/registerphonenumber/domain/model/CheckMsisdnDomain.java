package com.tokopedia.session.register.registerphonenumber.domain.model;

/**
 * @author by yfsx on 28/02/18.
 */

public class CheckMsisdnDomain {

    private boolean isExist;

    public CheckMsisdnDomain(boolean isExist) {
        this.isExist = isExist;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }

}
