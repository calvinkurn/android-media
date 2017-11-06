package com.tokopedia.transaction.purchase.detail.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.adapter.ButtonAdapter;
import com.tokopedia.transaction.purchase.detail.adapter.OrderItemAdapter;
import com.tokopedia.transaction.purchase.detail.model.OrderDetailData;

/**
 * Created by kris on 11/2/17. Tokopedia
 */

public class OrderDetailActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.order_detail_page);
    }

    private void initView(OrderDetailData data) {
        setItemListView(data);
        setStatusView(data);
        setInvoiceView(data);
        setDescriptionView(data);
        setPriceView(data);
        setButtonView(data);

    }

    private void setPriceView(OrderDetailData data) {
        TextView itemAmount = (TextView) findViewById(R.id.item_amount);
        TextView productPrice = (TextView) findViewById(R.id.product_price);
        TextView deliveryPrice = (TextView) findViewById(R.id.delivery_price);
        TextView insurancePrice = (TextView) findViewById(R.id.insurance_price);
        TextView additionalFee = (TextView) findViewById(R.id.additional_fee);
        TextView totalPayment = (TextView) findViewById(R.id.total_payment);
        itemAmount.setText(data.getItemAmount());
        productPrice.setText(data.getProductPrice());
        deliveryPrice.setText(data.getDeliveryPrice());
        insurancePrice.setText(data.getAdditionalFee());
        additionalFee.setText(data.getTotalPayment());
        totalPayment.setText(data.getTotalPayment());
    }

    private void setButtonView(OrderDetailData data) {
        RecyclerView buttonListRecyclerView = (RecyclerView) findViewById(R.id.button_list);
        buttonListRecyclerView.setAdapter(new ButtonAdapter(data.getButtonList()));
    }

    private void setItemListView(OrderDetailData data) {
        RecyclerView itemListRecycleView = (RecyclerView) findViewById(R.id.item_list);
        itemListRecycleView.setAdapter(new OrderItemAdapter(data.getItemList()));
    }

    private void setStatusView(OrderDetailData data) {
        TextView statusTextView = (TextView) findViewById(R.id.text_view_status);
        statusTextView.setText(data.getOrderStatus());
    }

    private void setInvoiceView(OrderDetailData data) {
        ViewGroup invoiceLayout = (ViewGroup) findViewById(R.id.invoice_layout);
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
        descriptionDate.setText(data.getDescriptionDate());
        responseTime.setText(data.getDescriptionResponseTime());
        descriptionBuyerName.setText(data.getDescriptionBuyerName());
        descriptionCourierName.setText(data.getDescriptionCourierName());
        descriptionShippingAddess.setText(data.getDescriptionShippingAddress());
        descriptionPartialOrderStatus.setText(data.getDescriptionPartialOrderStatus());
    }



}
