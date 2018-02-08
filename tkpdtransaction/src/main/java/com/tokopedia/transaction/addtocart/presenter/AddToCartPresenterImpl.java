package com.tokopedia.transaction.addtocart.presenter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Window;

import com.appsflyer.AFInAppEventParameterName;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.PaymentTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.nishikino.model.GTMCart;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.router.transactionmodule.TransactionCartRouter;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.transaction.addtocart.activity.AddToCartActivity;
import com.tokopedia.transaction.addtocart.interactor.AddToCartNetInteractor;
import com.tokopedia.transaction.addtocart.interactor.AddToCartNetInteractorImpl;
import com.tokopedia.transaction.addtocart.interactor.KeroNetInteractor;
import com.tokopedia.transaction.addtocart.interactor.KeroNetInteractorImpl;
import com.tokopedia.transaction.addtocart.listener.AddToCartViewListener;
import com.tokopedia.transaction.addtocart.model.OrderData;
import com.tokopedia.transaction.addtocart.model.kero.Attribute;
import com.tokopedia.transaction.addtocart.model.kero.Data;
import com.tokopedia.transaction.addtocart.model.kero.Product;
import com.tokopedia.transaction.addtocart.model.responseatcform.AtcFormData;
import com.tokopedia.transaction.addtocart.model.responseatcform.Destination;
import com.tokopedia.transaction.addtocart.model.responseatcform.Shipment;
import com.tokopedia.transaction.addtocart.model.responseatcform.ShipmentPackage;
import com.tokopedia.transaction.addtocart.receiver.ATCResultReceiver;
import com.tokopedia.transaction.addtocart.services.ATCIntentService;
import com.tokopedia.transaction.addtocart.utils.KeroppiParam;
import com.tokopedia.transaction.addtocart.utils.NetParamUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Angga.Prasetiyo on 11/03/2016.
 */
public class AddToCartPresenterImpl implements AddToCartPresenter {
    private final AddToCartNetInteractor addToCartNetInteractor;
    private final AddToCartViewListener viewListener;
    private final KeroNetInteractorImpl keroNetInteractor;
    private static final String GOJEK_ID = "10";
    private int minimumNoInsuranceCount = 0;

    public AddToCartPresenterImpl(AddToCartActivity addToCartActivity) {
        this.addToCartNetInteractor = new AddToCartNetInteractorImpl();
        this.viewListener = addToCartActivity;
        keroNetInteractor = new KeroNetInteractorImpl();
    }

    @Override
    public void getCartFormData(@NonNull final Context context,
                                @NonNull final ProductCartPass data) {
        viewListener.showInitLoading();
        Map<String, String> param = new HashMap<>();
        param.put("product_id", data.getProductId());
        viewListener.disableAllForm();
        addToCartNetInteractor.getAddToCartForm(context, param,
                new AddToCartNetInteractor.OnGetCartFormListener() {
                    @Override
                    public void onSuccess(AtcFormData data) {
                        viewListener.hideNetworkError();
                        viewListener.initialOrderData(data);
                        viewListener.renderFormProductInfo(data.getForm().getProductDetail());
                        viewListener.renderFormAddress(data.getForm().getDestination());
                        viewListener.hideInitLoading();
                        setInitialWeight(data);
                        if (isAllowKeroAccess(data) && isAllowedCourier(data)) {
                            calculateKeroRates(context, data);
                        } else {
                            viewListener.hideProgressLoading();
                        }
                    }

                    @Override
                    public void onFailure() {
                        viewListener.onCartFailedLoading();
                    }
                });
    }

    @Override
    public void getCartKeroToken(@NonNull final Context context, @NonNull ProductCartPass data,
                                 @NonNull final Destination destination) {
        viewListener.showInitLoading();
        Map<String, String> param = new HashMap<>();
        param.put("product_id", data.getProductId());
        viewListener.disableAllForm();
        addToCartNetInteractor.getAddToCartForm(context, param,
                new AddToCartNetInteractor.OnGetCartFormListener() {
                    @Override
                    public void onSuccess(AtcFormData data) {
                        data.getForm().setDestination(destination);
                        viewListener.hideNetworkError();
                        viewListener.initialOrderData(data);
                        viewListener.renderFormProductInfo(data.getForm().getProductDetail());
                        viewListener.renderFormAddress(data.getForm().getDestination());
                        viewListener.hideInitLoading();
                        if (isAllowKeroAccess(data) && isAllowedCourier(data)) {
                            calculateKeroRates(context, data);
                        } else {
                            viewListener.hideProgressLoading();
                        }
                    }

                    @Override
                    public void onFailure() {
                        viewListener.onCartFailedLoading();
                    }
                });
    }

