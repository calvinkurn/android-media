package com.tokopedia.transaction.cart.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.text.TextUtils;

import com.appsflyer.AFInAppEventParameterName;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.PaymentTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.nishikino.model.Checkout;
import com.tokopedia.core.analytics.nishikino.model.Product;
import com.tokopedia.core.analytics.nishikino.model.Purchase;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.BranchSdkUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.addtocart.model.kero.Rates;
import com.tokopedia.transaction.cart.activity.CartActivity;
import com.tokopedia.transaction.cart.interactor.CartDataInteractor;
import com.tokopedia.transaction.cart.interactor.ICartDataInteractor;
import com.tokopedia.transaction.cart.listener.ICartView;
import com.tokopedia.transaction.cart.model.CartItemEditable;
import com.tokopedia.transaction.cart.model.ResponseTransform;
import com.tokopedia.transaction.cart.model.calculateshipment.ProductEditData;
import com.tokopedia.transaction.cart.model.cartdata.CartCourierPrices;
import com.tokopedia.transaction.cart.model.cartdata.CartData;
import com.tokopedia.transaction.cart.model.cartdata.CartItem;
import com.tokopedia.transaction.cart.model.cartdata.CartProduct;
import com.tokopedia.transaction.cart.model.cartdata.CartRatesData;
import com.tokopedia.transaction.cart.model.paramcheckout.CheckoutData;
import com.tokopedia.transaction.cart.model.paramcheckout.CheckoutDropShipperData;
import com.tokopedia.transaction.cart.model.thankstoppaydata.ThanksTopPayData;
import com.tokopedia.transaction.cart.model.voucher.VoucherData;
import com.tokopedia.transaction.cart.services.TopPayIntentService;
import com.tokopedia.transaction.exception.HttpErrorException;
import com.tokopedia.transaction.exception.ResponseErrorException;
import com.tokopedia.transaction.pickuppoint.di.CartPickupPointComponent;
import com.tokopedia.transaction.pickuppoint.di.DaggerCartPickupPointComponent;
import com.tokopedia.transaction.pickuppoint.domain.usecase.EditCartPickupPointsUseCase;
import com.tokopedia.transaction.pickuppoint.domain.usecase.RemoveCartPickupPointsUseCase;

import org.json.JSONArray;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Subscriber;

/**
 * @author anggaprasetiyo on 11/3/16.
 */
public class CartPresenter implements ICartPresenter {


    private static final int MUST_INSURANCE_MODE = 3;
    public static final int OPTIONAL_INSURANCE_MODE = 2;
    public static final String VOUCHER_CODE = "voucher_code";
    public static final String IS_SUGGESTED = "suggested";
    private static final String PARAM_CART_PAGE_LOADED = "cart page loaded";
    private static final String PARAM_CLICK_PAYMENT_OPTION_BUTTON = "click payment option button";
    private final ICartView view;
    private final ICartDataInteractor cartDataInteractor;
    private Gson gson;
    private LocalCacheHandler cartCache;

    @Inject
    EditCartPickupPointsUseCase editCartPickupPointsUseCase;

    @Inject
    RemoveCartPickupPointsUseCase removeCartPickupPointsUseCase;

    public CartPresenter(ICartView iCartView, LocalCacheHandler cartCache) {
        this.view = iCartView;
        this.cartDataInteractor = new CartDataInteractor();
        this.gson = new Gson();
        this.cartCache = cartCache;
        initializeInjector();
    }

    void initializeInjector() {
        AppComponent component = ((CartActivity) view.getContext()).getApplicationComponent();
        CartPickupPointComponent cartPickupPointComponent = DaggerCartPickupPointComponent.builder()
                .appComponent(component).build();
        cartPickupPointComponent.inject(this);
    }

    @Override
    public void processGetCartData() {
        view.renderInitialLoadingCartInfo();
        TKPDMapParam<String, String> params = view.getGeneratedAuthParamNetwork(null);
        cartDataInteractor.getCartData(params, new Subscriber<ResponseTransform<CartData>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                handleThrowableCartInfo(e);
            }

            @Override
            public void onNext(ResponseTransform<CartData> responseTransform) {
                CartData cartData = responseTransform.getData();
                try {
                    processCartAnalytics(cartData);
                    trackStep1CheckoutEE(getCheckoutTrackingData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                processRenderViewCartData(cartData);
                view.renderVisibleMainCartContainer();
                if(!cartData.getCartItemList().isEmpty()){
                    autoApplyCouponIfAvailable(1);

                }
            }
        });
    }

