package com.tokopedia.transaction.checkout.view.data;

import java.util.List;

/**
 * The type Single address product shipment data.
 *
 * @author Aghny A. Putra on 25/01/18
 */
public class CartSingleAddressData {

    private ShippingFeeBannerModel shippingFeeBannerModel;
    private ShippingRecipientModel shippingRecipientModel;
    private DropshipperShippingOptionModel dropshipperShippingOptionModel;
    private List<CartItemModel> cartItemModelList;
    private CartPayableDetailModel cartPayableDetailModel;

    public ShippingFeeBannerModel getShippingFeeBannerModel() {
        return shippingFeeBannerModel;
    }

    public void setShippingFeeBannerModel(ShippingFeeBannerModel shippingFeeBannerModel) {
        this.shippingFeeBannerModel = shippingFeeBannerModel;
    }

    public ShippingRecipientModel getShippingRecipientModel() {
        return shippingRecipientModel;
    }

    public void setShippingRecipientModel(ShippingRecipientModel shippingRecipientModel) {
        this.shippingRecipientModel = shippingRecipientModel;
    }

    public DropshipperShippingOptionModel getDropshipperShippingOptionModel() {
        return dropshipperShippingOptionModel;
    }

    public void setDropshipperShippingOptionModel(DropshipperShippingOptionModel dropshipperShippingOptionModel) {
        this.dropshipperShippingOptionModel = dropshipperShippingOptionModel;
    }

    public List<CartItemModel> getCartItemModelList() {
        return cartItemModelList;
    }

    public void setCartItemModelList(List<CartItemModel> cartItemModelList) {
        this.cartItemModelList = cartItemModelList;
    }

    public CartPayableDetailModel getCartPayableDetailModel() {
        return cartPayableDetailModel;
    }

    public void setCartPayableDetailModel(CartPayableDetailModel cartPayableDetailModel) {
        this.cartPayableDetailModel = cartPayableDetailModel;
    }

}