    @Override
    public void calculateKeroRates(@NonNull Context context,
                                   @NonNull final AtcFormData atcFormData) {
        viewListener.disableBuyButton();
        keroNetInteractor.calculateShipping(context, KeroppiParam.paramsKero(atcFormData.getShop(),
                atcFormData.getForm().getDestination(), atcFormData.getForm().getProductDetail()),
                new KeroNetInteractor.CalculationListener() {
                    @Override
                    public void onSuccess(Data rates) {
                        viewListener.renderFormShipmentRates(filterAvailableKeroShipment(
                                rates.getAttributes(), atcFormData.getForm().getShipment())
                        );
                        viewListener.enableBuyButton();
                    }

                    @Override
                    public void onFailed(String error) {
                        viewListener.showErrorMessage(error);
                        viewListener.enableBuyButton();
                    }

                    @Override
                    public void onTimeout(String timeoutError) {
                        viewListener.showErrorMessage(timeoutError);
                        viewListener.enableBuyButton();
                    }

                    @Override
                    public void onNoConnection() {
                        viewListener.onCartFailedLoading();
                        viewListener.enableBuyButton();
                    }
                });

    }

    private List<Attribute> filterAvailableKeroShipment(List<Attribute> datas,
                                                        List<Shipment> oldShipments) {
        List<Attribute> shipmentsResult = new ArrayList<>();
        for (Attribute shipment : datas) {
            for (Shipment oldShipment : oldShipments) {
                if (shipment.getShipperId().equals(oldShipment.getShipmentId())) {
                    List<Product> packages
                            = filterAvailableKeroPackageShipment(
                            shipment.getProducts(), oldShipment.getShipmentPackage()
                    );
                    shipment.getProducts().clear();
                    shipment.setProducts(packages);
                    shipmentsResult.add(shipment);
                }
            }
        }
        return shipmentsResult;
    }

    private List<Product> filterAvailableKeroPackageShipment(List<Product> datas,
                                                             List<ShipmentPackage> dataOld) {
        List<Product> packageResults = new ArrayList<>();
        for (Product packageShipment : datas) {
            for (ShipmentPackage shipmentPackage : dataOld) {
                if (packageShipment.getShipperProductId().equals(shipmentPackage.getSpId()))
                    packageResults.add(packageShipment);
            }
        }
        return packageResults;
    }

    @Override
    public void calculateProduct(@NonNull final Context context,
                                 @NonNull final OrderData orderData,
                                 final boolean mustReCalculateAddressShipping) {
        viewListener.disableBuyButton();
        addToCartNetInteractor.calculateCartPrice(context, AuthUtil.generateParamsNetwork(context,
                NetParamUtil.paramCalculateCart("calculate_product", orderData)),
                new AddToCartNetInteractor.OnCalculateProduct() {
                    @Override
                    public void onSuccess(String price) {
                        viewListener.renderProductPrice(price);
                        viewListener.enableBuyButton();
                        if (mustReCalculateAddressShipping) {
                            calculateKeroAddressShipping(context, orderData);
                        }
                    }

                    @Override
                    public void onFailure() {
                        viewListener.showCalculateProductErrorMessage(
                                context.getString(R.string.msg_network_error)
                        );
                        viewListener.enableBuyButton();
                    }

                    @Override
                    public void onShowErrorMessage(String errorMessage) {
                        viewListener.showCalculateProductErrorMessage(errorMessage);
                    }
                });
    }

    @Override
    public void calculateKeroAddressShipping(@NonNull Context context,
                                             @NonNull final OrderData orderData) {
        keroNetInteractor.calculateKeroCartAddressShipping(context,
                AuthUtil.generateParamsNetwork(
                        context, KeroppiParam.paramsKeroOrderData(orderData)
                ),
                new KeroNetInteractor.OnCalculateKeroAddressShipping() {
                    @Override
                    public void onSuccess(List<Attribute> datas) {
                        viewListener.renderFormShipmentRates(filterAvailableKeroShipment(
                                datas, orderData.getShipments())
                        );
                    }

                    @Override
                    public void onFailure() {
                        viewListener.showCalculateShippingErrorMessage();
                    }
                }
        );
    }

