package com.tokopedia.seller.shop.common.tracking;

import com.tokopedia.seller.SellerModuleRouter;

/**
 * Created by zulfikarrahman on 1/8/18.
 */

public class TrackingOpenShop {
    private final SellerModuleRouter sellerModuleRouter;

    public TrackingOpenShop(SellerModuleRouter sellerModuleRouter) {
        this.sellerModuleRouter = sellerModuleRouter;
    }

    public void eventOpenShop(String category, String action, String label){
        sellerModuleRouter.sendEventTracking(
                ConstantTrackingOpenShop.CLICK_CREATE_SHOP,
                category,
                action, 
                label
                );
    }

    public void eventOpenShopBiodataSuccess(){
        eventOpenShop(
                ConstantTrackingOpenShop.OPEN_SHOP_BIODATA_FORM,
                ConstantTrackingOpenShop.OPEN_SHOP_SUCCESS,
                "");
    }

    public void eventOpenShopBiodataError(String labelError){
        eventOpenShop(
                ConstantTrackingOpenShop.OPEN_SHOP_BIODATA_FORM,
                ConstantTrackingOpenShop.OPEN_SHOP_ERROR,
                labelError
        );
    }

    public void eventOpenShopBiodataNameError(String labelError){
        eventOpenShop(
                ConstantTrackingOpenShop.OPEN_SHOP_BIODATA_FORM,
                ConstantTrackingOpenShop.OPEN_SHOP_ERROR_NAME,
                labelError
        );
    }

    public void eventOpenShopBiodataDomainError(String labelError){
        eventOpenShop(
                ConstantTrackingOpenShop.OPEN_SHOP_BIODATA_FORM,
                ConstantTrackingOpenShop.OPEN_SHOP_ERROR_DOMAIN,
                labelError
        );
    }

    public void eventOpenShopFormSuccess(){
        eventOpenShop(
                ConstantTrackingOpenShop.OPEN_SHOP_INFO_FORM,
                ConstantTrackingOpenShop.OPEN_SHOP_CLICK_NEXT_STEP_SUCCESS,
                ""
        );
    }

    public void eventOpenShopFormError(String labelError){
        eventOpenShop(
                ConstantTrackingOpenShop.OPEN_SHOP_INFO_FORM,
                ConstantTrackingOpenShop.OPEN_SHOP_CLICK_NEXT_STEP_ERROR,
                labelError
        );
    }

    public void eventOpenShopShippingSuccess(){
        eventOpenShop(
                ConstantTrackingOpenShop.OPEN_SHOP_SHIPPING_FORM,
                ConstantTrackingOpenShop.OPEN_SHOP_CLICK_NEXT_STEP_SUCCESS,
                ""
        );
    }

    public void eventOpenShopShippingError(String labelError){
        eventOpenShop(
                ConstantTrackingOpenShop.OPEN_SHOP_SHIPPING_FORM,
                ConstantTrackingOpenShop.OPEN_SHOP_CLICK_NEXT_STEP_ERROR,
                labelError
        );
    }

    public void eventOpenShopShippingServices(String courierName){
        eventOpenShop(
                ConstantTrackingOpenShop.OPEN_SHOP_SHIPPING_FORM,
                ConstantTrackingOpenShop.OPEN_SHOP_CLICK_CARGO_SERVICES,
                courierName
        );
    }

    public void eventOpenShopThanksClickLearnMore(){
        eventOpenShop(
                ConstantTrackingOpenShop.OPEN_SHOP_THANKS_PAGE,
                ConstantTrackingOpenShop.OPEN_SHOP_CLICK_LEARN_MORE,
                ""
        );
    }

    public void eventOpenShopThanksClickAddProduct(){
        eventOpenShop(
                ConstantTrackingOpenShop.OPEN_SHOP_THANKS_PAGE,
                ConstantTrackingOpenShop.OPEN_SHOP_CLICK_ADD_PRODUCT,
                ""
        );
    }

    public void eventOpenShopThanksGoToMyShop(){
        eventOpenShop(
                ConstantTrackingOpenShop.OPEN_SHOP_THANKS_PAGE,
                ConstantTrackingOpenShop.OPEN_SHOP_CLICK_MY_SHOP_PAGE,
                ""
        );
    }

    public void eventOpenShopLocationForm(String addressDetail){
        eventOpenShop(
                ConstantTrackingOpenShop.OPEN_SHOP_SHOP_LOCATION_FORM,
                ConstantTrackingOpenShop.OPEN_SHOP_CLICK_ADDRESS_LIST,
                addressDetail
        );
    }

    public void eventOpenShopPinPointLocation(){
        eventOpenShop(
                ConstantTrackingOpenShop.OPEN_SHOP_SHOP_LOCATION_FORM,
                ConstantTrackingOpenShop.OPEN_SHOP_CLICK_PINPOINT_LOCATION,
                ""
        );
    }

    public void eventOpenShopLocationNext(){
        eventOpenShop(
                ConstantTrackingOpenShop.OPEN_SHOP_SHOP_LOCATION_FORM,
                ConstantTrackingOpenShop.OPEN_SHOP_CLICK_NEXT_STEP_SUCCESS,
                ""
        );
    }

    public void eventOpenShopLocationError(String error){
        eventOpenShop(
                ConstantTrackingOpenShop.OPEN_SHOP_SHOP_LOCATION_FORM,
                ConstantTrackingOpenShop.OPEN_SHOP_CLICK_NEXT_STEP_ERROR,
                ""
        );
    }
}
