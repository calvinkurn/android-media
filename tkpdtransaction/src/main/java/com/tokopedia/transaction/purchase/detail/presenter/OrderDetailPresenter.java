package com.tokopedia.transaction.purchase.detail.presenter;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.purchase.detail.activity.OrderDetailView;
import com.tokopedia.transaction.purchase.detail.model.detail.editmodel.OrderDetailShipmentModel;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.EmptyVarianProductEditable;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.WrongProductPriceWeightEditable;

import java.util.List;

/**
 * Created by kris on 11/10/17. Tokopedia
 */

public interface OrderDetailPresenter {

    void setMainViewListener(OrderDetailView view);

    void fetchData(Context context, String orderId, int userMode);

    void processInvoice(Context context, OrderDetailData data);

    void processToShop(Context context, OrderDetailData data);

    void processShowComplain(Context context, OrderDetailData data);

    void processComplaint(Context context, OrderDetailData data);

    void processConfirmDeliver(Context context, OrderDetailData data);

    void processTrackOrder(Context context, OrderDetailData data);

    void processRequestCancelOrder(Context context, OrderDetailData data);

    void processSeeAllHistories(Context context, OrderDetailData data);

    void processAskSeller(Context context, OrderDetailData data);

    void processChangeAwb(Context context, OrderDetailData data);

    void processSellerConfirmShipping(Context context, OrderDetailData data);

    void processAskBuyer(Context context, OrderDetailData data);

    void processAcceptOrder(Context context, OrderDetailData data);

    void processAcceptPartialOrder(Context context, OrderDetailData data);

    void processRequestPickup(Context context, OrderDetailData data);

    void processChangeCourier(Context context, OrderDetailData data);

    void processRejectOrder(Context context, OrderDetailData data);

    void processRejectShipment(Context context, OrderDetailData data);

    void processCancelSearch(Context context, OrderDetailData data);

    void acceptOrder(Context context, String orderId);

    void confirmChangeAwb(Context context, String orderId, String refNumber);

    void partialOrder(Context context, String orderId, String reason, String quantityAccept);

    void rejectOrder(Context context, String orderId, String reason);

    void rejectOrderGenericReason(Context context, TKPDMapParam<String, String> reasonParam);

    void rejectOrderChangeVarian(Context context,
                                 List<EmptyVarianProductEditable> emptyVarianProductEditables);

    void rejectOrderChangeWeightPrice(Context context,
                                      List<WrongProductPriceWeightEditable> editables);

    void processInstantCourierShipping(Context context,
                                       String orderId);

    void processShipping(Context context,
                         OrderDetailShipmentModel shipmentModel);

    void cancelShipping(Context context, String orderId, String reason);

    void retryOrder(Context context, OrderDetailData data);

    void processFinish(Context context, String orderId, String orderStatus);

    void cancelOrder(Context context, String orderId, String notes);

    void cancelReplacement(Context context, String orderId, int reasonCode, String reasonText);

    void onDestroyed();

    String SHIPPING_REF_KEY = "shipping_ref";

    String ACTION_TYPE_KEY = "action_type";

    String ORDER_ID_KEY = "order_id";

    String LANGUAGE_KEY = "lang";

    String OS_TYPE_KEY = "os_type";

    String REQUEST_BY_KEY = "request_by";

    String REASON_KEY = "reason";

    String QUANTITY_ACCEPT_KEY = "qty_accept";

    String REASON_CODE_KEY = "reason_code";

    String SHIPMENT_ID_KEY = "shipment_id";

    String SHIPMENT_NAME_KEY = "shipment_name";

    String SERVICE_ID_KEY = "sp_id";

    String REASON_CANCEL_KEY = "reason_cancel";

    String USER_ID_KEY = "user_id";

    String REPLACEMENT_REASON_CODE = "r_code";

    String INDONESIAN_LANGUAGE_CONSTANT = "id";

    String ACCEPT_ORDER_CONSTANT = "accept";

    String PARTIAL_ORDER_CONSTANT = "partial";

    String REJECT_ORDER_CONSTANT = "reject";

    String CHANGE_VARIAN_CODE = "2";

    String CHANGE_PRODUCT_CODE = "3";

    String CONFIRM_SHIPPING_CONSTANT = "confirm";

}