    @Override
    public Destination generateAddressData(Intent data) {
        return Destination.convertFromBundle(
                data.getExtras().getParcelable(ManageAddressConstant.EXTRA_ADDRESS)
        );
    }

    @Override
    public void calculateAllPrices(@NonNull Context context, @NonNull final OrderData orderData) {
        if ((orderData.getShipment() != null
                && orderData.getShipmentPackage() != null
                && orderData.getAddress() != null)
                || orderData.getShipment() != null
                && orderData.getShipment().equals(TkpdState.SHIPPING_ID.GOJEK)) {
            viewListener.disableBuyButton();
            CommonUtils.dumper("rates/v1 kerorates called calculateAllShipping");
            keroNetInteractor.calculateKeroCartAddressShipping(context,
                    AuthUtil.generateParamsNetwork(
                            context, KeroppiParam.paramsKeroOrderData(orderData)
                    ),
                    new KeroNetInteractor.OnCalculateKeroAddressShipping() {
                        @Override
                        public void onSuccess(List<Attribute> datas) {
                            viewListener.renderFormShipmentRates(filterAvailableKeroShipment(
                                    datas, orderData.getShipments())
                            );
                            viewListener.enableBuyButton();
                        }

                        @Override
                        public void onFailure() {
                            viewListener.enableBuyButton();
                            viewListener.showCalculateShippingErrorMessage();
                        }
                    });
        }
    }

    @Override
    public void processChooseGeoLocation(@NonNull Context context, @NonNull OrderData orderData) {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();

        int resultCode = availability.isGooglePlayServicesAvailable(context);

        if (ConnectionResult.SUCCESS == resultCode) {
            String lat = orderData.getAddress().getLatitude();
            String lon = orderData.getAddress().getLongitude();

            LocationPass locationPass = null;
            if (!(lat == null || lat.isEmpty()) && !(lon == null || lon.isEmpty())) {
                locationPass = new LocationPass();
                locationPass.setLatitude(lat);
                locationPass.setLongitude(lon);
                locationPass.setGeneratedAddress(orderData.getAddress().getGeoLocation(context));
            }
            Intent intent = GeolocationActivity.createInstance(context, locationPass);
            viewListener.navigateToActivityRequest(intent,
                    AddToCartActivity.REQUEST_CHOOSE_LOCATION);
        } else {
            CommonUtils.dumper("Google play services unavailable");
            Dialog dialog = availability.getErrorDialog((AddToCartActivity) context, resultCode, 0);
            viewListener.showDialog(dialog);
        }
    }

    @Override
    public void updateAddressShipping(@NonNull final Context context,
                                      @NonNull final OrderData orderData,
                                      @NonNull final LocationPass locationPass) {
        Map<String, String> maps = new HashMap<>();
        maps.put("act", "edit_address");
        maps.put("is_from_cart", "1");
        maps.put("address_id", orderData.getAddress().getAddressId());
        maps.put("latitude", locationPass.getLatitude());
        maps.put("longitude", locationPass.getLongitude());
        final String oldLatitude = orderData.getAddress().getLatitude();
        final String oldLongitude = orderData.getAddress().getLongitude();
        orderData.getAddress().setLatitude(locationPass.getLatitude());
        orderData.getAddress().setLongitude(locationPass.getLongitude());
        addToCartNetInteractor.updateAddress(context, maps,
                new AddToCartNetInteractor.OnUpdateAddressListener() {
                    @Override
                    public void onSuccess() {
                        viewListener.enableQuantityTextWatcher();
                        viewListener.renderFormAddress(orderData.getAddress());
                        viewListener.changeQuantity(String.valueOf(orderData.getQuantity()));
                        calculateAllPrices(context, orderData);
                    }

                    @Override
                    public void onFailure(String messageError) {
                        viewListener.enableQuantityTextWatcher();
                        viewListener.renderFormAddress(orderData.getAddress());
                        viewListener.showUpdateAddressShippingError(messageError);
                        orderData.getAddress().setLatitude(oldLatitude);
                        orderData.getAddress().setLongitude(oldLongitude);
                        viewListener.changeQuantity(String.valueOf(orderData.getQuantity()));
                    }

                    @Override
                    public void onError() {
                        viewListener.enableQuantityTextWatcher();
                        viewListener.changeQuantity(String.valueOf(orderData.getQuantity()));
                        viewListener.showErrorMessage(
                                context.getString(R.string.default_request_error_unknown)
                        );
                    }
                });
    }

