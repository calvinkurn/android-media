package com.tokopedia.transaction.purchase.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.customView.OrderStatusView;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.product.model.passdata.ProductPass;
import com.tokopedia.transaction.purchase.adapter.TxProductListAdapter;
import com.tokopedia.transaction.purchase.listener.TxDetailViewListener;
import com.tokopedia.transaction.purchase.model.response.txlist.OrderData;
import com.tokopedia.transaction.purchase.presenter.TxDetailPresenter;
import com.tokopedia.transaction.purchase.presenter.TxDetailPresenterImpl;
import com.tokopedia.transaction.purchase.view.NestedListView;

import java.text.MessageFormat;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author by Angga.Prasetiyo on 28/04/2016.
 */
public class TxDetailActivity extends BasePresenterActivity<TxDetailPresenter> implements
        TxDetailViewListener, TxProductListAdapter.ActionListener {
    private static final String EXTRA_ORDER_DATA = "EXTRA_ORDER_DATA";

    @Bind(R2.id.invoice_text)
    TextView tvInvoiceNumber;
    @Bind(R2.id.shop_name)
    TextView tvShopName;
    @Bind(R2.id.product_list)
    NestedListView lvProduct;
    @Bind(R2.id.shipping_cost)
    TextView tvShippingCost;
    @Bind(R2.id.additional_cost)
    TextView tvAdditionalCost;
    @Bind(R2.id.destination)
    TextView tvDestination;
    @Bind(R2.id.destination_detail)
    TextView tvDestinationDetail;
    @Bind(R2.id.quantity)
    TextView tvQuantity;
    @Bind(R2.id.grand_total)
    TextView tvGrandTotal;
    @Bind(R2.id.transaction)
    TextView tvTransactionDate;
    @Bind(R2.id.see_all)
    TextView btnShowMoreHistory;
    @Bind(R2.id.receive_btn)
    TextView btnReceiveOrder;
    @Bind(R2.id.reject_btn)
    TextView btnRejectOrder;
    @Bind(R2.id.ask_seller)
    TextView btnAskSeller;
    @Bind(R2.id.track_btn)
    TextView btnTrackOrder;
    @Bind(R2.id.complain_but)
    TextView btnComplainOrder;
    @Bind(R2.id.upload_proof)
    TextView btnUploadProof;
    @Bind(R2.id.btn_request_cancel_order)
    TextView btnRequestCancelOrder;
    @Bind(R2.id.sender_name)
    TextView tvSenderName;
    @Bind(R2.id.sender_phone)
    TextView tvSenderPhone;
    @Bind(R2.id.sender_form)
    View holderFormSender;
    @Bind(R2.id.order_status_layout)
    LinearLayout holderOrderStatus;

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
        return R.layout.activity_people_tx_details;
    }

    @Override
    protected void initView() {
        progressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setViewListener() {
        lvProduct.setAdapter(productListAdapter);
    }

    @Override
    protected void initVar() {
        this.productListAdapter = new TxProductListAdapter(this, this);
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
        productListAdapter.addAll(orderData.getOrderProducts());
        productListAdapter.notifyDataSetChanged();
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
        tvShopName.setText(Html.fromHtml(orderData.getOrderShop().getShopName()));
        tvAdditionalCost.setText(orderData.getOrderDetail().getDetailTotalAddFeeIdr());
        tvShippingCost.setText(orderData.getOrderDetail().getDetailShippingPriceIdr());
        tvQuantity.setText(MessageFormat.format("{0} Barang",
                orderData.getOrderDetail().getDetailQuantity()));
        tvGrandTotal.setText(orderData.getOrderDetail().getDetailOpenAmountIdr());
        tvTransactionDate.setText(orderData.getOrderDetail().getDetailOrderDate());
        tvDestination.setText(MessageFormat.format("{0} {1}",
                Html.fromHtml(orderData.getOrderShipment().getShipmentName() + " -"),
                orderData.getOrderShipment().getShipmentProduct()));
        tvDestinationDetail.setText(orderData.getOrderDestination().getDetailDestination()
                .replace("&amp;", "&"));
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

        btnRejectOrder.setVisibility(orderData.getOrderButton().getButtonOpenDispute() != null
                && orderData.getOrderButton().getButtonOpenDispute().equals("1")
                ? View.VISIBLE : View.GONE);

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
        presenter.processShowComplain(this, orderData.getOrderButton());
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
        presenter.processConfirmDeliver(this, orderData);
        UnifyTracking.eventReceivedShipping();
    }

    @OnClick(R2.id.track_btn)
    void actionTracking() {
        presenter.processTrackOrder(this, orderData);
        UnifyTracking.eventTrackOrder();
    }

    @OnClick(R2.id.btn_request_cancel_order)
    void actionRequestCancelOrder() {
        showDialog(getRequestCancelOrderAlertDialog());
    }

    @OnClick(R2.id.upload_proof)
    void actionUploadProof() {
        presenter.processUploadProof(this, orderData);
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
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroyView();
    }

    @Override
    public void actionToProductInfo(ProductPass productPass) {
        navigateToActivity(ProductInfoActivity.createInstance(this, productPass));
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
            final TextInputLayout tilEtReason, final EditText etReason, final AlertDialog alertDialog
    ) {
        return new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String reason = etReason.getText().toString();
                                if (reason.isEmpty()) {
                                    tilEtReason.setErrorEnabled(true);
                                    tilEtReason.setError(getString(
                                            com.tokopedia.transaction.R
                                                    .string.label_error_reason_must_fill
                                    ));
                                } else {
                                    dialog.dismiss();
                                    presenter.processRequestCancelOrder(
                                            TxDetailActivity.this, reason, orderData
                                    );
                                }
                            }
                        }
                );
            }
        };
    }

    private void alterHistoryComment(String comment, int historyIndex) {
        if (orderData.getOrderDetail().getDetailPreorder() != null
                && orderData.getOrderDetail().getDetailPreorder().getPreorderStatus() == 1
                && comment.equals("0")
                && Integer.parseInt(orderData.getOrderHistory().get(historyIndex)
                .getHistoryOrderStatus()) == 400) {
            orderData.getOrderHistory().get(historyIndex)
                    .setHistoryComments(
                            "Lama waktu proses produk : "
                                    + orderData.getOrderDetail().getDetailPreorder()
                                    .getPreorderProcessTime()
                                    + " "
                                    + orderData.getOrderDetail().getDetailPreorder()
                                    .getPreorderProcessTimeTypeString()
                    );
        }
    }


}
