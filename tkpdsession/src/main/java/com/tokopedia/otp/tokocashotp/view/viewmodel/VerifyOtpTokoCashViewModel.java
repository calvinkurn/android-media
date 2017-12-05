package com.tokopedia.otp.tokocashotp.view.viewmodel;

import com.tokopedia.session.login.loginphonenumber.view.viewmodel.AccountTokocash;

import java.util.ArrayList;

/**
 * @author by nisie on 12/4/17.
 */

public class VerifyOtpTokoCashViewModel {

    private String key;
    private boolean hasTokocashAccount;
    private boolean isVerified;
    private ArrayList<AccountTokocash> list;

    public VerifyOtpTokoCashViewModel(String key,
                                      boolean hasTokocashAccount,
                                      boolean isVerified, ArrayList<AccountTokocash> list) {
        this.key = key;
        this.hasTokocashAccount = hasTokocashAccount;
        this.isVerified = isVerified;
        this.list = list;
    }

    public String getKey() {
        return key;
    }

    public boolean isHasTokocashAccount() {
        return hasTokocashAccount;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public ArrayList<AccountTokocash> getList() {
        return list;
    }

}
