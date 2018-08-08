package com.tokopedia.transaction.cart.model.cancelcart;


import com.tokopedia.transaction.cart.model.cartdata.CartData;

/**
 * Created by Angga.Prasetiyo on 30/05/2016.
 */
public class CancelCartData {
    private static final String TAG = CancelCartData.class.getSimpleName();

    private boolean isSuccessCancel;
    private boolean isSuccessRefresh;
    private CartData cartData;
    private String messageCancelCart = "";
    private String messageRefreshCart = "";

    public boolean isSuccessCancel() {
        return isSuccessCancel;
    }

    public void setSuccessCancel(boolean successCancel) {
        isSuccessCancel = successCancel;
    }

    public boolean isSuccessRefresh() {
        return isSuccessRefresh;
    }

    public void setSuccessRefresh(boolean successRefresh) {
        isSuccessRefresh = successRefresh;
    }

    public CartData getCartData() {
        return cartData;
    }

    public void setCartData(CartData cartData) {
        this.cartData = cartData;
    }

    public String getMessageCancelCart() {
        return messageCancelCart;
    }

    public void setMessageCancelCart(String messageCancelCart) {
        this.messageCancelCart = messageCancelCart;
    }

    public String getMessageRefreshCart() {
        return messageRefreshCart;
    }

    public void setMessageRefreshCart(String messageRefreshCart) {
        this.messageRefreshCart = messageRefreshCart;
    }
}