    @Override
    public void addToCart(@NonNull final Context context, @NonNull OrderData orderData) {
        addToCartNetInteractor.addToCart(context, null, NetParamUtil.paramAddToCart(orderData),
                new AddToCartNetInteractor.OnAddToCart() {

                    @Override
                    public void onSuccess(String message) {
                        createDialogAtcSuccess(context, message);
                    }

                    @Override
                    public void onFailure(String message) {
                        viewListener.showErrorMessage(message);
                    }

                    @Override
                    public void onTimeout() {

                    }

                    @Override
                    public void onNoConnection() {

                    }
                });
    }

    @Override
    public boolean isValidOrder(@NonNull Context context, @NonNull OrderData orderData) {
        if (orderData.getQuantity() < orderData.getMinOrder()) {
            viewListener.showErrorMessage(context.getString(R.string.error_min_order)
                    + " " + orderData.getMinOrder());
            return false;
        }

        if (orderData.getAddress() == null || orderData.getAddress().getAddressId().equals("0") ||
                orderData.getAddress().getAddressId().equals("")) {
            viewListener.showErrorMessage(context.getString(R.string.error_no_address));
            return false;
        }
        if (orderData.getShipment() != null && orderData.getShipmentPackage() != null) {
            if (orderData.getShipment().equals(TkpdState.SHIPPING_ID.GOJEK)
                    && orderData.getShipmentPackage().equals("0")
                    && viewListener.getGoogleMapLocation().isEmpty()) {
                viewListener.showErrorMessage(context.getString(R.string.error_google_map_not_chosen));
                return false;
            }
            if (orderData.getShipment().equals("0") || orderData.getShipmentPackage().equals("0")) {
                viewListener.showErrorMessage(context.getString(R.string.title_select_agency_error));
                return false;
            }
        }
        return true;
    }

    @Override
    public void processAddToCartSuccess(@NonNull Context context, String message) {
        createDialogAtcSuccess(context, message);
    }

    @Override
    public void processAddToCartFailure(@NonNull Context context, String string) {
        viewListener.showErrorMessage(string);
    }

    @Override
    public void sendAnalyticsATCSuccess(@NonNull Context context,
                                        @NonNull ProductCartPass productCartPass,
                                        @NonNull OrderData orderData) {
        Map<String, String> values = new HashMap<>();
        values.put(context.getString(R.string.value_product_id), orderData.getProductId());
        values.put(context.getString(R.string.value_price_total),
                orderData.getPriceTotal().replace("Rp", "").replace(".", "").trim());
        values.put(context.getString(R.string.value_price_per_item),
                orderData.getPriceItem().replace("Rp", "").replace(".", "").trim());
        values.put(context.getString(R.string.value_quantity), orderData.getQuantity() + "");
        values.put(context.getString(R.string.value_seller_id), orderData.getShopId() + "");
        values.put(context.getString(R.string.value_products_category), productCartPass.getProductCategory());

        com.tokopedia.core.analytics.nishikino.model.Product product
                = new com.tokopedia.core.analytics.nishikino.model.Product();
        product.setProductID(orderData.getProductId());
        product.setProductName(productCartPass.getProductName());
        product.setPrice(CurrencyFormatHelper.convertRupiahToInt(orderData.getPriceItem()));
        product.setQty(orderData.getQuantity());

        GTMCart gtmCart = new GTMCart();
        gtmCart.addProduct(product.getProduct());
        gtmCart.setCurrencyCode("IDR");
        gtmCart.setAddAction(GTMCart.ADD_ACTION);

        UnifyTracking.eventATCSuccess(gtmCart);
    }

