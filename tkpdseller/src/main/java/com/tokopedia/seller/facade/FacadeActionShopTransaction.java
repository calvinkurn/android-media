package com.tokopedia.seller.facade;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.R;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.fragment.FragmentShopShippingDetailV2;
import com.tokopedia.seller.selling.network.apiservices.MyShopOrderActService;
import com.tokopedia.core.network.apiservices.shop.MyShopOrderService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.seller.selling.model.modelConfirmShipping.Data;
import com.tokopedia.seller.selling.model.modelEditRef.DataResponseEditRef;
import com.tokopedia.seller.selling.model.modelShippingForm.DataResponseGetShipment;
import com.tokopedia.seller.selling.model.modelShippingForm.Shipment;
import com.tokopedia.seller.selling.model.modelShippingForm.ShipmentPackage;
import com.tokopedia.core.var.TkpdUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Tkpd_Eka on 2/2/2015.
 * move to {@link com.tokopedia.core.selling.SellingService}
 */
@Deprecated
public class FacadeActionShopTransaction {

    private static final String STUART = "STUART";
    private static final String FACADE_SHOP_TRANSACTION = "FACADE SHOP TRANSACTION";

    public interface OnGetEditShippingListener {
        void onSuccess(List<FragmentShopShippingDetailV2.ShippingServices> serviceList);

        void onFailed();
    }

    public interface OnConfirmMultiShippingListener {
        void onSuccess();

        void onFailed();
    }

    public interface OnEditRefNumListener {
        void onSuccess(String refNum);

        void onFailed(String errorMsg);
    }

    public interface OnRetryPickupListener {
        void onSuccess(String successMessage);

        void onFailed(String errorMessage);
    }

    public static FacadeActionShopTransaction createInstance(Context context, String orderId) {
        FacadeActionShopTransaction facade = new FacadeActionShopTransaction();
        facade.context = context;
        facade.URL = TkpdUrl.MY_SHOP_ORDER;
        facade.orderId = orderId;
        return facade;
    }

    public static FacadeActionShopTransaction createInstanceNoId(Context context) {
        FacadeActionShopTransaction facade = new FacadeActionShopTransaction();
        facade.context = context;
        facade.URL = TkpdUrl.MY_SHOP_ORDER;
        return facade;
    }

    private Context context;
    private String URL;
    private String orderId;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public CompositeSubscription getCompositeSubscription() {
        return compositeSubscription;
    }

    public void setCompositeSubscription(CompositeSubscription compositeSubscription) {
        this.compositeSubscription = compositeSubscription;
    }

