package com.tokopedia.transaction.purchase.detail.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.adapter.OrderItemAdapter;
import com.tokopedia.transaction.purchase.detail.customview.OrderDetailButtonLayout;
import com.tokopedia.transaction.purchase.detail.di.DaggerOrderDetailComponent;
import com.tokopedia.transaction.purchase.detail.di.OrderDetailComponent;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.presenter.OrderDetailPresenterImpl;

import javax.inject.Inject;

/**
 * Created by kris on 11/2/17. Tokopedia
 */

public class OrderDetailActivity extends TActivity implements OrderDetailView {
    private static final String EXTRA_ORDER_ID = "EXTRA_ORDER_ID";

    @Inject
    OrderDetailPresenterImpl presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.order_detail_page);
        initInjector();
        presenter.setMainViewListener(this);
        presenter.fetchData(this, getIntent().getStringExtra(EXTRA_ORDER_ID));
    }

    private void initInjector() {
        OrderDetailComponent component = DaggerOrderDetailComponent
                .builder()
                .appComponent(getApplicationComponent())
                .build();
        component.inject(this);
    }

    public static Intent createInstance(Context context, String orderId) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(EXTRA_ORDER_ID, orderId);
        return intent;

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
        statusLayout.setOnClickListener(onStatusLayoutClickedListener());
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
        itemListRecycleView.setAdapter(new OrderItemAdapter(data.getItemList()));
        itemListRecycleView.setNestedScrollingEnabled(false);
    }

    private void setInvoiceView(OrderDetailData data) {
        //TODO redirect once invoice page ready
        //ViewGroup invoiceLayout = (ViewGroup) findViewById(R.id.invoice_layout);
        TextView invoiceNumber = (TextView) findViewById(R.id.invoice_number);
        invoiceNumber.setText(data.getInvoiceNumber());
    }

    private void setDescriptionView(OrderDetailData data) {
        TextView descriptionDate = (TextView) findViewById(R.id.description_date);
        TextView responseTime = (TextView) findViewById(R.id.description_response_time);
        TextView descriptionBuyerName = (TextView) findViewById(R.id.description_buyer_name);
        TextView descriptionCourierName = (TextView) findViewById(R.id.description_courier_name);
        TextView descriptionShippingAddess = (TextView)
                findViewById(R.id.description_shipping_address);
        TextView descriptionPartialOrderStatus = (TextView)
                findViewById(R.id.description_partial_order_status);
        descriptionDate.setText(data.getPurchaseDate());
        responseTime.setText(data.getResponseTimeLimit());
        descriptionBuyerName.setText(data.getDescriptionBuyerName());
        descriptionCourierName.setText(data.getCourierName());
        descriptionShippingAddess.setText(data.getShippingAddress());
        descriptionPartialOrderStatus.setText(data.getPartialOrderStatus());
    }

    private View.OnClickListener onStatusLayoutClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, OrderHistoryActivity.class);
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
}