    private void processCartAnalytics(CartData cartData) throws Exception {
        Checkout checkoutAnalytics = new Checkout();
        ArrayList<String> afProductIds = new ArrayList<>();
        ArrayList<Purchase> allPurchase = new ArrayList<>();

        ArrayList<HashMap<String, Object>> afProducts = new ArrayList<>();
        ArrayList<com.tokopedia.core.analytics.model.Product> locaProducts = new ArrayList<>();

        long shippingRate = 0;
        int afQty = 0;
        for (CartItem cartItem : cartData.getCartItemList()) {
            Purchase purchase = new Purchase();
            purchase.setAffiliation(cartItem.getCartShop().getShopName());
            purchase.setRevenue(cartItem.getCartTotalProductPrice());
            purchase.setShipping(cartItem.getCartShippingRate());
            try {
                shippingRate = shippingRate + cartData.getCartShippingRate();
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (CartProduct cartProduct : cartItem.getCartProducts()) {
                afQty = afQty + cartProduct.getProductQuantity();
                Product product = new Product();
                product.setProductID(cartProduct.getProductId());
                product.setPrice(cartProduct.getProductPrice());
                product.setQty(String.valueOf(cartProduct.getProductQuantity()));
                product.setProductName(MethodChecker.fromHtml(cartProduct.getProductName()).toString());

                com.tokopedia.core.analytics.model.Product locaProduct
                        = new com.tokopedia.core.analytics.model.Product();
                locaProduct.setId(cartProduct.getProductId());
                locaProduct.setName(cartProduct.getProductName());
                locaProduct.setPrice(cartProduct.getProductPrice());

                locaProducts.add(locaProduct);


                purchase.addProduct(product.getProduct());
                allPurchase.add(purchase);

                HashMap<String, Object> afItem = new HashMap<>();

                afItem.put(AFInAppEventParameterName.CONTENT_ID, cartProduct.getProductId() + "");
                afItem.put(AFInAppEventParameterName.PRICE, cartProduct.getProductPrice());
                afItem.put(AFInAppEventParameterName.QUANTITY, cartProduct.getProductQuantity());

                afProducts.add(afItem);
                afProductIds.add(cartProduct.getProductId() + "");

                checkoutAnalytics.addProduct(product.getProduct());
            }
        }
        checkoutAnalytics.setCurrency("IDR");

        Map[] afAllItemsPurchased = new Map[afProducts.size()];
        int ctr = 0;
        for (HashMap<String, Object> afItem : afProducts) {
            afAllItemsPurchased[ctr] = afItem;
            ctr++;
        }
        CommonUtils.dumper("GAv4 scrooge " + afQty + " price " + cartData.getGrandTotal()
                + " lp " + cartData.getLpAmount() + " size " + afAllItemsPurchased.length);

        Gson afGSON = new Gson();
        String afpurchased = afGSON.toJson(afAllItemsPurchased, new TypeToken<Map[]>() {
        }.getType());
        String allPurchases = afGSON.toJson(allPurchase, new TypeToken<ArrayList<Purchase>>() {
        }.getType());
        String allLocaProducts = afGSON.toJson(
                locaProducts,
                new TypeToken<ArrayList<com.tokopedia.core.analytics.model.Product>>() {
                }.getType()
        );
        String checkout = afGSON.toJson(checkoutAnalytics, new TypeToken<Checkout>() {
        }.getType());

        LocalCacheHandler cache = view.getLocalCacheHandlerNotificationData();

        cache.putLong(Jordan.CACHE_LC_KEY_SHIPPINGRATE, shippingRate);
        cache.putString(Jordan.CACHE_LC_KEY_ALL_PRODUCTS, allLocaProducts);
        cache.putArrayListString(Jordan.CACHE_AF_KEY_JSONIDS, afProductIds);
        cache.putInt(Jordan.CACHE_AF_KEY_QTY, afQty);
        cache.putString(Jordan.CACHE_AF_KEY_ALL_PRODUCTS, afpurchased);
        cache.putString(Jordan.CACHE_AF_KEY_REVENUE, cartData.getGrandTotal() + "");
        cache.putString(Jordan.CACHE_KEY_DATA_AR_ALLPURCHASE, allPurchases);
        cache.putString(Jordan.CACHE_KEY_DATA_CHECKOUT, checkout);
        cache.applyEditor();

        Map<String, Object> afValue = new HashMap<>();
        afValue.put(AFInAppEventParameterName.PRICE, cartData.getGrandTotal());
        afValue.put(AFInAppEventParameterName.CONTENT_ID, afProductIds);
        afValue.put(AFInAppEventParameterName.QUANTITY, afQty);
        afValue.put(AFInAppEventParameterName.CURRENCY, "IDR");
        afValue.put("product", afAllItemsPurchased);

        sendAppsFlyerATC(afValue);
    }

    private void sendAppsFlyerATC(Map<String, Object> afValue) {
        PaymentTracking.atcAF(afValue);
    }

    @Override
    public void processCancelCart(@NonNull final CartItem canceledCartItem) {
        view.showProgressLoading();
        TKPDMapParam<String, String> maps = new TKPDMapParam<>();
        maps.put("address_id", canceledCartItem.getCartDestination().getAddressId());
        maps.put("shipment_id", canceledCartItem.getCartShipments().getShipmentId());
        maps.put("shipment_package_id", canceledCartItem.getCartShipments().getShipmentPackageId());
        maps.put("shop_id", canceledCartItem.getCartShop().getShopId());
        cartDataInteractor.cancelCart(view.getGeneratedAuthParamNetwork(maps),
                view.getGeneratedAuthParamNetwork(null),
                new Subscriber<ResponseTransform<CartData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handleThrowableGeneral(e);
                    }

                    @Override
                    public void onNext(ResponseTransform<CartData> responseTransform) {
                        view.hideProgressLoading();
                        view.trackingCartCancelEvent();
                        CartData cartData = responseTransform.getData();
                        String messageSuccess = view.getStringFromResource(
                                R.string.label_message_success_cancel_cart
                        );
                        if (!responseTransform.getMessageSuccess().isEmpty())
                            messageSuccess = responseTransform.getMessageSuccess();
                        view.showToastMessage(messageSuccess);
                        try {
                            processCartAnalytics(cartData);
                            trackCanceledCart(canceledCartItem);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        processRenderViewCartData(cartData);
                    }
                });
    }

