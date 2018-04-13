package com.tokopedia.otp.cotp.view.viewmodel;

import java.util.List;

/**
 * @author by nisie on 1/18/18.
 */

public class ListVerificationMethod {

    List<MethodItem> list;

    public ListVerificationMethod(List<MethodItem> list) {
        this.list = list;
    }

    public List<MethodItem> getList() {
        return list;
    }
}
