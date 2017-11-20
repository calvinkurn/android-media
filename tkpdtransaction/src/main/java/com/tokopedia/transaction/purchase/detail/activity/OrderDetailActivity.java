package com.tokopedia.transaction.purchase.detail.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.tracking.activity.TrackingActivity;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.adapter.OrderItemAdapter;
import com.tokopedia.transaction.purchase.detail.customview.OrderDetailButtonLayout;
import com.tokopedia.transaction.purchase.detail.di.DaggerOrderDetailComponent;
import com.tokopedia.transaction.purchase.detail.di.OrderDetailComponent;
import com.tokopedia.transaction.purchase.detail.dialog.ComplaintDialog;
import com.tokopedia.transaction.purchase.detail.dialog.FinishOrderDialog;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.presenter.OrderDetailPresenterImpl;

import javax.inject.Inject;

/**
 * Created by kris on 11/2/17. Tokopedia
 */

public class OrderDetailActivity extends TActivity
        implements OrderDetailView,
        FinishOrderDialog.FinishOrderDialogListener, ComplaintDialog.ComplaintDialogListener{
    private static final String EXTRA_ORDER_ID = "EXTRA_ORDER_ID";

    @Inject
    OrderDetailPresenterImpl presenter;

    public static Intent createInstance(Context context, String orderId) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(EXTRA_ORDER_ID, orderId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.order_detail_page);
        initInjector();
        presenter.setMainViewListener(this);
        presenter.fetchData(this, getExtraOrderId());
    }

    private void initInjector() {
        OrderDetailComponent component = DaggerOrderDetailComponent
                .builder()
                .appComponent(getApplicationComponent())
                .build();
        component.inject(this);
    }

    private void initView(OrderDetailData data) {
        setStatusView(data);
        setItemListView(data);
        setInvoiceView(data);
        setDescriptionView(data);
        setPriceView(data);
        setButtonView(data);

    }

    private void setStatusView(OrderDetailData data) {
        ViewGroup statusLayout = (ViewGroup) findViewById(R.id.order_detail_status_layout);
        TextView statusTextView = (TextView) findViewById(R.id.text_view_status);
        ImageView imageView = (ImageView) findViewById(R.id.order_detail_status_image);
        statusLayout.setOnClickListener(onStatusLayoutClickedListener(data.getOrderId()));
        statusTextView.setText(data.getOrderStatus());
        ImageHandler.LoadImage(imageView, data.getOrderImage());
    }

    private void setPriceView(OrderDetailData data) {
        TextView itemAmount = (TextView) findViewById(R.id.item_amount);
        TextView productPrice = (TextView) findViewById(R.id.product_price);
        TextView deliveryPrice = (TextView) findViewById(R.id.delivery_price);
        TextView insurancePrice = (TextView) findViewById(R.id.insurance_price);
        TextView additionalFee = (TextView) findViewById(R.id.additional_fee);
        TextView totalPayment = (TextView) findViewById(R.id.total_payment);
        itemAmount.setText(data.getTotalItemQuantity());
        productPrice.setText(data.getProductPrice());
        deliveryPrice.setText(data.getDeliveryPrice());
        insurancePrice.setText(data.getInsurancePrice());
        additionalFee.setText(data.getAdditionalFee());
        totalPayment.setText(data.getTotalPayment());
    }

    private void setButtonView(OrderDetailData data) {
        OrderDetailButtonLayout buttonLayout = (OrderDetailButtonLayout)
                findViewById(R.id.button_layout);
        buttonLayout.initButton(this, presenter, data);
    }

    private void setItemListView(OrderDetailData data) {
        RecyclerView itemListRecycleView = (RecyclerView) findViewById(R.id.item_list);
        itemListRecycleView.setLayoutManager(new LinearLayoutManager(this));
        itemListRecycleView.setAdapter(new OrderItemAdapter(data.getItemList(), this));
        itemListRecycleView.setNestedScrollingEnabled(false);
    }

    private void setInvoiceView(OrderDetailData data) {
        ViewGroup invoiceLayout = (ViewGroup) findViewById(R.id.invoice_layout);
        TextView invoiceNumber = (TextView) findViewById(R.id.invoice_number);
        invoiceNumber.setText(data.getInvoiceNumber());
        invoiceLayout.setOnClickListener(onInvoiceClickedListener(data));
    }

    private void setDescriptionView(OrderDetailData data) {
        TextView descriptionDate = (TextView) findViewById(R.id.description_date);
        LinearLayout timeLimitLayout = (LinearLayout) findViewById(R.id.time_limit_layout);
        TextView responseTime = (TextView) findViewById(R.id.description_response_time);
        TextView descriptionBuyerName = (TextView) findViewById(R.id.description_buyer_name);
        TextView descriptionCourierName = (TextView) findViewById(R.id.description_courier_name);
        TextView descriptionShippingAddess = (TextView)
                findViewById(R.id.description_shipping_address);
        TextView descriptionPartialOrderStatus = (TextView)
                findViewById(R.id.description_partial_order_status);
        descriptionDate.setText(data.getPurchaseDate());
        descriptionBuyerName.setText(data.getBuyerName());
        descriptionCourierName.setText(data.getCourierName());
        descriptionShippingAddess.setText(data.getShippingAddress());
        descriptionPartialOrderStatus.setText(data.getPartialOrderStatus());
        if(data.getResponseTimeLimit() == null || data.getResponseTimeLimit().isEmpty()) {
            timeLimitLayout.setVisibility(View.GONE);
        } else responseTime.setText(data.getResponseTimeLimit());
    }

    private View.OnClickListener onStatusLayoutClickedListener(final String orderId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = OrderHistoryActivity
                        .createInstance(OrderDetailActivity.this, orderId);
                startActivity(intent);
            }
        };
    }

    @Override
    public void onReceiveDetailData(OrderDetailData data) {
        initView(data);
    }

    @Override
    public void onError(String errorMessage) {
        NetworkErrorHelper.showSnackbar(this, errorMessage);
    }

    @Override
    public void goToProductInfo(ProductPass productPass) {
        Intent intent = ProductDetailRouter
                .createInstanceProductDetailInfoActivity(this, productPass);
        startActivity(intent);
    }

    @Override
    public void trackShipment(String orderId) {
        Intent intent = new Intent(OrderDetailActivity.this, TrackingActivity.class);
        intent.putExtra("OrderID", orderId);
        startActivity(intent);
    }

    @Override
    public void showConfirmDialog(String orderId) {
        FinishOrderDialog dialog = FinishOrderDialog.createDialog(orderId);
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
    }

    @Override
    public void showComplaintDialog(String shopName, String orderId) {
        ComplaintDialog dialog = ComplaintDialog.createDialog(orderId,shopName);
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
    }

    @Override
    public void onAskSeller(OrderDetailData orderData) {
        Intent intent = ((TkpdInboxRouter) MainApplication.getAppContext())
                .getAskSellerIntent(this,
                        orderData.getShopId(),
                        orderData.getShopName(),
                        orderData.getInvoiceNumber(),
                        MethodChecker.fromHtml(
                                getString(R.string.dialog_message_ask_seller)
                                        .replace("XXX",
                                                orderData.getInvoiceUrl())
                        ).toString(),
                        TkpdInboxRouter.TX_ASK_SELLER);
        startActivity(intent);
    }

    @Override
    public void onOrderFinished(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroyed();
    }

    private View.OnClickListener onInvoiceClickedListener(final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.processInvoice(OrderDetailActivity.this, data);
            }
        };
    }

    private String getExtraOrderId() {
        return getIntent().getStringExtra(EXTRA_ORDER_ID);
    }

    @Override
    public void onConfirmFinish(String orderId) {
        presenter.processFinish(this, orderId);
    }

    @Override
    public void onComplaintClicked(String orderId) {

    }
}
