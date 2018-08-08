package com.tokopedia.session.register.domain.model;

/**
 * @author by nisie on 10/16/17.
 */

public class CreatePasswordDomain {
    private final boolean isSuccess;

    public CreatePasswordDomain(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