    public void getShippingForm(final OnGetEditShippingListener listener) {
        compositeSubscription.add(new MyShopOrderService().getApi().getEditShippingForm(AuthUtil.generateParams(context, new HashMap<String, String>()))
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                listener.onFailed();
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                TkpdResponse response = responseData.body();

                                if (response.getStringData() == null || response.getStringData().equals("{}")) {
                                    listener.onFailed();
                                }

                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.getStringData());

                                    Gson gson = new GsonBuilder().create();
                                    DataResponseGetShipment data =
                                            gson.fromJson(jsonObject.toString(), DataResponseGetShipment.class);
                                    listener.onSuccess(getServicesModel(data));
                                } catch (JSONException je) {
                                    Log.e(STUART, FACADE_SHOP_TRANSACTION + je.getLocalizedMessage());
                                }
                            }
                        }
                ));
    }


    private List<FragmentShopShippingDetailV2.ShippingServices> getServicesModel(DataResponseGetShipment data) {
        List<FragmentShopShippingDetailV2.ShippingServices> serviceList = new ArrayList<>();
        FragmentShopShippingDetailV2.ShippingServices index0 = new FragmentShopShippingDetailV2.ShippingServices();
        index0.serviceName = context.getString(R.string.item_choose_agent);
        index0.serviceId = "";
        serviceList.add(index0);
        List<Shipment> shipmentList = data.getShipment();
        for (int i = 0; i < shipmentList.size(); i++) {
            Shipment shipment = shipmentList.get(i);
            FragmentShopShippingDetailV2.ShippingServices service = new FragmentShopShippingDetailV2.ShippingServices();
            service.serviceId = shipment.getShipmentId();
            service.serviceName = shipment.getShipmentName();
            service.packageId.add("");
            service.packageList.add(context.getString(R.string.item_choose_option));
            List<ShipmentPackage> shipmentPackages = shipment.getShipmentPackage();
            for (int j = 0; j < shipmentPackages.size(); j++) {
                ShipmentPackage shipmentPackage = shipmentPackages.get(j);
                service.packageId.add(shipmentPackage.getSpId());
                service.packageList.add(shipmentPackage.getName());
            }
            serviceList.add(service);
        }
        return serviceList;
    }

    public void cancelShipping(String remark, final OnConfirmMultiShippingListener listener) {
        HashMap<String, String> paramsCancel = new HashMap<>();
        paramsCancel.put("order_id", orderId);
        paramsCancel.put("action_type", "reject");
        paramsCancel.put("reason", remark);
        compositeSubscription.add(new MyShopOrderActService().getApi().proceedShipping(AuthUtil.generateParams(context, paramsCancel))
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                listener.onFailed();
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                TkpdResponse response = responseData.body();

                                if (response.getStringData() == null || response.getStringData().equals("{}")) {
                                    listener.onFailed();
                                }

                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.getStringData());

                                    Gson gson = new GsonBuilder().create();
                                    Data data = gson.fromJson(jsonObject.toString(), Data.class);

                                    if (data.getIsSuccess() == 1) {
                                        listener.onSuccess();
                                    } else {
                                        Toast.makeText(context, response.getErrorMessages().get(0), Toast.LENGTH_SHORT).show();
                                        listener.onFailed();
                                    }
                                } catch (JSONException je) {
                                    Log.e(STUART, FACADE_SHOP_TRANSACTION + je.getLocalizedMessage());
                                }
                            }
                        }
                ));
    }

    public void editRefNum(final String refNum, final OnEditRefNumListener listener) {
        HashMap<String, String> paramsEditRef = new HashMap<>();
        paramsEditRef.put("order_id", orderId);
        paramsEditRef.put("shipping_ref", refNum);
        compositeSubscription.add(new MyShopOrderActService().getApi().editShippingRef(AuthUtil.generateParams(context, paramsEditRef))
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                listener.onFailed("terjadi masalah koneksi");
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                TkpdResponse response = responseData.body();

                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.getStringData());

                                    Gson gson = new GsonBuilder().create();
                                    DataResponseEditRef data = gson.fromJson(jsonObject.toString(), DataResponseEditRef.class);

                                    if (data.getIsSuccess() == 1) {
                                        listener.onSuccess(refNum);
                                    } else {
                                        Toast.makeText(context, response.getErrorMessages().get(0), Toast.LENGTH_SHORT).show();
                                        listener.onFailed(response.getErrorMessages().get(0));
                                    }
                                } catch (JSONException je) {
                                    Log.e(STUART, FACADE_SHOP_TRANSACTION + je.getLocalizedMessage());
                                }
                            }
                        }
                ));
    }

    public void retryCourierPickup(final OnRetryPickupListener listener) {
        TKPDMapParam<String, String> paramsRetryPickup = new TKPDMapParam<>();
        paramsRetryPickup.put("order_id", orderId);
        compositeSubscription.add(new MyShopOrderActService().getApi()
                .retryPickUp(AuthUtil.generateParamsNetwork(context, paramsRetryPickup))
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<TkpdResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onFailed(context.getString(R.string.msg_network_error_1));
                    }

                    @Override
                    public void onNext(Response<TkpdResponse> tkpdResponse) {
                        if(!tkpdResponse.body().isError()) {
                            listener.onSuccess(tkpdResponse.body().getStatusMessages().get(0));
                        } else {
                            listener.onFailed(tkpdResponse.body().getErrorMessageJoined());
                        }
                    }
                }));
    }
}
