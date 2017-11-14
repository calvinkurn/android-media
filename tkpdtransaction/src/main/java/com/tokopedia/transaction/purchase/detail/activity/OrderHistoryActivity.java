package com.tokopedia.transaction.purchase.detail.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.adapter.OrderHistoryAdapter;
import com.tokopedia.transaction.purchase.detail.customview.OrderHistoryStepperLayout;
import com.tokopedia.transaction.purchase.detail.model.history.viewmodel.OrderHistoryData;
import com.tokopedia.transaction.purchase.detail.model.history.viewmodel.OrderHistoryListData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 11/7/17. Tokopedia
 */

public class OrderHistoryActivity extends TActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.order_history_layout);
        //TODO delete after fetching data from ws
        initView(dummyOrderHistoryData());
    }

    private void initView(OrderHistoryData data) {
        OrderHistoryStepperLayout stepperLayout = (OrderHistoryStepperLayout)
                findViewById(R.id.order_history_stepper_layout);
        stepperLayout.setStepperStatus(data);

        RecyclerView orderHistoryList = (RecyclerView) findViewById(R.id.order_history_list);
        orderHistoryList.setNestedScrollingEnabled(false);
        orderHistoryList.setLayoutManager(new LinearLayoutManager(this));
        orderHistoryList.setAdapter(new OrderHistoryAdapter(
                dummyOrderHistoryData().getOrderListData()));
    }

    private OrderHistoryData dummyOrderHistoryData() {
        OrderHistoryData data = new OrderHistoryData();
        data.setOrderListData(dummyOrderHistoryListData());
        data.setStepperMode(OrderHistoryData.ORDER_PROCESSED);
        data.setStepperStatusTitle("Pesanan Sedang Diproses");
        return data;
    }

    private List<OrderHistoryListData> dummyOrderHistoryListData() {
        List<OrderHistoryListData> dataList = new ArrayList<>();
        OrderHistoryListData data1 = new OrderHistoryListData();
        data1.setColor("#42B549");
        data1.setActionBy("Buyer");
        data1.setOrderHistoryTitle("Transaksi selesai");
        data1.setOrderHistoryDate("Jumat, 13 Okt 2017");
        data1.setOrderHistoryTime("11:18 WIB");
        OrderHistoryListData data2 = new OrderHistoryListData();
        data2.setColor("#E0E0E0");
        data2.setActionBy("Tokopedia");
        data2.setOrderHistoryTitle( "Pesanan telah tiba di tujuan");
        data2.setOrderHistoryDate("Jumat, 13 Okt 2017");
        data2.setOrderHistoryTime("11:18 WIB");
        OrderHistoryListData data3 = new OrderHistoryListData();
        data3.setColor("#E0E0E0");
        data3.setActionBy("Seller");
        data3.setOrderHistoryTitle("Pesanan telah dikirim<br>Pesanan Anda dalam proses pengiriman oleh kurir");
        data3.setOrderHistoryDate("Jumat, 13 Okt 2017");
        data3.setOrderHistoryTime("11:18 WIB");
        OrderHistoryListData data4 = new OrderHistoryListData();
        data4.setColor("#E0E0E0");
        data4.setActionBy("Seller");
        data4.setOrderHistoryTitle("Pemesanan sedang diproses oleh penjual.");
        data4.setOrderHistoryDate("Jumat, 13 Okt 2017");
        data4.setOrderHistoryTime("11:18 WIB");
        OrderHistoryListData data5 = new OrderHistoryListData();
        data5.setColor("#E0E0E0");
        data5.setActionBy("Buyer");
        data5.setOrderHistoryTitle("Verifikasi Konfirmasi Pembayaran<br>Pembayaran telah diterima Tokopedia dan pesanan Anda sudah diteruskan ke penjual");
        data5.setOrderHistoryDate("Jumat, 13 Okt 2017");
        data5.setOrderHistoryTime("11:18 WIB");
        dataList.add(data1);
        dataList.add(data2);
        dataList.add(data3);
        dataList.add(data4);
        dataList.add(data5);
        return dataList;
    }
}
