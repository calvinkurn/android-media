package com.tokopedia.events.domain.model;

/**
 * Created by pranaymohapatra on 04/04/18.
 */

public class LikeUpdateResultDomain {
    String message;
    int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