    @Override
    public void addToCartService(@NonNull Context context, @NonNull ATCResultReceiver atcReceiver,
                                 @NonNull OrderData orderData) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, context,
                ATCIntentService.class);
        intent.putExtra(ATCIntentService.EXTRA_ACTION,
                ATCIntentService.ACTION_ADD_TO_CART);
        intent.putExtra(ATCIntentService.EXTRA_RECEIVER, atcReceiver);
        intent.putExtra(ATCIntentService.EXTRA_ORDER_DATA, orderData);
        context.startService(intent);
    }

    @Override
    public void sendToGTM(@NonNull Context context) {
        UnifyTracking.eventATCAddAddress();
    }

    @Override
    public void setCacheCart(@NonNull Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, DrawerHelper.DRAWER_CACHE);
        cache.putInt(DrawerNotification.IS_HAS_CART, 1);
        cache.applyEditor();
    }

    private void createDialogAtcSuccess(final Context context, String message) {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
        myAlertDialog.setMessage(message);
        myAlertDialog.setCancelable(true);
        myAlertDialog.setPositiveButton(context.getString(R.string.title_pay),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        viewListener.navigateToActivity(
                                TransactionCartRouter.createInstanceCartActivity(context)
                        );
                        viewListener.closeView();
                    }

                });

        myAlertDialog.setNegativeButton(context.getString(R.string.title_continue_shopping),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        viewListener.closeView();
                    }
                });
        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewListener.showDialog(dialog);
    }

    @Override
    public void sendAppsFlyerATC(@NonNull Context context, @NonNull OrderData orderData) {
        Map<String, Object> values = new HashMap<>();

        values.put(AFInAppEventParameterName.CONTENT_ID, orderData.getProductId());
        values.put(AFInAppEventParameterName.CONTENT_TYPE, Jordan.AF_VALUE_PRODUCTTYPE);
        values.put(AFInAppEventParameterName.CURRENCY, "IDR");
        values.put(AFInAppEventParameterName.PRICE,
                CurrencyFormatHelper.convertRupiahToInt(orderData.getPriceItem()));
        values.put(AFInAppEventParameterName.QUANTITY, orderData.getQuantity());

        PaymentTracking.atcAF(values);

        UnifyTracking.eventATCBuy();
    }

    @Override
    public void onViewDestroyed() {
        keroNetInteractor.onViewDestroyed();
    }

    @Override
    public void processGetGTMTicker() {
        if (TrackingUtils.getGtmString(AppEventTracking.GTM.TICKER_ATC).equalsIgnoreCase("true")) {
            String message = TrackingUtils.getGtmString(AppEventTracking.GTM.TICKER_ATC_TEXT);
            viewListener.showTickerGTM(message);
        } else {
            viewListener.hideTickerGTM();
        }
    }

    @Override
    public boolean isAllowKeroAccess(AtcFormData data) {
        return data.getShop().getUt() != 0 && !TextUtils.isEmpty(data.getShop().getToken())
                && !TextUtils.isEmpty(data.getShop().getAvailShippingCode())
                && !TextUtils.isEmpty(data.getShop().getOriginId() + "")
                && !TextUtils.isEmpty(data.getShop().getOriginPostal())
                && !TextUtils.isEmpty(data.getForm().getDestination().getDistrictId())
                && !TextUtils.isEmpty(data.getForm().getDestination().getPostalCode())
                && !TextUtils.isEmpty(data.getForm().getProductDetail().getProductWeight())
                && data.getForm().getDestination() != null
                && !data.getForm().getDestination().getAddressId().isEmpty()
                && !data.getForm().getDestination().getAddressId().equals("0");
    }

    private boolean isAllowedCourier(AtcFormData data) {
        boolean allowedInstant = data.getForm().getDestination().getLatitude() != null
                && !data.getForm().getDestination().getLatitude().isEmpty();
        for (int i = 0; i < data.getForm().getShipment().size(); i++) {
            for (int j = 0; j < data.getForm().getShipment().get(i).getShipmentPackage().size(); j++) {
                ShipmentPackage shipmentPackage = data.getForm().getShipment().get(i)
                        .getShipmentPackage().get(j);
                boolean packageAvailable = shipmentPackage.getPackageAvailable() == 1;
                boolean isGojek = shipmentPackage.getShipmentId().equals(GOJEK_ID);
                if (packageAvailable && !isGojek)
                    return true;
                else if (allowedInstant)
                    return true;
            }
        }
        viewListener.showAddressErrorMessage();
        return false;
    }

    private void setInitialWeight(AtcFormData data) {
        data.getForm().getProductDetail().setProductWeight(calculateWeight(
                data.getForm().getProductDetail().getProductWeight(),
                data.getForm().getProductDetail().getProductMinOrder()));
    }

    @Override
    public String calculateWeight(String initialWeight, String quantity) {
        return String.valueOf(CommonUtils.round((Double.parseDouble(initialWeight) *
                Double.parseDouble(quantity)), 4));
    }

}
