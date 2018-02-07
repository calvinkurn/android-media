package com.tokopedia.session.login.loginphonenumber.view.viewmodel;

/**
 * @author by nisie on 12/6/17.
 */

public class CheckMsisdnTokoCashViewModel {
    private final boolean isTokoCashAccountExist;
    private final boolean isTokopediaAccountExist;

    public CheckMsisdnTokoCashViewModel(boolean tokocashAccountExist, boolean tokopediaAccountExist) {
        this.isTokoCashAccountExist = tokocashAccountExist;
        this.isTokopediaAccountExist = tokopediaAccountExist;
    }

    public boolean isTokoCashAccountExist() {
        return isTokoCashAccountExist;
    }

    public boolean isTokopediaAccountExist() {
        return isTokopediaAccountExist;
    }
}
