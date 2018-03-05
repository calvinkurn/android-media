package com.tokopedia.otp.registerphonenumber.view.viewmodel;


import com.tokopedia.session.login.loginphonenumber.view.viewmodel.AccountTokocash;

import java.util.ArrayList;

/**
 * @author by yfsx on 5/3/18.
 */

public class VerifyOtpViewModel {

    private String key;
    private boolean isVerified;
    private ArrayList<AccountTokocash> list;

    public VerifyOtpViewModel(String key,
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
