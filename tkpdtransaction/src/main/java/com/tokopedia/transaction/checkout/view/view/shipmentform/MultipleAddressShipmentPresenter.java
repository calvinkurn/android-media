package com.tokopedia.transaction.checkout.view.view.shipmentform;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.transaction.checkout.data.entity.request.CheckoutRequest;
import com.tokopedia.transaction.checkout.data.entity.request.DataCheckoutRequest;
import com.tokopedia.transaction.checkout.data.entity.request.DropshipDataCheckoutRequest;
import com.tokopedia.transaction.checkout.data.entity.request.ProductDataCheckoutRequest;
import com.tokopedia.transaction.checkout.data.entity.request.ShippingInfoCheckoutRequest;
import com.tokopedia.transaction.checkout.data.entity.request.ShopProductCheckoutRequest;
import com.tokopedia.transaction.checkout.data.mapper.ShipmentRatesDataMapper;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressPriceSummaryData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressShipmentAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentDetailData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.GroupAddress;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.GroupShop;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.Product;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.transaction.checkout.domain.usecase.ICartListInteractor;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemPromoHolderData;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public class MultipleAddressShipmentPresenter implements IMultipleAddressShipmentPresenter {

    private final IMultipleAddressShipmentView view;
    private final ICartListInteractor cartListInteractor;

    @Inject
    public MultipleAddressShipmentPresenter(IMultipleAddressShipmentView view,
                                            ICartListInteractor cartListInteractor) {
        this.view = view;
        this.cartListInteractor = cartListInteractor;
    }

    @Override
    public CheckoutRequest generateCheckoutRequest(List<MultipleAddressShipmentAdapterData> shipmentData,
                                                   MultipleAddressPriceSummaryData priceData) {
        CheckoutRequest.Builder checkoutRequest = new CheckoutRequest.Builder();
        List<DataCheckoutRequest> dataCheckoutRequests = new ArrayList<>();
        for (int i = 0; i < shipmentData.size(); i++) {
            MultipleAddressShipmentAdapterData currentShipmentAdapterData = shipmentData.get(i);
            ShipmentDetailData currentShipmentDetailData = currentShipmentAdapterData
                    .getSelectedShipmentDetailData();

            DataCheckoutRequest.Builder checkoutData = new DataCheckoutRequest.Builder();

            List<ProductDataCheckoutRequest> productDataCheckoutRequests = new ArrayList<>();
            ProductDataCheckoutRequest.Builder productDataCheckoutRequest =
                    new ProductDataCheckoutRequest.Builder();
            productDataCheckoutRequests.add(productDataCheckoutRequest
                    .productId(Integer.parseInt(
                            currentShipmentAdapterData.getItemData().getProductId())
                    ).build());

            ShopProductCheckoutRequest.Builder shopCheckoutBuilder;
            shopCheckoutBuilder = new ShopProductCheckoutRequest.Builder()
                    .productData(productDataCheckoutRequests)
                    .shippingInfo(setShippingInfoRequest(currentShipmentDetailData));
            if (currentShipmentDetailData.getUseDropshipper()) {
                shopCheckoutBuilder
                        .dropshipData(setDropshipDataCheckoutRequest(currentShipmentDetailData));
            }

            shopCheckoutBuilder
                    .fcancelPartial(
                            switchValue(currentShipmentAdapterData.isProductFcancelPartial())
                    );
            shopCheckoutBuilder
                    .finsurance(switchValue(currentShipmentAdapterData.isProductFinsurance()));
            shopCheckoutBuilder
                    .isPreorder(switchValue(currentShipmentAdapterData.isProductIsPreorder()));
            shopCheckoutBuilder
                    .isDropship(switchValue(currentShipmentDetailData.getUseDropshipper()));
            shopCheckoutBuilder.shopId(currentShipmentAdapterData.getShopId());

            List<ShopProductCheckoutRequest> shopCheckoutRequests = new ArrayList<>();
            shopCheckoutRequests.add(shopCheckoutBuilder.build());

            checkoutData.addressId(Integer
                    .parseInt(shipmentData.get(i).getItemData().getAddressId()))
                    .shopProducts(shopCheckoutRequests).build();
            dataCheckoutRequests.add(checkoutData.build());
        }
        return checkoutRequest.data(dataCheckoutRequests).build();
    }

    private DropshipDataCheckoutRequest setDropshipDataCheckoutRequest(ShipmentDetailData data) {
        return new DropshipDataCheckoutRequest.Builder()
                .name(data.getDropshipperName())
                .telpNo(data.getDropshipperPhone()).build();
    }

    private ShippingInfoCheckoutRequest setShippingInfoRequest(ShipmentDetailData data) {
        return new ShippingInfoCheckoutRequest.Builder()
                .shippingId(data.getSelectedCourier().getShipperId())
                .spId(data.getSelectedShipment().getServiceId())
                .build();
    }

    @Override
    public List<MultipleAddressShipmentAdapterData> initiateAdapterData(CartShipmentAddressFormData data) {
        List<MultipleAddressShipmentAdapterData> adapterDataList = new ArrayList<>();
        for (int addressIndex = 0; addressIndex < data.getGroupAddress().size(); addressIndex++) {
            GroupAddress currentAddress = data.getGroupAddress().get(addressIndex);
            List<GroupShop> groupShopList = data.getGroupAddress().get(addressIndex).getGroupShop();
            for (int shopIndex = 0; shopIndex < groupShopList.size(); shopIndex++) {
                GroupShop currentGroupShop = groupShopList.get(shopIndex);
                List<Product> productList = currentGroupShop.getProducts();
                for (int productIndex = 0; productIndex < productList.size(); productIndex++) {
                    MultipleAddressShipmentAdapterData adapterData =
                            new MultipleAddressShipmentAdapterData();
                    Product currentProduct = productList.get(productIndex);
                    adapterData.setInvoicePosition(adapterDataList.size());
                    adapterData.setShopId(currentGroupShop.getShop().getShopId());
                    adapterData.setProductName(currentProduct.getProductName());
                    adapterData.setProductPriceNumber(currentProduct.getProductPrice());
                    adapterData.setProductPrice(formatRupiah(currentProduct.getProductPrice()));
                    adapterData.setProductImageUrl(currentProduct.getProductImageSrc200Square());
                    adapterData.setSenderName(currentGroupShop.getShop().getShopName());
                    MultipleAddressItemData addressItemData = new MultipleAddressItemData();
                    addressItemData.setCartPosition(productIndex);
                    addressItemData.setAddressPosition(0);
                    addressItemData.setProductId(String.valueOf(currentProduct.getProductId()));
                    addressItemData.setProductWeight(currentProduct.getProductWeightFmt());
                    addressItemData.setProductRawWeight(currentProduct.getProductWeight());
                    addressItemData.setProductNotes(currentProduct.getProductNotes());
                    addressItemData.setProductQty(
                            String.valueOf(currentProduct.getProductQuantity())
                    );
                    addressItemData.setAddressId(
                            String.valueOf(currentAddress.getUserAddress().getAddressId())
                    );
                    addressItemData.setAddressTitle(currentAddress.getUserAddress()
                            .getAddressName());
                    addressItemData.setAddressReceiverName(currentAddress.getUserAddress()
                            .getReceiverName());
                    addressItemData.setAddressStreet(currentAddress.getUserAddress().getAddress());
                    addressItemData.setAddressCityName(currentAddress.getUserAddress().getCityName());
                    addressItemData.setAddressProvinceName(
                            currentAddress.getUserAddress().getProvinceName()
                    );
                    addressItemData.setRecipientPhoneNumber(
                            currentAddress.getUserAddress().getPhone()
                    );
                    addressItemData.setAddressCountryName(currentAddress.getUserAddress()
                            .getCountry());
                    adapterData.setItemData(addressItemData);
                    adapterData.setShipmentCartData(new ShipmentRatesDataMapper()
                            .getShipmentCartData(data, currentAddress.getUserAddress(),
                                    currentGroupShop, adapterData));

                    adapterData.setProductIsFreeReturns(currentProduct.isProductIsFreeReturns());
                    adapterData.setProductIsPreorder(currentProduct.isProductIsPreorder());
                    adapterData.setProductFcancelPartial(currentProduct.isProductFcancelPartial());
                    adapterData.setProductFinsurance(currentProduct.isProductFinsurance());

                    adapterDataList.add(adapterData);
                }
            }
        }
        return adapterDataList;
    }

    @Override
    public CheckPromoCodeCartShipmentRequest generateCheckPromoRequest(
            List<MultipleAddressShipmentAdapterData> shipmentData, CartItemPromoHolderData appliedPromo
    ) {
        CheckPromoCodeCartShipmentRequest.Builder checkoutPromoRequest =
                new CheckPromoCodeCartShipmentRequest.Builder();

        List<CheckPromoCodeCartShipmentRequest.Data> orderDatas = new ArrayList<>();

        for (int i = 0; i < shipmentData.size(); i++) {
            CheckPromoCodeCartShipmentRequest.Data.Builder orderData =
                    new CheckPromoCodeCartShipmentRequest.Data.Builder();
            orderData.addressId(Integer.parseInt(shipmentData.get(i).getItemData().getAddressId()));

            List<CheckPromoCodeCartShipmentRequest.ShopProduct> shopProducts = new ArrayList<>();
            CheckPromoCodeCartShipmentRequest.ShopProduct.Builder shopProduct =
                    new CheckPromoCodeCartShipmentRequest.ShopProduct.Builder();

            List<CheckPromoCodeCartShipmentRequest.ProductData> productDatas = new ArrayList<>();
            CheckPromoCodeCartShipmentRequest.ProductData.Builder productData =
                    new CheckPromoCodeCartShipmentRequest.ProductData.Builder();
            productData
                    .productId(Integer.parseInt(shipmentData.get(i).getItemData().getProductId()))
                    .productNotes(shipmentData.get(i).getItemData().getProductNotes())
                    .productQuantity(
                            Integer.parseInt(shipmentData.get(i).getItemData().getProductQty()
                            )
                    );
            productDatas.add(productData.build());

            CheckPromoCodeCartShipmentRequest.ShippingInfo.Builder shipmentInfo =
                    new CheckPromoCodeCartShipmentRequest.ShippingInfo.Builder();
            shipmentInfo
                    .shippingId(shipmentData.get(i).getSelectedShipmentDetailData()
                            .getSelectedCourier().getShipperId())
                    .spId(shipmentData.get(i).getSelectedShipmentDetailData()
                            .getSelectedCourier().getShipperProductId());

            shopProduct.productData(productDatas)
                    .shopId(shipmentData.get(i).getShopId())
                    .fcancelPartial(switchValue(shipmentData.get(i).isProductFcancelPartial()))
                    .finsurance(switchValue(shipmentData.get(i).isProductFinsurance()))
                    .isPreorder(switchValue(shipmentData.get(i).isProductIsPreorder()))
                    .shippingInfo(shipmentInfo.build());

            if (shipmentData.get(i).getSelectedShipmentDetailData().getUseDropshipper()) {
                CheckPromoCodeCartShipmentRequest.DropshipData.Builder dropshipData =
                        new CheckPromoCodeCartShipmentRequest.DropshipData.Builder();

                dropshipData
                        .name(shipmentData.get(i).getSelectedShipmentDetailData()
                                .getDropshipperName())
                        .telpNo(shipmentData.get(i).getSelectedShipmentDetailData()
                                .getDropshipperPhone());
            }

            shopProducts.add(shopProduct.build());

            orderData.shopProducts(shopProducts);

            orderDatas.add(orderData.build());
        }

        checkoutPromoRequest.data(orderDatas);
        if (appliedPromo.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_VOUCHER) {
            checkoutPromoRequest.promoCode(appliedPromo.getVoucherCode());
        } else if (appliedPromo.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_COUPON) {
            checkoutPromoRequest.promoCode(appliedPromo.getCouponCode());
        }

        return checkoutPromoRequest.build();
    }

    @Override
    public CartItemPromoHolderData generateCartPromoHolderData(
            PromoCodeAppliedData appliedPromoData
    ) {
        return null;
    }

    @Override
    public Subscriber<CheckPromoCodeCartShipmentResult> checkPromoSubscription(
            final CartItemPromoHolderData cartItemPromoHolderData) {
        return new Subscriber<CheckPromoCodeCartShipmentResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.showPromoError(null);
            }

            @Override
            public void onNext(CheckPromoCodeCartShipmentResult checkPromoCodeCartShipmentResult) {
                if (!checkPromoCodeCartShipmentResult.isError()) {
                    view.showPromoMessage(checkPromoCodeCartShipmentResult, cartItemPromoHolderData);
                } else {
                    view.showPromoError(checkPromoCodeCartShipmentResult.getErrorMessage());
                }
            }
        };
    }

    @Override
    public void processCheckShipmentFormPrepareCheckout() {
        cartListInteractor.getShipmentForm(new Subscriber<CartShipmentAddressFormData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(CartShipmentAddressFormData cartShipmentAddressFormData) {
                boolean isEnableCheckout = true;
                for (GroupAddress groupAddress : cartShipmentAddressFormData.getGroupAddress()) {
                    if (groupAddress.isError() || groupAddress.isWarning())
                        isEnableCheckout = false;
                    for (GroupShop groupShop : groupAddress.getGroupShop()) {
                        if (groupShop.isError() || groupShop.isWarning())
                            isEnableCheckout = false;
                    }
                }
                if (isEnableCheckout) {
                    view.renderCheckShipmentPrepareCheckoutSuccess();
                } else {
                    view.renderErrorDataHasChangedCheckShipmentPrepareCheckout(
                            cartShipmentAddressFormData
                    );
                }
            }
        }, AuthUtil.generateParamsNetwork(MainApplication.getAppContext(), new TKPDMapParam<String, String>()));
    }

    private int switchValue(boolean isTrue) {
        if (isTrue) return 1;
        else return 0;
    }

    private String formatRupiah(long rupiahAmount) {
        Locale locale = new Locale("in", "ID");
        NumberFormat rupiahCurrencyFormat = NumberFormat.getCurrencyInstance(locale);
        return rupiahCurrencyFormat.format(rupiahAmount);
    }

}
