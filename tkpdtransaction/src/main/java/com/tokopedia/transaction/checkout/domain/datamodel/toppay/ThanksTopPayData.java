package com.tokopedia.transaction.checkout.domain.datamodel.toppay;

/**
 * @author anggaprasetiyo on 06/03/18.
 */

public class ThanksTopPayData {
    private boolean isSuccess;
    private boolean message;

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isMessage() {
        return message;
    }

    private ThanksTopPayData(Builder builder) {
        isSuccess = builder.isSuccess;
        message = builder.message;
    }


    public static final class Builder {
        private boolean isSuccess;
        private boolean message;

        public Builder() {
        }

        public Builder isSuccess(boolean val) {
            isSuccess = val;
            return this;
        }

        public Builder message(boolean val) {
            message = val;
            return this;
        }

        public ThanksTopPayData build() {
            return new ThanksTopPayData(this);
        }
    }
}
