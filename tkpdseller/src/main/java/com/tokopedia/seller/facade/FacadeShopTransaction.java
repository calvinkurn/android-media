package com.tokopedia.seller.facade;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.apiservices.shop.MyShopOrderService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.seller.selling.model.SellingStatusTxModel;
import com.tokopedia.seller.selling.model.orderShipping.OrderDestination;
import com.tokopedia.seller.selling.model.orderShipping.OrderDetail;
import com.tokopedia.seller.selling.model.orderShipping.OrderShipment;
import com.tokopedia.seller.selling.model.orderShipping.OrderShippingData;
import com.tokopedia.seller.selling.model.orderShipping.OrderShippingList;
import com.tokopedia.seller.selling.presenter.NewOrderImpl;
import com.tokopedia.seller.selling.presenter.ShippingImpl;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by Tkpd_Eka on 2/2/2015.
 * modified by m.normansyah on 1/05/2016 - move to retrofit
 * modified by zul & bas - merged model
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class FacadeShopTransaction {

    public static final String STUART = "STUART";
    public static final String FACADE_SHOP_TRANSACTION = "FacadeShopTransaction";

    public interface GetNewOrderListener {
        void OnSuccess(NewOrderImpl.Model model, OrderShippingData Result);

        void OnNoResult();

        void OnNoNextPage();

        void OnError();

        void onNetworkTimeOut();
    }

    public interface GetStatusListener {
        void OnSuccess(List<SellingStatusTxModel> model, OrderShippingData Result);

        void OnNoResult();

        void OnError();

        void onNetworkTimeOut();
    }

    public interface GetShippingListener {
        void OnSuccess(List<ShippingImpl.Model> model, OrderShippingData Result);

        void OnNoResult();

        void OnError();

        void onNetworkTimeOut();
    }

    private static final String ACT_GET_NEW_ORDER = "get_order";
    private static final String ACT_GET_STATUS = "get_status_order";
    private static final String ACT_GET_TX = "get_list_order";
    private static final String ACT_GET_SHIPPING = "get_process_order";


    private Context context;
    private String URL;
    private MyShopOrderService service = new MyShopOrderService();
    private GetNewOrderListener listener;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public static FacadeShopTransaction createInstance(Context context) {
        FacadeShopTransaction facade = new FacadeShopTransaction();
        facade.context = context;
        facade.URL = TkpdUrl.MY_SHOP_ORDER;
        return facade;
    }


    public CompositeSubscription getCompositeSubscription() {
        return compositeSubscription;
    }

    public void setCompositeSubscription(CompositeSubscription compositeSubscription) {
        this.compositeSubscription = compositeSubscription;
    }

    public void getNewOrderV4(PagingHandler page, String filter, int deadline, final GetNewOrderListener listener) {
        Observable<Response<TkpdResponse>> observable = service.getApi().getOrderNew(AuthUtil.generateParams(context, getNewOrderParam(page.getPage(), deadline, filter)));
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread()).unsubscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                listener.OnError();
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                if(responseData.isSuccessful()) {
                                    TkpdResponse response = responseData.body();

                                    if (response.getStringData() == null || response.getStringData().equals("{}")) {
                                        listener.OnNoResult();
                                        return;
                                    }

                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response.getStringData());

                                        Gson gson = new GsonBuilder().create();
                                        OrderShippingData data =
                                                gson.fromJson(jsonObject.toString(), OrderShippingData.class);
                                        listener.OnSuccess(getNewOrderModel(data), data);

                                    } catch (JSONException je) {
                                        Log.e(STUART, FACADE_SHOP_TRANSACTION + je.getLocalizedMessage());
                                    }
                                }else{
                                    listener.OnError();
                                }
                            }
                        }
                ));

        this.listener = listener;
    }

    private HashMap<String, String> getNewOrderParam(int page, int deadline, String filter) {
        HashMap<String, String> params = new HashMap<>();
        if (deadline > 0)
            params.put("deadline", Integer.toString(deadline));
        params.put("status", filter);
        params.put("page", Integer.toString(page));
        params.put("per_page", "10");
        params.put("bypass_hash", "1");
        params.put("user_id", SessionHandler.getLoginID(context));
        params.put("device_id", GCMHandler.getRegistrationId(context));
        return params;
    }


    public void getNewOrder(PagingHandler page, String filter, int deadline, final GetNewOrderListener listener) {
        getNewOrderV4(page, filter, deadline, listener);
    }


    private boolean isNewOrderHasResult(JSONObject Result) {
        return !Result.equals("null");
    }

    //================================================= GET STATUS ==========================================================================

    private HashMap<String, String> getStatusParam(PagingHandler page, String search) {
        HashMap<String, String> orderList = new HashMap<>();
        orderList.put("deadline", "");
        orderList.put("end", "");
        if (search.length() > 0)
            orderList.put("invoice", search);
        orderList.put("page", Integer.toString(page.getPage()));
        orderList.put("shipment_id", "");
        orderList.put("start", "");
        orderList.put("status", ACT_GET_STATUS);
        return orderList;
    }


    public void getStatusV4(PagingHandler page, String search, final GetStatusListener listener) {
        compositeSubscription.add(new MyShopOrderService().getApi().getOrderStatus(
                AuthUtil.generateParams(context, getStatusParam(page, search))
        )
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
                                listener.OnError();
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                if(responseData.isSuccessful()) {
                                    TkpdResponse response = responseData.body();

                                    if (response.getStringData() == null || response.getStringData().equals("{}")) {
                                        listener.OnNoResult();
                                        return;
                                    }

                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response.getStringData());

                                        Gson gson = new GsonBuilder().create();
                                        OrderShippingData data =
                                                gson.fromJson(jsonObject.toString(), OrderShippingData.class);
                                        listener.OnSuccess(getStatusModel(data), data);

                                    } catch (JSONException je) {
                                        Log.e(STUART, FACADE_SHOP_TRANSACTION + je.getLocalizedMessage());
                                    }
                                }else{
                                    listener.OnError();
                                }
                            }
                        }
                ));
    }



    private List<SellingStatusTxModel> getStatusModel(OrderShippingData data){
        List<SellingStatusTxModel> modelList = new ArrayList<>();
        for(int i = 0; i< data.getOrderShippingList().size(); i++){
            OrderShippingList orderList = data.getOrderShippingList().get(i);
            SellingStatusTxModel model = new SellingStatusTxModel();
            model.DataList = orderList;
            model.UserName = orderList.getOrderCustomer().getCustomerName();
            model.AvatarUrl = orderList.getOrderCustomer().getCustomerImage();
            model.BuyerId = orderList.getOrderCustomer().getCustomerId();
            model.Permission = Integer.toString(data.getOrder().getIsAllowManageTx());
            model.Komisi = orderList.getOrderPayment().getPaymentKomisi();
            model.Deadline = orderList.getOrderPayment().getPaymentVerifyDate();
            model.Invoice = orderList.getOrderDetail().getDetailInvoice();
            model.LastStatus = orderList.getOrderLast().getLastSellerStatus().replace("<br>", "\n").replace("<br/>", "\n");
            model.Pdf = orderList.getOrderDetail().getDetailPdf();
            model.PdfUri = orderList.getOrderDetail().getDetailPdfUri();
            model.OrderId = Integer.toString(orderList.getOrderDetail().getDetailOrderId());
            model.OrderStatus = Integer.toString(orderList.getOrderDetail().getDetailOrderStatus());
            model.DeadlineFinish = orderList.getOrderDeadline().getDeadlineFinishDate();
            model.RefNum = orderList.getOrderLast().getLastShippingRefNum();
            model.ShippingID = orderList.getOrderShipment().getShipmentId();
            model.isPickUp = orderList.getIsPickUp();
            modelList.add(model);
        }
        return modelList;
    }

    private HashMap<String, String> getOrderListParam(PagingHandler page, String search, String filter, String startDate, String endDate) {
        HashMap<String, String> orderList = new HashMap<>();
        orderList.put("deadline", "");
        orderList.put("page", page.getPage() + "");
        orderList.put("end", endDate);
        orderList.put("start", startDate);
        orderList.put("invoice", search);
        orderList.put("status", filter);
        orderList.put("per_page", "8");
        orderList.put("shipment_id", "");
        return orderList;
    }

    /**
     * @param page
     * @param search
     * @param filter
     * @param startDate
     * @param endDate
     * @param listener
     */
    public void getTxListV4(PagingHandler page, String search, String filter, String startDate, String endDate, final GetStatusListener listener){
        compositeSubscription.add(new MyShopOrderService().getApi().getOrderList(
                AuthUtil.generateParams(context, getOrderListParam(page, search, filter, startDate, endDate))
        )
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
                                listener.OnError();
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                if(responseData.isSuccessful()) {
                                    TkpdResponse response = responseData.body();
                                    if (response.getStringData() == null || response.getStringData().equals("{}")) {
                                        listener.OnNoResult();
                                        return;
                                    }

                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response.getStringData());

                                        Gson gson = new GsonBuilder().create();
                                        OrderShippingData data =
                                                gson.fromJson(jsonObject.toString(), OrderShippingData.class);

                                        ;
                                        listener.OnSuccess(getStatusModel(data), data);

                                    } catch (JSONException je) {
                                        Log.e(STUART, FACADE_SHOP_TRANSACTION + je.getLocalizedMessage());
                                    }
                                }else{
                                    listener.OnError();
                                }
                            }
                        }
                ));
    }

    private HashMap<String, String> generateShippingOrder(PagingHandler page, String invoice, int deadline, int service) {
        HashMap<String, String> hashMap = new HashMap<>();
        if (invoice.length() > 0)
            hashMap.put("invoice", invoice);
        if (deadline > 0)
            hashMap.put("deadline", deadline + "");
        if (service > 0)
            hashMap.put("shipment_id", service + "");
        hashMap.put("page", page.getPage() + "");
        hashMap.put("end", "");
        hashMap.put("per_page", "8");
        hashMap.put("start", "");
        hashMap.put("status", "");
        return hashMap;
    }

    public void getShippingV4(PagingHandler page, String invoice, int deadline, int service, final GetShippingListener listener) {
        compositeSubscription.add(new MyShopOrderService().getApi().getOrderProcess(AuthUtil.generateParams(context, generateShippingOrder(
                page, invoice, deadline, service
        ))).subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                listener.OnError();
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                if(responseData.isSuccessful()) {
                                    TkpdResponse response = responseData.body();

                                    if (response.getStringData() == null || response.getStringData().equals("{}")) {
                                        listener.OnNoResult();
                                        return;
                                    }

                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response.getStringData());

                                        Gson gson = new GsonBuilder().create();
                                        OrderShippingData data =
                                                gson.fromJson(jsonObject.toString(), OrderShippingData.class);
                                        listener.OnSuccess(getShippingModel(data), data);

                                    } catch (JSONException je) {
                                        Log.e(STUART, FACADE_SHOP_TRANSACTION + je.getLocalizedMessage());
                                    }
                                }else{
                                    listener.OnError();
                                }
                            }
                        }
                ));
    }

    private List<ShippingImpl.Model> getShippingModel(OrderShippingData data) {
        List<ShippingImpl.Model> modelList = new ArrayList<>();
        for(int i = 0; i<data.getOrderShippingList().size(); i++){
            ShippingImpl.Model model = new ShippingImpl.Model();
            OrderShippingList orderShippingList = data.getOrderShippingList().get(i);
            model.orderShippingList = data.getOrderShippingList().get(i);
            model.UserName = orderShippingList.getOrderCustomer().getCustomerName();
            model.AvatarUrl = orderShippingList.getOrderCustomer().getCustomerImage();
            model.Komisi = orderShippingList.getOrderPayment().getPaymentKomisi();
            model.Deadline = orderShippingList.getOrderDeadline().getDeadlineShipping();
            OrderDetail orderDetail = orderShippingList.getOrderDetail();
            model.Invoice = orderDetail.getDetailInvoice();
            model.BuyerId = orderShippingList.getOrderCustomer().getCustomerId();
            model.Permission = data.getOrder().getIsAllowManageTx() + "";
            model.OrderId = orderDetail.getDetailOrderId() + "";
            model.Pdf = orderDetail.getDetailPdf();
            model.PdfUri = orderDetail.getDetailPdfUri();
            model.ShippingPrice = orderDetail.getDetailShippingPriceIdr();
            OrderDestination orderDestination = orderShippingList.getOrderDestination();
            model.ReceiverName = orderDestination.getReceiverName();
            model.Dest = orderDestination.getAddressDistrict() + ", " + orderDestination.getAddressCity() + " " + orderDestination.getAddressProvince();
            OrderShipment orderShipment = orderShippingList.getOrderShipment();
            model.Shipping = orderShipment.getShipmentName() + " " + orderShipment.getShipmentProduct();
            model.Checked = false;
            model.ErrorRow = false;
            model.ErrorMsg = "";
            model.RefNum = "";
            if (orderDetail.getDetailDropshipName() != null) {
                model.SenderDetail = orderDetail.getDetailDropshipName() + " (" +
                        orderDetail.getDetailDropshipTelp() + ")";
            } else {
                model.SenderDetail = "";
            }
            modelList.add(model);
        }
        return modelList;
    }

    private NewOrderImpl.Model getNewOrderModel(OrderShippingData data) {
        NewOrderImpl.Model model = new NewOrderImpl.Model();
        model.DataList = data.getOrderShippingList();
        model.Permission = data.getOrder().getIsAllowManageTx() + "";
        return model;
    }

}