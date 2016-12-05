package com.tokopedia.transaction.cart.model;

/**
 * @author anggaprasetiyo on 11/21/16.
 */

public class CartDropshipper {

    private String cartString;
    private String dropShipperName;
    private String dropShipperPhone;

    public String getCartString() {
        return cartString;
    }

    public void setCartString(String cartString) {
        this.cartString = cartString;
    }

    public String getDropShipperName() {
        return dropShipperName;
    }

    public void setDropShipperName(String dropShipperName) {
        this.dropShipperName = dropShipperName;
    }

    public String getDropShipperPhone() {
        return dropShipperPhone;
    }

    public void setDropShipperPhone(String dropShipperPhone) {
        this.dropShipperPhone = dropShipperPhone;
    }

    @Override
    public int hashCode() {
        return cartString.hashCode();
    }

    @Override
    public boolean equals(Object data) {
        if (!(data instanceof CartDropshipper)) {
            return false;
        }
        CartDropshipper cartDropshipper = (CartDropshipper) data;
        return this.cartString.equals(cartDropshipper.getCartString());
    }
}
