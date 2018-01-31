package com.tokopedia.transaction.checkout.view.data.factory;

import com.tokopedia.transaction.checkout.view.data.CartSingleAddressData;

/**
 * @author Aghny A. Putra on 31/01/18
 */

public class CartSingleAddressDataFactory {

    public CartSingleAddressDataFactory() {

    }

    public CartSingleAddressData getDummyCartSingleAddressData() {
        return createDummyCartSingleAddressData();
    }

    private CartSingleAddressData createDummyCartSingleAddressData() {
        CartSingleAddressData cartSingleAddressData = new CartSingleAddressData();

        cartSingleAddressData.setCartItemModelList(CartItemModelFactory.getDummyCartItemModelList());
        cartSingleAddressData.setCartPayableDetailModel(CartPayableDetailModelFactory.getCartPayableDetailModel());
        cartSingleAddressData.setDropshipperShippingOptionModel(DropshipperShippingOptionModelFactory.getDummyDropshippingOptionModel());
        cartSingleAddressData.setShippingFeeBannerModel(ShippingFeeBannerModelFactory.getDummyShippingFeeBannerModel());
        cartSingleAddressData.setShippingRecipientModel(ShippingRecipientModelFactory.getDummyShippingRecipientModel());

        return cartSingleAddressData;
    }

}
