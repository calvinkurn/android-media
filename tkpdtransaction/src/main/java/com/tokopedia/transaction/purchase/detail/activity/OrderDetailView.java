package com.tokopedia.transaction.purchase.detail.activity;

import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.transaction.purchase.detail.dialog.AcceptOrderDialog;
import com.tokopedia.transaction.purchase.detail.dialog.AcceptPartialOrderDialog;
import com.tokopedia.transaction.purchase.detail.dialog.ComplaintDialog;
import com.tokopedia.transaction.purchase.detail.dialog.FinishOrderDialog;
import com.tokopedia.transaction.purchase.detail.fragment.CancelOrderFragment;
import com.tokopedia.transaction.purchase.detail.fragment.CancelSearchFragment;
import com.tokopedia.transaction.purchase.detail.fragment.CancelShipmentFragment;
import com.tokopedia.transaction.purchase.detail.fragment.ChangeAwbFragment;
import com.tokopedia.transaction.purchase.detail.fragment.RejectOrderBuyerRequest;
import com.tokopedia.transaction.purchase.detail.fragment.RejectOrderCourierProblemFragment;
import com.tokopedia.transaction.purchase.detail.fragment.RejectOrderEmptyProductFragment;
import com.tokopedia.transaction.purchase.detail.fragment.RejectOrderEmptyVarianFragment;
import com.tokopedia.transaction.purchase.detail.fragment.RejectOrderShopClosedFragment;
import com.tokopedia.transaction.purchase.detail.fragment.RejectOrderWeightPriceFragment;
import com.tokopedia.transaction.purchase.detail.fragment.RequestPickupFragment;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.listener.ToolbarChangeListener;

/**
 * Created by kris on 11/13/17. Tokopedia
 */

public interface OrderDetailView extends FinishOrderDialog.FinishOrderDialogListener,
        ComplaintDialog.ComplaintDialogListener,
        CancelOrderFragment.CancelOrderListener,
        CancelSearchFragment.CancelSearchReplacementListener,
        AcceptOrderDialog.AcceptOrderListener,
        AcceptPartialOrderDialog.PartialDialogListener,
        ChangeAwbFragment.ChangeAwbListener,
        CancelShipmentFragment.CancelShipmentListener,
        RejectOrderEmptyProductFragment.RejectOrderEmptyStockListener,
        RejectOrderCourierProblemFragment.RejectOrderCourierReasonListener,
        RejectOrderBuyerRequest.RejectOrderBuyerRequestListener,
        RejectOrderShopClosedFragment.RejectOrderShopClosedListener,
        RejectOrderEmptyVarianFragment.RejectOrderEmptyVarianFragmentListener,
        RejectOrderWeightPriceFragment.RejectOrderChangeWeightPriceListener,
        RequestPickupFragment.ConfirmRequestPickupListener,
        FragmentGeneralWebView.OnFragmentInteractionListener,
        ToolbarChangeListener {

    void onReceiveDetailData(OrderDetailData data);

    void onError(String errorMessage);

    void goToProductInfo(ProductPass productPass);

    void trackShipment(String orderId);

    void showConfirmDialog(String orderId, String orderStatus);

    void showComplaintDialog(String shopName, String orderId);

    void onAskSeller(OrderDetailData data);

    void onAskBuyer(OrderDetailData data);

    void onOrderFinished(String message);

    void onOrderCancelled(String message);

    void onRequestCancelOrder(OrderDetailData data);

    void onCancelSearchReplacement(OrderDetailData data);

    void onSellerConfirmShipping(OrderDetailData data);

    void onAcceptOrder(OrderDetailData data);

    void onAcceptOrderPartially(OrderDetailData data);

    void onRequestPickup(OrderDetailData data);

    void onChangeCourier(OrderDetailData data);

    void onRejectOrder(OrderDetailData data);

    void onRejectShipment(OrderDetailData data);

    void onSearchCancelled(String message);

    void onChangeAwb(OrderDetailData data);

    void showMainViewLoadingPage();

    void hideMainViewLoadingPage();

    void onViewComplaint(OrderDetailData resoId);

    void showSnackbar(String errorMessage);

    void dismissSellerActionFragment();

    void dismissRejectOrderActionFragment();

    void onRefreshActivity();

    void dismissActivity();

    void showProgressDialog();

    void dismissProgressDialog();

}