    @Override
    public void processCancelCartProduct(@NonNull final CartItem canceledCartItem,
                                         @NonNull final CartProduct canceledCartProduct) {
        view.showProgressLoading();
        TKPDMapParam<String, String> maps = new TKPDMapParam<>();
        maps.put("product_cart_id", canceledCartProduct.getProductCartId());
        maps.put("address_id", canceledCartItem.getCartDestination().getAddressId());
        maps.put("shipment_id", canceledCartItem.getCartShipments().getShipmentId());
        maps.put("shipment_package_id", canceledCartItem.getCartShipments().getShipmentPackageId());
        maps.put("shop_id", canceledCartItem.getCartShop().getShopId());
        cartDataInteractor.cancelCart(view.getGeneratedAuthParamNetwork(maps),
                view.getGeneratedAuthParamNetwork(null),
                new Subscriber<ResponseTransform<CartData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handleThrowableGeneral(e);
                    }

                    @Override
                    public void onNext(ResponseTransform<CartData> responseTransform) {
                        view.hideProgressLoading();
                        view.trackingCartCancelEvent();
                        CartData cartData = responseTransform.getData();
                        String messageSuccess = view.getStringFromResource(
                                R.string.label_message_success_cancel_cart
                        );
                        if (!responseTransform.getMessageSuccess().isEmpty())
                            messageSuccess = responseTransform.getMessageSuccess();
                        view.showToastMessage(messageSuccess);
                        try {
                            processCartAnalytics(cartData);
                            trackCanceledProduct(canceledCartItem, canceledCartProduct);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        processRenderViewCartData(cartData);
                    }
                });
    }

    @Override
    public void processSubmitEditCart(@NonNull CartItem cartData,
                                      @NonNull List<ProductEditData> cartProductEditDataList) {
        view.showProgressLoading();
        TKPDMapParam<String, String> maps = new TKPDMapParam<>();
        maps.put("carts", new Gson().toJson(cartProductEditDataList));
        maps.put("cart_shop_id", cartData.getCartShop().getShopId());
        maps.put("cart_addr_id", cartData.getCartDestination().getAddressId());
        maps.put("cart_shipping_id", cartData.getCartShipments().getShipmentId());
        maps.put("cart_sp_id", cartData.getCartShipments().getShipmentPackageId());
        maps.put("lp_flag", "1");
        maps.put("cart_string", cartData.getCartString());
        cartDataInteractor.updateCart(view.getGeneratedAuthParamNetwork(maps),
                view.getGeneratedAuthParamNetwork(null),
                new Subscriber<ResponseTransform<CartData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handleThrowableGeneral(e);
                    }

                    @Override
                    public void onNext(ResponseTransform<CartData> responseTransform) {
                        view.hideProgressLoading();
                        CartData cartData = responseTransform.getData();
                        String messageSuccess = view.getStringFromResource(
                                R.string.label_message_success_update_item_cart_data
                        );
                        if (!responseTransform.getMessageSuccess().isEmpty()) {
                            messageSuccess = responseTransform.getMessageSuccess();
                        }
                        view.showToastMessage(messageSuccess);
                        try {
                            processCartAnalytics(cartData);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        processRenderViewCartData(cartData);
                    }
                });
    }

    @Override
    public void processUpdateInsurance(@NonNull final CartItemEditable cartItemEditable,
                                       final boolean useInsurance) {
        view.showProgressLoading();
        CartItem cartData = cartItemEditable.getCartItem();
        TKPDMapParam<String, String> maps = new TKPDMapParam<>();
        maps.put("address_id", cartData.getCartDestination().getAddressId());
        maps.put("product_insurance", useInsurance ? "1" : "0");
        maps.put("shipment_id", cartData.getCartShipments().getShipmentId());
        maps.put("shipment_package_id", cartData.getCartShipments().getShipmentPackageId());
        maps.put("shop_id", cartData.getCartShop().getShopId());
        cartDataInteractor.updateInsuranceCart(view.getGeneratedAuthParamNetwork(maps),
                view.getGeneratedAuthParamNetwork(null),
                new Subscriber<ResponseTransform<CartData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handleThrowableGeneral(e);
                        switchInsurancePrice(cartItemEditable, !useInsurance);
                    }

                    @Override
                    public void onNext(ResponseTransform<CartData> responseTransform) {
                        view.hideProgressLoading();
                        CartData cartData = responseTransform.getData();
                        String messageSuccess = view.getStringFromResource(
                                R.string.label_message_success_update_cart_insurance
                        );
                        if (!responseTransform.getMessageSuccess().isEmpty()) {
                            messageSuccess = responseTransform.getMessageSuccess();
                        }
                        view.showToastMessage(messageSuccess);
                        try {
                            processCartAnalytics(cartData);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        switchInsurancePrice(cartItemEditable, useInsurance);
                        processRenderViewCartData(cartData);
                    }
                });
    }

    private void switchInsurancePrice(@NonNull CartItemEditable cartItemEditable, boolean useInsurance) {
        cartItemEditable.setUseInsurance(useInsurance);
        cartItemEditable.getCartCourierPrices().setCartSubtotal(useInsurance);
        view.refreshCartList();
    }

    @Override
    public void processCheckVoucherCode(final String voucherCode, final int instantCheckVoucher) {
        view.showProgressLoading();
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(VOUCHER_CODE, voucherCode);
        params.put(IS_SUGGESTED, String.valueOf(instantCheckVoucher));
        cartDataInteractor.checkVoucherCode(view.getGeneratedAuthParamNetwork(params),
                new Subscriber<ResponseTransform<VoucherData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e.getCause() instanceof ResponseErrorException) {
                            view.renderErrorCheckVoucher(e.getCause().getMessage());
                            view.renderErrorFromInstantVoucher(instantCheckVoucher);
                            view.hideProgressLoading();
                            removeBranchPromo();
                        } else {
                            handleThrowableVoucherCode(e);
                        }
                    }

                    @Override
                    public void onNext(ResponseTransform<VoucherData> responseTransform) {
                        VoucherData voucherData = responseTransform.getData();
                        String descVoucher = view.getStringFromResource(
                                R.string.label_message_default_voucher_desc_result
                        ) + responseTransform.getData().getVoucher().getVoucherAmountIdr();
                        if (voucherData.getVoucher().getVoucherAmount().equals("0"))
                            descVoucher = voucherData.getVoucher().getVoucherPromoDesc();
                        view.renderSuccessCheckVoucher(
                                voucherCode,
                                responseTransform.getData().getVoucher().getVoucherAmountIdr(),
                                descVoucher,
                                instantCheckVoucher
                        );
                        view.hideProgressLoading();
                    }
                });
    }


    @Override
    public void processGetTickerGTM() {
        if (TrackingUtils.getGtmString(AppEventTracking.GTM.TICKER_CART).equalsIgnoreCase("true")) {
            String message = TrackingUtils.getGtmString(AppEventTracking.GTM.TICKER_CART_TEXT);
            view.renderVisibleTickerGTM(message);
        } else {
            view.renderInvisibleTickerGTM();
        }
    }

    @Override
    public void processValidationCheckoutData() {
        List<CartItemEditable> cartItemEditables = view.getItemCartListCheckoutData();
        boolean canBeCheckout = true;

        CheckoutData.Builder checkoutDataBuilder = view.getCheckoutDataBuilder();

        String voucherCode = view.getVoucherCodeCheckoutData();
        boolean isUseVoucher = view.isCheckoutDataUseVoucher();

        if (isUseVoucher) {
            if (voucherCode.isEmpty()) {
                view.renderErrorCheckVoucher(
                        view.getStringFromResource(R.string.label_error_form_voucher_code_empty)
                );
                return;
            } else {
                view.renderDisableErrorCheckVoucher();
                checkoutDataBuilder.voucherCode(voucherCode);
            }
        } else {
            checkoutDataBuilder.voucherCode("");
        }

        String depositCheckout = view.getDepositCheckoutData();
        checkoutDataBuilder.usedDeposit(depositCheckout.replaceAll("\\D+", ""));
        checkoutDataBuilder.donationValue(view.getDonationValue());

        for (int i = 0, cartItemEditablesSize = cartItemEditables.size();
             i < cartItemEditablesSize; i++) {
            CartItemEditable cartItemEditable = cartItemEditables.get(i);
            view.renderErrorCartItem(cartItemEditable);
            if (cartItemEditable.getErrorType() != CartItemEditable.ERROR_NON) {
                canBeCheckout = false;
            }
        }
        if (!canBeCheckout) {
            view.showToastMessage(view.getStringFromResource(
                    R.string.label_message_error_cannot_checkout
            ));
        } else {
            processCheckoutCart(checkoutDataBuilder, cartItemEditables);
        }
    }

    @Override
    public void unSubscribeObservable() {
        cartDataInteractor.unSubscribeObservable();
    }

    @Override
    public void processValidationPayment(String paymentId) {
        Bundle bundle = new Bundle();
        bundle.putInt(TopPayIntentService.EXTRA_ACTION,
                TopPayIntentService.SERVICE_ACTION_GET_THANKS_TOP_PAY);
        bundle.putString(TopPayIntentService.EXTRA_PAYMENT_ID, paymentId);
        view.executeIntentService(bundle, TopPayIntentService.class);
    }

    @Override
    public void trackStep1CheckoutEE(Checkout checkoutData) {
        checkoutData.setStep("1");
        checkoutData.setCheckoutOption(PARAM_CART_PAGE_LOADED);
        PaymentTracking.eventCartCheckoutStep1(checkoutData);
    }

    @Override
    public void trackStep2CheckoutEE(Checkout checkoutData) {
        checkoutData.setStep("2");
        checkoutData.setCheckoutOption(PARAM_CLICK_PAYMENT_OPTION_BUTTON);
        PaymentTracking.eventCartCheckoutStep2(checkoutData);
    }

    @Override
    public void processPaymentAnalytics(LocalCacheHandler cacheHandler, ThanksTopPayData thanksTopPayData) {
        Gson afGSON = new Gson();
        ArrayList<Purchase> purchases = afGSON.fromJson(
                cacheHandler.getString(Jordan.CACHE_KEY_DATA_AR_ALLPURCHASE),
                new TypeToken<ArrayList<Purchase>>() {
                }.getType()
        );

        /*
          GTM Block
         */
        if (purchases != null) {
            if (!purchases.isEmpty()) {
                for (Purchase purchase : purchases) {
                    purchase.setTransactionID(thanksTopPayData.getParameter().getPaymentId());
                    PaymentTracking.eventTransactionGTM(purchase);
                }
            }
        }

    }

    @Override
    public void clearNotificationCart() {
        LocalCacheHandler cache = view.getLocalCacheHandlerNotificationData();
        cache.putInt(TkpdCache.Key.IS_HAS_CART, 0);
        cache.applyEditor();
    }

    private void trackCanceledCart(CartItem canceledCartItem) {
        if (canceledCartItem != null
                && canceledCartItem.getCartProducts() != null
                && !canceledCartItem.getCartProducts().isEmpty()) {
            for (CartProduct cartProduct : canceledCartItem.getCartProducts()) {
                trackCanceledProduct(canceledCartItem, cartProduct);
            }
        }
    }

    private void trackCanceledProduct(CartItem cartData, CartProduct cartProduct) {
        if (cartData != null
                && cartData.getCartShop() != null
                && cartProduct != null) {
            com.tokopedia.core.analytics.model.Product product = new com.tokopedia.core.analytics.model.Product();
            product.setName(cartProduct.getProductName());
            product.setId(cartProduct.getProductId());
            product.setUrl(cartProduct.getProductUrl());
            product.setImageUrl(cartProduct.getProductPic());
            product.setPrice(cartProduct.getProductPrice());
            product.setShopId(cartData.getCartShop().getShopId());

            TrackingUtils.sendMoEngageRemoveProductFromCart(product);
        }
    }

    @NonNull
    private Map<String, String> generateDropShipperParam(List<String> dropShipperNameList,
                                                         List<String> dropShipperPhoneList,
                                                         List<String> dropShipperStringList) {
        Map<String, String> params = new HashMap<>();
        for (int i = 0, dropShipperNameListSize = dropShipperNameList.size();
             i < dropShipperNameListSize; i++) {
            params.put(
                    "dropship_name-" + dropShipperStringList.get(i), dropShipperNameList.get(i)
            );
            params.put(
                    "dropship_telp-" + dropShipperStringList.get(i), dropShipperPhoneList.get(i)
            );
        }
        return params;
    }

    private void processCheckoutCart(@NonNull CheckoutData.Builder checkoutDataBuilder,
                                     @NonNull List<CartItemEditable> cartItemEditables) {
        List<String> dropShipperNameList = new ArrayList<>();
        List<String> dropShipperPhoneList = new ArrayList<>();

        List<String> dropShipperStringList = new ArrayList<>();
        List<String> partialDeliverStringList = new ArrayList<>();
        List<String> rateKeyList = new ArrayList<>();
        List<String> rateDataList = new ArrayList<>();
        List<CartItem> cartItemList = new ArrayList<>();

        for (CartItemEditable data : cartItemEditables) {
            if (data.isDropShipper()) {
                dropShipperNameList.add(data.getDropShipperName());
                dropShipperPhoneList.add(data.getDropShipperPhone());
                dropShipperStringList.add(data.getCartStringForDropShipperOption());
            }
            if (data.isPartialDeliver()) {
                partialDeliverStringList.add(data.getCartStringForDeliverOption());
            }
            if (data.getCartCourierPrices() != null && !hasError(data.getCartItem())) {
                rateKeyList.add(data.getCartCourierPrices().getKey());
                rateDataList.add(data.getCartCourierPrices().getKeroValue());
            }
            cartItemList.add(data.getCartItem());
        }

        StringBuilder dropShipperParamStringBuilder = new StringBuilder();
        for (String stringCart : dropShipperStringList) {
            dropShipperParamStringBuilder.append(stringCart).append("*~*");
        }
        String dropShipperParamString = dropShipperParamStringBuilder.toString();
        if (dropShipperParamString.endsWith("*~*")) {
            dropShipperParamString = dropShipperParamString.substring(0,
                    dropShipperParamString.lastIndexOf("*~*"));
        }

        StringBuilder partialDeliverParamBuilder = new StringBuilder();
        for (String stringCart : partialDeliverStringList) {
            partialDeliverParamBuilder.append(stringCart).append("*~*");
        }
        String partialDeliverParamString = partialDeliverParamBuilder.toString();
        if (partialDeliverParamString.endsWith("*~*")) {
            partialDeliverParamString = partialDeliverParamString.substring(0,
                    partialDeliverParamString.lastIndexOf("*~*"));
        }

        List<CheckoutDropShipperData> checkoutDropShipperDataList = new ArrayList<>();

        Map<String, String> dropShipperDataParam = generateDropShipperParam(dropShipperNameList,
                dropShipperPhoneList, dropShipperStringList);


        for (Map.Entry<String, String> entry : dropShipperDataParam.entrySet()) {
            checkoutDropShipperDataList.add(new CheckoutDropShipperData.Builder()
                    .key(entry.getKey())
                    .value(entry.getValue())
                    .build());
        }

        CheckoutData checkoutData = checkoutDataBuilder
                .dropShipperDataList(checkoutDropShipperDataList)
                .dropShipString(dropShipperParamString)
                .partialString(partialDeliverParamString)
                .lpFlag("1")
                .step("1")
                .keroKeyParams(rateKeyList)
                .keroValueParams(rateDataList)
                .build();
        if (checkoutData.getGateway() == null) {
            view.renderForceShowPaymentGatewaySelection();
            return;
        }

        if (checkoutData.getKeroKeyParams().size() < 1) {
            view.showToastMessage(view.getStringFromResource(
                    R.string.label_message_error_cannot_checkout));
        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelable(TopPayIntentService.EXTRA_CHECKOUT_DATA, checkoutData);
            bundle.putInt(TopPayIntentService.EXTRA_ACTION,
                    TopPayIntentService.SERVICE_ACTION_GET_PARAMETER_DATA);
            view.executeIntentService(bundle, TopPayIntentService.class);
            trackStep2CheckoutEE(getCheckoutTrackingData());
        }
    }

    private void handleThrowableGeneral(Throwable e) {
        e.printStackTrace();
        if (e instanceof SocketTimeoutException) {
            view.showToastMessage(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
        } else if (e instanceof UnknownHostException) {
            view.showToastMessage(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
        } else if (e.getCause() instanceof ResponseErrorException) {
            view.showToastMessage(e.getCause().getMessage());
        } else if (e.getCause() instanceof HttpErrorException) {
            view.showToastMessage(e.getCause().getMessage());
        } else {
            view.showToastMessage(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
        view.hideProgressLoading();
    }

    private void handleThrowableVoucherCode(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            view.showToastMessage(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
        } else if (e instanceof UnknownHostException) {
            view.showToastMessage(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
        } else if (e.getCause() instanceof HttpErrorException) {
            view.showToastMessage(e.getCause().getMessage());
        } else {
            view.showToastMessage(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
        view.hideProgressLoading();
    }

    private void handleThrowableCartInfo(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            view.renderErrorTimeoutInitialCartInfo(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
        } else if (e instanceof UnknownHostException) {
            view.renderErrorNoConnectionInitialCartInfo(
                    ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION
            );
        } else if (e.getCause() instanceof ResponseErrorException) {
            view.renderErrorResponseInitialCartInfo(e.getCause().getMessage());
        } else if (e.getCause() instanceof HttpErrorException) {
            view.renderErrorDefaultInitialCartInfo(e.getCause().getMessage());
        } else {
            view.renderErrorDefaultInitialCartInfo(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
        view.hideProgressLoading();
    }

    private void processRenderViewCartData(CartData data) {
        view.renderCheckboxDonasi(data.getDonation());
        if (data.getCartItemList().isEmpty()) {
            view.renderErrorEmptyCart();
            return;
        }
        if (data.getCashback() != 0)
            view.renderVisiblePotentialCashBack(data.getCashbackIdr());
        else view.renderInvisiblePotentialCashBack();
        view.renderDepositInfo(data.getDepositIdr());
        view.renderTotalPaymentWithLoyalty(data.getGrandTotalIdr());
        view.renderPaymentGatewayOption(data.getGatewayList());
        if (data.getLpAmount() != 0)
            view.renderVisibleLoyaltyBalance(data.getLpAmountIdr(),
                    String.valueOf(data.getLpAmount()));
        else view.renderInvisibleLoyaltyBalance();
        view.renderTotalPaymentWithoutLoyalty(data.getGrandTotalWithoutLPIDR());
        view.renderCartListData(data.getTokenKero(), data.getUt(), data.getCartItemList());
        view.renderCheckoutCartDepositAmount(data.getDeposit() + "");
        view.setCheckoutCartToken(data.getToken());
        if ((data.getCheckoutNotifError() != null && !data.getCheckoutNotifError().equals("0"))) {
            String countNotifError = data.getCheckoutNotifError();
            String messageError1 = view.getStringFromResource(
                    R.string.label_error_message_1_count_notif
            );
            String messageError2 = view.getStringFromResource(
                    R.string.label_error_message_2_count_notif
            );
            view.renderVisibleErrorPaymentCart(
                    messageError1.replace("XX_XX", countNotifError), messageError2
            );
        } else {
            view.renderInvisibleErrorPaymentCart();
        }
        view.renderButtonCheckVoucherListener();
        view.renderInstantPromo(data.getCartPromo());
        view.renderPromoView(data.getIsCouponActive() == 1);
    }

    @Override
    public void processCartRates(String token, String ut, final List<CartItem> cartItemList) {
        cartDataInteractor.calculateKeroRates(token, ut, cartItemList, keroRatesListener());
    }

    private ICartDataInteractor.KeroRatesListener keroRatesListener() {
        return new ICartDataInteractor.KeroRatesListener() {
            @Override
            public void onSuccess(CartRatesData cartRatesData) {
                if (cartRatesData.getRatesResponse() == null) {
                    view.setCartError(cartRatesData.getRatesIndex());
                } else {
                    Rates ratesData = new Gson().fromJson(cartRatesData.getRatesResponse(),
                            Rates.class);
                    CartCourierPrices cartCourierPrices = new CartCourierPrices();
                    cartCourierPrices.setKey(cartRatesData.getKeroRatesKey());
                    cartCourierPrices.setCartProductPrice(cartRatesData.getCartTotalProductPrice());
                    cartCourierPrices.setCartIndex(cartRatesData.getRatesIndex());
                    cartCourierPrices.setAdditionFee(cartRatesData.getCartAdditionalLogisticFee());
                    cartCourierPrices.setKeroWeight(String.valueOf(ratesData.getData()
                            .getAttributes()
                            .get(0)
                            .getWeight()));
                    List<com.tokopedia.transaction.addtocart.model.kero.Product> shipmentServices =
                            ratesData.getData().getAttributes().get(0).getProducts();

                    setSubTotalPrice(shipmentServices,
                            cartCourierPrices,
                            cartRatesData);
                    view.setCartSubTotal(cartCourierPrices);
                }
            }

            @Override
            public void onAllDataCompleted() {
                view.showRatesCompletion();
            }

            @Override
            public void onRatesFailed(String errorMessage) {
                view.setCartNoGrandTotal();
            }

            @Override
            public void onConnectionFailed() {

            }
        };
    }

    private void setSubTotalPrice(List<com.tokopedia.transaction.addtocart.model.kero
            .Product> keroShipmentServices,
                                  CartCourierPrices courierPrices,
                                  CartRatesData cartRatesData) {

        int shipmentServiceId = cartRatesData.getCourierServiceId();

        for (int i = 0; i < keroShipmentServices.size(); i++) {
            Integer currentKeroShipmentServiceId = Integer
                    .parseInt(keroShipmentServices.get(i).getShipperProductId());

            if (currentKeroShipmentServiceId == shipmentServiceId) {
                courierPrices.setShipmentPrice(keroShipmentServices.get(i).getPrice());
                courierPrices.setInsuranceMode(keroShipmentServices.get(i).getInsuranceMode());
                if (courierPrices.getInsuranceMode() == MUST_INSURANCE_MODE) {
                    courierPrices.setInsurancePrice(keroShipmentServices.get(i)
                            .getInsurancePrice());
                    courierPrices.setCartSubtotal(true);
                } else if (courierPrices.getInsuranceMode() == OPTIONAL_INSURANCE_MODE) {
                    courierPrices.setInsurancePrice(keroShipmentServices.get(i)
                            .getInsurancePrice());
                    courierPrices.setCartSubtotal(cartRatesData.isInsuranced());
                } else {
                    courierPrices.setCartSubtotal(false);
                }
                courierPrices.setKeroValue(keroShipmentServices.get(i));
                courierPrices.setUseInsurance(cartRatesData.isInsuranced() ? 1 : 0);
                courierPrices.setInsuranceUsedInfo(keroShipmentServices.get(i).getInsuranceUsedInfo());
                courierPrices.setInsuranceUsedType(keroShipmentServices.get(i).getInsuranceUsedType());
            }
        }

    }

    private boolean hasError(CartItem cartItem) {
        return (cartItem.getCartErrorMessage2() != null
                && !cartItem.getCartErrorMessage2().equals("0"))
                || (cartItem.getCartErrorMessage1() != null
                && !cartItem.getCartErrorMessage1().equals("0"));
    }


    public void autoApplyCouponIfAvailable(Integer selectedProduct) {
        String savedCoupon = BranchSdkUtils.getAutoApplyCouponIfAvailable(view.getActivity());
        if (!TextUtils.isEmpty(savedCoupon)) {
            processCheckVoucherCode(savedCoupon, selectedProduct);
            view.setListnerCancelPromoLayoutOnAutoApplyCode();
        }
    }

    private void removeBranchPromo() {
        BranchSdkUtils.removeCouponCode(view.getActivity());
    }
    private Checkout getCheckoutTrackingData() {
        return gson.fromJson(
                cartCache.getString(Jordan.CACHE_KEY_DATA_CHECKOUT),
                new TypeToken<Checkout>() {}.getType());
    }

    @Override
    public void processUpdatePickupPoint(String cartId, String oldStoreId, String newStoreId) {
        editCartPickupPointsUseCase.execute(
                EditCartPickupPointsUseCase.generateParams(view.getContext(), cartId, oldStoreId, newStoreId),
                new Subscriber<Response<TkpdResponse>>() {
                    @Override
                    public void onCompleted() {
                        Log.e("removeCartPickupPoints", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("removeCartPickupPoints", "onError");
                    }

                    @Override
                    public void onNext(Response<TkpdResponse> tkpdResponseResponse) {
                        Log.e("removeCartPickupPoints", "onNext");
                    }
                });
    }

    @Override
    public void processRemovePickupPoint(String cartId, String oldStoreId) {
        removeCartPickupPointsUseCase.execute(
                RemoveCartPickupPointsUseCase.generateParams(view.getContext(), cartId, oldStoreId),
                new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                Log.e("removeCartPickupPoints", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("removeCartPickupPoints", "onError");
            }

            @Override
            public void onNext(Response<TkpdResponse> tkpdResponseResponse) {
                Log.e("removeCartPickupPoints", "onNext");
            }
        });
    }
}
