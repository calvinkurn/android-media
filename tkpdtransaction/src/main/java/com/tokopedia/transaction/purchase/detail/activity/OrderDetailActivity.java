package com.tokopedia.transaction.purchase.detail.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.adapter.ButtonAdapter;
import com.tokopedia.transaction.purchase.detail.adapter.OrderItemAdapter;
import com.tokopedia.transaction.purchase.detail.model.ButtonAttribute;
import com.tokopedia.transaction.purchase.detail.model.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.model.OrderDetailItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 11/2/17. Tokopedia
 */

public class OrderDetailActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.order_detail_page);
        //TODO delete after fetching data from WS
        initView(dummyOrderDetailData());
    }

    public static Intent createInstance(Context context) {
        return new Intent(context, OrderDetailActivity.class);
    }

    private void initView(OrderDetailData data) {
        setMainScrollProperty();
        setStatusView(data);
        setItemListView(data);
        setInvoiceView(data);
        setDescriptionView(data);
        setPriceView(data);
        setButtonView(data);

    }

    private void setMainScrollProperty() {
        NestedScrollView mainScrollView = (NestedScrollView) findViewById(R.id.main_scroll_view);
        mainScrollView.setNestedScrollingEnabled(false);
    }

    private void setStatusView(OrderDetailData data) {
        TextView statusTextView = (TextView) findViewById(R.id.text_view_status);
        statusTextView.setText(data.getOrderStatus());
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
        insurancePrice.setText(data.getAdditionalFee());
        additionalFee.setText(data.getTotalPayment());
        totalPayment.setText(data.getTotalPayment());
    }

    private void setButtonView(OrderDetailData data) {
        RecyclerView buttonListRecyclerView = (RecyclerView) findViewById(R.id.button_list);
        buttonListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        buttonListRecyclerView.setAdapter(new ButtonAdapter(data.getButtonList()));
    }

    private void setItemListView(OrderDetailData data) {
        RecyclerView itemListRecycleView = (RecyclerView) findViewById(R.id.item_list);
        itemListRecycleView.setLayoutManager(new LinearLayoutManager(this));
        itemListRecycleView.setAdapter(new OrderItemAdapter(data.getItemList()));
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


    private OrderDetailData dummyOrderDetailData() {
        OrderDetailData data = new OrderDetailData();
        data.setOrderStatus("Pesanan Sedang diproses");
        data.setPurchaseDate("1 Sep 2017");
        data.setResponseTimeLimit("3 hari lagi");
        data.setBuyerName("Ryan Handy");
        data.setCourierName("Wahana Express");
        data.setShippingAddress("Ryan handy \n 08113400775 \n Wisma 77 Tower 2 \n Jl.Letjen s parman kav.77 \n Slipi, Jakarta Barat 14410");
        data.setInvoiceNumber("INV/20161025/XVI/X/5506957");
        data.setItemList(dummyItemList());
        data.setTotalItemQuantity("3 barang (3,75kg)");
        data.setProductPrice("Rp 75.000");
        data.setDeliveryPrice("Rp 15.000");
        data.setInsurancePrice("Rp 15.500");
        data.setAdditionalFee("Rp 1.000");
        data.setTotalPayment("Rp 2.000.000");
        data.setButtonList(dummyButtonAttributes());
        return data;
    }

    private List<OrderDetailItemData> dummyItemList() {
        List<OrderDetailItemData> list = new ArrayList<>();
        OrderDetailItemData item1 = new OrderDetailItemData();
        item1.setItemName("Earpads Audio Technica ATH M50x");
        item1.setPrice("Rp 75.000");
        item1.setItemQuantity("1 Barang");
        item1.setDescription("Barangnya tolong dikirim segera dan earpadsnya saya pesan yang warna Hitam! yah");
        OrderDetailItemData item2 = new OrderDetailItemData();
        item2.setItemName("KRK Rockit Speaker Monitor 7 inch New 100% lalalalallala");
        item2.setPrice("Rp 2.275.000");
        item2.setItemQuantity("1 Barang");
        item2.setDescription("");
        OrderDetailItemData item3 = new OrderDetailItemData();
        item3.setItemName("Native instrument Traktor Sratch MK II - Timberland Extended Version");
        item3.setPrice("Rp 545.000");
        item3.setItemQuantity("1 Barang");
        item3.setDescription("Barangnya tolong dikirim segera dan earpadsnya saya pesan yang warna Hitam! yah");
        list.add(item1);
        list.add(item2);
        list.add(item3);
        return list;
    }

    private List<ButtonAttribute> dummyButtonAttributes() {
        List<ButtonAttribute> buttonAttributes = new ArrayList<>();
        ButtonAttribute dummyButton = new ButtonAttribute();
        dummyButton.setButtonColorMode(ButtonAttribute.WHITE_COLOR_MODE);
        dummyButton.setButtonText("Tanya Penjual");
        dummyButton.setId(ButtonAttribute.ASK_SELLER_ID);
        buttonAttributes.add(dummyButton);
        return buttonAttributes;
    }

}
