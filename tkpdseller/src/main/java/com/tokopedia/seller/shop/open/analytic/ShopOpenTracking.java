package com.tokopedia.seller.shop.open.analytic;

import com.tokopedia.seller.SellerModuleRouter;

/**
 * Created by zulfikarrahman on 1/8/18.
 */

public class ShopOpenTracking {
    private final SellerModuleRouter sellerModuleRouter;

    public ShopOpenTracking(SellerModuleRouter sellerModuleRouter) {
        this.sellerModuleRouter = sellerModuleRouter;
    }

    public void eventOpenShop(String category, String action, String label){
        sellerModuleRouter.sendEventTracking(
                ShopOpenTrackingConstant.CLICK_CREATE_SHOP,
                category,
                action, 
                label
                );
    }

    public void eventOpenShopBiodataSuccess(){
        eventOpenShop(
                ShopOpenTrackingConstant.OPEN_SHOP_BIODATA_FORM,
                ShopOpenTrackingConstant.OPEN_SHOP_SUCCESS,
                "");
    }

    public void eventOpenShopBiodataError(String labelError){
        eventOpenShop(
                ShopOpenTrackingConstant.OPEN_SHOP_BIODATA_FORM,
                ShopOpenTrackingConstant.OPEN_SHOP_ERROR,
                labelError
        );
    }

    public void eventOpenShopBiodataErrorWithData(String labelError){
        eventOpenShop(
                ShopOpenTrackingConstant.OPEN_SHOP_BIODATA_FORM,
                ShopOpenTrackingConstant.OPEN_SHOP_ERROR_WITH_DATA,
                labelError
        );
    }

    public void eventOpenShopBiodataNameError(String labelError){
        eventOpenShop(
                ShopOpenTrackingConstant.OPEN_SHOP_BIODATA_FORM,
                ShopOpenTrackingConstant.OPEN_SHOP_ERROR_NAME,
                labelError
        );
    }

    public void eventOpenShopBiodataDomainError(String labelError){
        eventOpenShop(
                ShopOpenTrackingConstant.OPEN_SHOP_BIODATA_FORM,
                ShopOpenTrackingConstant.OPEN_SHOP_ERROR_DOMAIN,
                labelError
        );
    }

    public void eventOpenShopFormSuccess(){
        eventOpenShop(
                ShopOpenTrackingConstant.OPEN_SHOP_INFO_FORM,
                ShopOpenTrackingConstant.OPEN_SHOP_CLICK_NEXT_STEP_SUCCESS,
                ""
        );
    }

    public void eventOpenShopFormError(String labelError){
        eventOpenShop(
                ShopOpenTrackingConstant.OPEN_SHOP_INFO_FORM,
                ShopOpenTrackingConstant.OPEN_SHOP_CLICK_NEXT_STEP_ERROR,
                labelError
        );
    }

    public void eventOpenShopShippingSuccess(){
        eventOpenShop(
                ShopOpenTrackingConstant.OPEN_SHOP_SHIPPING_FORM,
                ShopOpenTrackingConstant.OPEN_SHOP_CLICK_NEXT_STEP_SUCCESS,
                ""
        );
    }

    public void eventOpenShopShippingError(String labelError){
        eventOpenShop(
                ShopOpenTrackingConstant.OPEN_SHOP_SHIPPING_FORM,
                ShopOpenTrackingConstant.OPEN_SHOP_CLICK_NEXT_STEP_ERROR,
                labelError
        );
    }

    public void eventOpenShopShippingServices(String courierName){
        eventOpenShop(
                ShopOpenTrackingConstant.OPEN_SHOP_SHIPPING_FORM,
                ShopOpenTrackingConstant.OPEN_SHOP_CLICK_CARGO_SERVICES,
                courierName
        );
    }

    public void eventOpenShopThanksClickLearnMore(){
        eventOpenShop(
                ShopOpenTrackingConstant.OPEN_SHOP_THANKS_PAGE,
                ShopOpenTrackingConstant.OPEN_SHOP_CLICK_LEARN_MORE,
                ""
        );
    }

    public void eventOpenShopThanksClickAddProduct(){
        eventOpenShop(
                ShopOpenTrackingConstant.OPEN_SHOP_THANKS_PAGE,
                ShopOpenTrackingConstant.OPEN_SHOP_CLICK_ADD_PRODUCT,
                ""
        );
    }

    public void eventOpenShopThanksGoToMyShop(){
        eventOpenShop(
                ShopOpenTrackingConstant.OPEN_SHOP_THANKS_PAGE,
                ShopOpenTrackingConstant.OPEN_SHOP_CLICK_MY_SHOP_PAGE,
                ""
        );
    }

    public void eventOpenShopLocationForm(String addressDetail){
        eventOpenShop(
                ShopOpenTrackingConstant.OPEN_SHOP_SHOP_LOCATION_FORM,
                ShopOpenTrackingConstant.OPEN_SHOP_CLICK_ADDRESS_LIST,
                addressDetail
        );
    }

    public void eventOpenShopPinPointSelected(){
        eventOpenShop(
                ShopOpenTrackingConstant.OPEN_SHOP_SHOP_LOCATION_FORM,
                ShopOpenTrackingConstant.OPEN_SHOP_CLICK_PINPOINT_LOCATION,
                ""
        );
    }

    public void eventOpenShopPinPointDeleted(){
        eventOpenShop(
                ShopOpenTrackingConstant.OPEN_SHOP_SHOP_LOCATION_FORM,
                ShopOpenTrackingConstant.OPEN_SHOP_CLICK_DELETE_PINPOINT_LOCATION,
                ""
        );
    }

    public void eventOpenShopLocationNext(){
        eventOpenShop(
                ShopOpenTrackingConstant.OPEN_SHOP_SHOP_LOCATION_FORM,
                ShopOpenTrackingConstant.OPEN_SHOP_CLICK_NEXT_STEP_SUCCESS,
                ""
        );
    }

    public void eventOpenShopLocationError(String labelError){
        eventOpenShop(
                ShopOpenTrackingConstant.OPEN_SHOP_SHOP_LOCATION_FORM,
                ShopOpenTrackingConstant.OPEN_SHOP_CLICK_NEXT_STEP_ERROR,
                labelError
        );
    }

    public void eventOpenShopLocationErrorWithData(String labelError){
        eventOpenShop(
                ShopOpenTrackingConstant.OPEN_SHOP_SHOP_LOCATION_FORM,
                ShopOpenTrackingConstant.OPEN_SHOP_CLICK_NEXT_STEP_ERROR_WITH_DATA,
                labelError
        );
    }

    public void eventOpenShopDomainReserveNext(String label) {
        eventOpenShop(ShopOpenTrackingConstant.OPEN_SHOP_DOMAIN_RESERVE,
                ShopOpenTrackingConstant.OPEN_SHOP_CLICK_NEXT_STEP,
                label);
    }
}
