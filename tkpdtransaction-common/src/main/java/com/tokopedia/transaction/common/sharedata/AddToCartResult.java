package com.tokopedia.transaction.common.sharedata;

/**
 * @author anggaprasetiyo on 13/02/18.
 */

public class AddToCartResult {
    private boolean success;
    private String message;
    private String cartId;
    private String source;

    public AddToCartResult() {
    }

    private AddToCartResult(Builder builder) {
        setSuccess(builder.success);
        setMessage(builder.message);
        setCartId(builder.cartId);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCartId() {
        return cartId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public static final class Builder {
        private boolean success;
        private String message;
        private String cartId;

        public Builder() {
        }

        public Builder success(boolean val) {
            success = val;
            return this;
        }

        public Builder message(String val) {
            message = val;
            return this;
        }

        public Builder cartId(String val) {
            cartId = val;
            return this;
        }

        public AddToCartResult build() {
            return new AddToCartResult(this);
        }
    }
}
