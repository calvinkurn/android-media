package com.tokopedia.session.register.view.viewmodel.createpassword;

/**
 * @author by nisie on 10/16/17.
 */

public class CreatePasswordViewModel {
    private final boolean isSuccess;

    public CreatePasswordViewModel(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
