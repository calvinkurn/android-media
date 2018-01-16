package com.tokopedia.otp.tokocashotp.view.viewmodel;

import com.tokopedia.session.login.loginphonenumber.view.viewmodel.AccountTokocash;

import java.util.ArrayList;

/**
 * @author by nisie on 12/4/17.
 */

public class VerifyOtpTokoCashViewModel {

    private String key;
    private boolean isVerified;
    private ArrayList<AccountTokocash> list;

    public VerifyOtpTokoCashViewModel(String key,
                                      boolean isVerified, ArrayList<AccountTokocash> list) {
        this.key = key;
        this.isVerified = isVerified;
        this.list = list;
    }

    public String getKey() {
        return key;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public ArrayList<AccountTokocash> getList() {
        return list;
    }

}
