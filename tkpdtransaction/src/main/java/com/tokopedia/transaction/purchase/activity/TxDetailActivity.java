package com.tokopedia.transaction.purchase.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.IntentService;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.customView.OrderStatusView;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.bottomsheet.BottomSheetCallAction;
import com.tokopedia.track.TrackApp;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.purchase.adapter.TxProductListAdapter;
import com.tokopedia.transaction.purchase.listener.TxDetailViewListener;
import com.tokopedia.transaction.purchase.model.response.txlist.OrderData;
import com.tokopedia.transaction.purchase.presenter.TxDetailPresenter;
import com.tokopedia.transaction.purchase.presenter.TxDetailPresenterImpl;
import com.tokopedia.transaction.purchase.receiver.TxListUIReceiver;
import com.tokopedia.transaction.utils.LinearLayoutManagerNonScroll;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author by Angga.Prasetiyo on 28/04/2016.
 */
public class TxDetailActivity extends BasePresenterActivity<TxDetailPresenter> implements
        TxDetailViewListener, TxProductListAdapter.ActionListener {
    private static final String EXTRA_ORDER_DATA = "EXTRA_ORDER_DATA";

    @BindView(R2.id.invoice_text)
    TextView tvInvoiceNumber;
    @BindView(R2.id.shop_name)
    TextView tvShopName;
    @BindView(R2.id.product_list)
    RecyclerView rvProductList;
    @BindView(R2.id.shipping_cost)
    TextView tvShippingCost;
    @BindView(R2.id.additional_cost)
    TextView tvAdditionalCost;
    @BindView(R2.id.destination)
    TextView tvDestination;
    @BindView(R2.id.destination_detail)
    TextView tvDestinationDetail;
    @BindView(R2.id.quantity)
    TextView tvQuantity;
    @BindView(R2.id.grand_total)
    TextView tvGrandTotal;
    @BindView(R2.id.transaction)
    TextView tvTransactionDate;
    @BindView(R2.id.see_all)
    TextView btnShowMoreHistory;
    @BindView(R2.id.receive_btn)
    TextView btnReceiveOrder;
    @BindView(R2.id.reject_btn)
    TextView btnRejectOrder;
    @BindView(R2.id.ask_seller)
    TextView btnAskSeller;
    @BindView(R2.id.track_btn)
    TextView btnTrackOrder;
    @BindView(R2.id.complain_but)
    TextView btnComplainOrder;
    @BindView(R2.id.btn_do_complain)
    TextView btnDoComplain;
    @BindView(R2.id.upload_proof)
    TextView btnUploadProof;
    @BindView(R2.id.btn_request_cancel_order)
    TextView btnRequestCancelOrder;
    @BindView(R2.id.sender_name)
    TextView tvSenderName;
    @BindView(R2.id.sender_phone)
    TextView tvSenderPhone;
    @BindView(R2.id.sender_form)
    View holderFormSender;
    @BindView(R2.id.order_status_layout)
    LinearLayout holderOrderStatus;
    @BindView(R2.id.instant_courier_driver_layout)
    LinearLayout instantCourierDriverLayout;
    @BindView(R2.id.driver_photo)
    ImageView driverPhoto;
    @BindView(R2.id.driver_name)
    TextView driverName;
    @BindView(R2.id.driver_phone)
    TextView driverPhone;
    @BindView(R2.id.driver_license)
    TextView driverLicense;
    @BindView(R2.id.btn_call_driver)
    TextView btnCallOnDemandDriver;

    private OrderData orderData;
    private TkpdProgressDialog progressDialog;
    private TxProductListAdapter productListAdapter;

    public static Intent createInstance(Context context, OrderData orderData) {
        Intent intent = new Intent(context, TxDetailActivity.class);
        intent.putExtra(EXTRA_ORDER_DATA, orderData);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.orderData = extras.getParcelable(EXTRA_ORDER_DATA);
    }

    @Override
    protected void initialPresenter() {
        presenter = new TxDetailPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transaction_detail_tx_module;
    }

    @Override
    protected void initView() {
        progressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        rvProductList.setLayoutManager(new LinearLayoutManagerNonScroll(this));
    }

    @Override
    protected void setViewListener() {
        rvProductList.setAdapter(productListAdapter);
    }

    @Override
    protected void initVar() {
        this.productListAdapter = new TxProductListAdapter(this);
    }

    @Override
    protected void setActionVar() {
        renderActionButton();
        renderOrderStatus();
        renderDetailInfo();
        renderProductList();
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_TX_PEOPLE_DETAIL;
    }

    private void renderProductList() {
        productListAdapter.addAllDataList(orderData.getOrderProducts());
    }

    private void renderOrderStatus() {
        holderOrderStatus.removeAllViews();
        int length = orderData.getOrderHistory().size();
        btnShowMoreHistory.setVisibility(View.GONE);
        if (length > 2) {
            btnShowMoreHistory.setVisibility(View.VISIBLE);
            length = 2;
        }
        for (int i = 0; i < length; i++) {
            alterHistoryComment(orderData.getOrderHistory().get(i).getHistoryComments(), i);
            OrderStatusView orderStatusView = new OrderStatusView(this,
                    orderData.getOrderHistory().get(i));
            holderOrderStatus.addView(orderStatusView.getView());
        }
    }

    @SuppressWarnings("deprecation")
    private void renderDetailInfo() {
        if (orderData.getOrderDetail().getDetailDropshipName() != null
                && !orderData.getOrderDetail().getDetailDropshipName().equals("0")
                && orderData.getOrderDetail().getDetailDropshipTelp() != null
                && !orderData.getOrderDetail().getDetailDropshipTelp().equals("0")) {
            tvSenderName.setText(orderData.getOrderDetail().getDetailDropshipName());
            tvSenderPhone.setText(orderData.getOrderDetail().getDetailDropshipTelp());
            holderFormSender.setVisibility(View.VISIBLE);
        } else {
            holderFormSender.setVisibility(View.GONE);
        }
        tvInvoiceNumber.setText(orderData.getOrderDetail().getDetailInvoice());
        tvShopName.setText(MethodChecker.fromHtml(orderData.getOrderShop().getShopName()));
        tvAdditionalCost.setText(orderData.getOrderDetail().getDetailTotalAddFeeIdr());
        tvShippingCost.setText(orderData.getOrderDetail().getDetailShippingPriceIdr());
        tvQuantity.setText(MessageFormat.format("{0} Barang",
                orderData.getOrderDetail().getDetailQuantity()));
        tvGrandTotal.setText(orderData.getOrderDetail().getDetailOpenAmountIdr());
        tvTransactionDate.setText(orderData.getOrderDetail().getDetailOrderDate());
        tvDestination.setText(MessageFormat.format("{0} {1}",
                MethodChecker.fromHtml(orderData.getOrderShipment().getShipmentName() + " -"),
                orderData.getOrderShipment().getShipmentProduct()));
        tvDestinationDetail.setText(orderData.getOrderDestination().getDetailDestination()
                .replace("&amp;", "&"));
        if (orderData.getDriverInfo() != null
                && !orderData.getDriverInfo().getDriverName().isEmpty()) {
            instantCourierDriverLayout.setVisibility(View.VISIBLE);
            ImageHandler.loadImageCircle2(this, driverPhoto, orderData.getDriverInfo().getDriverPhoto());
            driverName.setText(orderData.getDriverInfo().getDriverName());
            driverPhone.setText(orderData.getDriverInfo().getDriverPhone());
            btnCallOnDemandDriver.setOnClickListener(getClickListenerCallDriver());
            if (orderData.getDriverInfo().getLicenseNumber().isEmpty()) {
                driverLicense.setVisibility(View.GONE);
            } else {
                driverLicense.setVisibility(View.VISIBLE);
                driverLicense.setText(orderData.getDriverInfo().getLicenseNumber());
            }
        } else instantCourierDriverLayout.setVisibility(View.GONE);
    }

    @NonNull
    private View.OnClickListener getClickListenerCallDriver() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BottomSheetCallAction.Builder(TxDetailActivity.this)
                        .actionListener(new BottomSheetCallAction.ActionListener() {
                            @Override
                            public void onCallAction1Clicked(BottomSheetCallAction.CallActionData callActionData) {
                                openDialCaller(callActionData.getPhoneNumber());
                            }

                            @Override
                            public void onCallAction2Clicked(BottomSheetCallAction.CallActionData callActionData) {
                                openSmsApplication(callActionData.getPhoneNumber());
                            }
                        })
                        .callActionData(
                                new BottomSheetCallAction.CallActionData().setPhoneNumber(
                                        orderData.getDriverInfo().getDriverPhone()
                                ).setLabelAction1(
                                        getString(R.string.label_call_ondemand_driver_logistic_action_1)
                                ).setLabelAction2(
                                        getString(R.string.label_call_ondemand_driver_logistic_action_2)
                                )
                        ).build().show();
            }
        };
    }

    void openSmsApplication(String phoneNumber) {
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
        try {
            startActivity(smsIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            showToastMessage(getString(R.string.error_message_sms_app_not_found));
        }
    }

    void openDialCaller(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            showToastMessage(getString(R.string.error_message_phone_dialer_app_not_found));
        }
    }

    private void renderActionButton() {
        String statusOrder = String.valueOf(orderData.getOrderDetail().getDetailOrderStatus());
        btnReceiveOrder.setVisibility(statusOrder.equals(getString(R.string.ORDER_DELIVERED))
                || statusOrder.equals(getString(R.string.ORDER_DELIVERY_AUTOMATIC))
                || statusOrder.equals(getString(R.string.ORDER_DELIVERY_FAILURE))
                || statusOrder.equals(getString(R.string.ORDER_WAITING_STATUS_FROM_SHIPPING_AGENCY))
                || statusOrder.equals(getString(R.string.ORDER_SHIPPING))
                || statusOrder.equals(getString(R.string.ORDER_SHIPPING_REF_NUM_EDITED))
                || statusOrder.equals(getString(R.string.ORDER_SHIPPING_TRACKER_INVALID))
                ? View.VISIBLE : View.GONE);

        btnTrackOrder.setVisibility(
                statusOrder.equals(getString(R.string.ORDER_WAITING_STATUS_FROM_SHIPPING_AGENCY))
                        || statusOrder.equals(getString(R.string.ORDER_SHIPPING))
                        || statusOrder.equals(getString(R.string.ORDER_SHIPPING_REF_NUM_EDITED))
                        || statusOrder.equals(getString(R.string.ORDER_SHIPPING_TRACKER_INVALID))
                        ? View.VISIBLE : View.GONE
        );


        btnRejectOrder.setVisibility(View.GONE);

        if (orderData.getOrderButton().getButtonResCenterGoTo().equals("1")) {
            btnDoComplain.setVisibility(View.GONE);
        } else {
            btnDoComplain.setVisibility(orderData.getOrderButton().getButtonComplaintReceived().equals("1")
                    || orderData.getOrderButton().getButtonComplaintNotReceived().equals("1")
                    ? View.VISIBLE : View.GONE);
        }
        btnComplainOrder.setVisibility(orderData.getOrderButton().getButtonResCenterGoTo() != null
                && orderData.getOrderButton().getButtonResCenterGoTo().equals("1")
                ? View.VISIBLE : View.GONE);

        btnRequestCancelOrder.setVisibility(
                orderData.getOrderButton().getButtonCancelRequest() != null
                        && orderData.getOrderButton().getButtonCancelRequest().equals("1")
                        ? View.VISIBLE : View.GONE
        );

        btnUploadProof.setVisibility(View.GONE);

        btnAskSeller.setVisibility(orderData.getOrderButton().getButtonAskSeller() != null &&
                orderData.getOrderButton().getButtonAskSeller().equals("1")
                ? View.VISIBLE : View.GONE);
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        this.startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        this.startActivity(intent);
    }

    @Override
    public void showProgressLoading() {
        progressDialog.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void showToastMessage(String message) {
        View viewParent = findViewById(android.R.id.content);
        if (viewParent != null) Snackbar.make(viewParent, message, Snackbar.LENGTH_SHORT).show();
        else Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog(Dialog dialog) {
        if (!dialog.isShowing()) dialog.show();
    }

    @Override
    public void dismissDialog(Dialog dialog) {
        if (dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

    }

    @Override
    public String getStringFromResource(@StringRes int resId) {
        return getString(resId);
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams
    ) {
        return null;
    }

    @Override
    public void closeView() {
        this.finish();
    }

    @OnClick(R2.id.invoice_text)
    void actionInvoice() {
        presenter.processInvoice(this, orderData);
    }

    @OnClick(R2.id.shop_name)
    void actionToShop() {
        presenter.processToShop(this, orderData.getOrderShop());
    }

    @OnClick(R2.id.complain_but)
    void actionShowComplain() {
        presenter.processShowComplain(this, orderData.getOrderButton(), orderData.getOrderShop());
    }

    @OnClick(R2.id.ask_seller)
    void actionAskSeller() {
        presenter.processAskSeller(this, orderData);
    }

    @OnClick(R2.id.reject_btn)
    void actionOpenDispute() {
        presenter.processOpenDispute(this, orderData, 0);
    }

    @OnClick(R2.id.receive_btn)
    void actionConfirmDeliver() {
        presenter.processFinish(this, orderData);
        eventReceivedShipping();
    }

    public static void eventReceivedShipping () {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.RECEIVED,
                AppEventTracking.Category.RECEIVED,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.CONFIRMATION);
    }

    @OnClick(R2.id.btn_do_complain)
    void actionComplain() {
        presenter.processComplain(this, orderData);
    }

    @OnClick(R2.id.track_btn)
    void actionTracking() {
        presenter.processTrackOrder(this, orderData);
        eventTrackOrder();
    }

    public static void eventTrackOrder() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.STATUS,
                AppEventTracking.Category.STATUS,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.TRACK);
    }

    @OnClick(R2.id.btn_request_cancel_order)
    void actionRequestCancelOrder() {
        showDialog(getRequestCancelOrderAlertDialog());
    }

    @OnClick(R2.id.see_all)
    void actionSeeAllHistories() {
        presenter.processSeeAllHistories(this, orderData);
    }

    @Override
    public void closeWithResult(int requestCode, Intent data) {
        this.setResult(requestCode, data);
        this.finish();
    }

    @Override
    public void renderSuccessRequestCancelOrder(String message) {
        orderData.getOrderButton().setButtonCancelRequest("0");
        showToastMessage(message);
        btnRequestCancelOrder.setVisibility(View.GONE);
        TxListUIReceiver.sendBroadcastForceRefreshListData(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroyView();
    }

    @Override
    public void actionToProductInfo(ProductPass productPass) {
        Intent intent = getProductIntent(productPass.getProductId());
        navigateToActivity(intent);
    }

    private Intent getProductIntent(String productId){
            return RouteManager.getIntent(this,ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(this, requestCode, resultCode, data);
    }

    @NonNull
    private AlertDialog getRequestCancelOrderAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(
                com.tokopedia.transaction.R.string.label_title_dialog_request_cancel_order)
        );
        builder.setMessage(
                getString(
                        com.tokopedia.transaction.R.string
                                .label_sub_title_dialog_request_cancel_order
                )
        );
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View layout = inflater.inflate(
                com.tokopedia.transaction.R.layout.dialog_alert_request_cancel_order, null
        );

        final TextInputLayout tilEtReason = (TextInputLayout) layout.findViewById(
                com.tokopedia.transaction.R.id.til_et_reason
        );
        final EditText etReason = (EditText) layout.findViewById(
                com.tokopedia.transaction.R.id.et_reason
        );
        builder.setView(layout);
        builder.setPositiveButton(
                getString(com.tokopedia.transaction.R.string.title_btn_alert_dialog_adjust), null
        );
        builder.setNegativeButton(
                com.tokopedia.transaction.R.string.title_btn_alert_dialog_cancel, null
        );
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(
                getRequestCancelOrderAlertDialogPositiveButtonListener(
                        tilEtReason, etReason, alertDialog
                )
        );
        return alertDialog;
    }

    @NonNull
    private DialogInterface.OnShowListener getRequestCancelOrderAlertDialogPositiveButtonListener(
            final TextInputLayout tilEtReason, final EditText etReason,
            final AlertDialog alertDialog) {
        return new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                processRequestCancelOrderWithValidationInput(
                                        etReason, tilEtReason, dialog
                                );
                            }
                        }
                );
            }
        };
    }

    private void processRequestCancelOrderWithValidationInput(EditText etReason,
                                                              TextInputLayout tilEtReason,
                                                              DialogInterface dialog) {
        String reason = etReason.getText().toString();
        if (reason.isEmpty()) {
            tilEtReason.setErrorEnabled(true);
            tilEtReason.setError(getString(
                    com.tokopedia.transaction
                            .R.string
                            .label_error_request_cancel_order_reason_empty
            ));
        } else {
            if (reason.length() < 10) {
                tilEtReason.setErrorEnabled(true);
                tilEtReason.setError(getString(
                        com.tokopedia.transaction
                                .R.string
                                .label_error_request_cancel_order_reason_to_short
                ));
            } else {
                dialog.dismiss();
                presenter.processRequestCancelOrder(
                        TxDetailActivity.this, reason, orderData
                );
            }

        }
    }

    private void alterHistoryComment(String comment, int historyIndex) {
        if (orderData.getOrderDetail().getDetailPreorder() != null
                && orderData.getOrderDetail().getDetailPreorder().getPreorderStatus() == 1
                && comment.equals("0")
                && Integer.parseInt(orderData.getOrderHistory().get(historyIndex)
                .getHistoryOrderStatus()) == 400) {
            orderData.getOrderHistory().get(historyIndex)
                    .setHistoryComments(
                            getString(R.string.label_title_prefix_preorder_period_time_info)
                                    + orderData.getOrderDetail().getDetailPreorder()
                                    .getPreorderProcessTime()
                                    + " "
                                    + orderData.getOrderDetail().getDetailPreorder()
                                    .getPreorderProcessTimeTypeString()
                    );
        }
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
