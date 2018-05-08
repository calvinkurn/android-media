package com.tokopedia.transaction.orders.orderlist.view.presenter;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.tokopedia.transaction.orders.orderlist.data.ActionButton;
import com.tokopedia.transaction.orders.orderlist.data.ConditionalInfo;
import com.tokopedia.transaction.orders.orderlist.data.DotMenuList;
import com.tokopedia.transaction.orders.orderlist.data.MetaData;
import com.tokopedia.transaction.orders.orderlist.data.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by baghira on 22/03/18.
 */

public class ListAdapterPresenterImpl implements ListAdapterPresenter {
    ListAdapterListenter adapterListenter;

    public ListAdapterPresenterImpl(ListAdapterListenter listAdapterListenter) {
        adapterListenter = listAdapterListenter;
    }

    @Override
    public void setActionButtonData(List<ActionButton> actionButtons) {
        Log.e("sandeep","actionButton size = "+actionButtons.size()+"  actionbutton ="+actionButtons);
        if (actionButtons.size() == 2) {
            ActionButton leftActionButton = actionButtons.get(0);
            ActionButton rightActionButton = actionButtons.get(1);
            adapterListenter.setButtonData(View.VISIBLE, View.VISIBLE, leftActionButton.label(), rightActionButton.label(), leftActionButton.uri(), rightActionButton.uri(), null, null, leftActionButton.color(), rightActionButton.color());
        } else if (actionButtons.size() == 1) {
            ActionButton actionButton = actionButtons.get(0);
            if (actionButton.buttonType().equals("primary")) {
                adapterListenter.setButtonData(View.VISIBLE, View.GONE, actionButton.label(), null, actionButton.uri(), null, null, null, actionButton.color(), null);
            } else {
                adapterListenter.setButtonData(View.GONE, View.VISIBLE, null, actionButton.label(), null, actionButton.uri(), null, null, null, actionButton.color());
            }
        } else{
            adapterListenter.setButtonData(View.GONE, View.GONE, null, null, null, null, null, null, null, null);
        }
    }

    @Override
    public void setDotMenuClick(List<DotMenuList> dotMenuLists) {

    }

    @Override
    public void setDotMenuVisibility(List<DotMenuList> dotMenuLists) {
        if (dotMenuLists != null) {
            adapterListenter.setDotMenuVisibility(dotMenuLists.size() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void setViewData(Order order) {
        adapterListenter.setStatus(order.statusStr());
        if(!order.statusColor().equals("")){
            adapterListenter.setStatusBgColor(Color.parseColor(order.statusColor()));
        }
        if(!order.conditionalInfo().text().equals("")) {
            ConditionalInfo conditionalInfo = order.conditionalInfo();
            if(!order.status().equals("")) {
                int status = Integer.parseInt(order.status());
                adapterListenter.setConditionalInfo(View.VISIBLE, conditionalInfo.text(), conditionalInfo.color());
            }
        } else {
            adapterListenter.setConditionalInfo(View.GONE,null, null);
        }
        adapterListenter.setInvoice(order.invoiceRefNum());
        String date = order.createdAt();
        if(date != null && date.contains("T")){
            date = lastUpdatedDate(order.createdAt());
        }
        adapterListenter.setDate(date);

        adapterListenter.setCategoryAndTitle(order.categoryName(), order.title());
        List <MetaData> metaDataList = order.metaData();
        for(MetaData metaData : metaDataList){
            if((order.status().equals("103") || order.status().equals("107")) &&
            metaData.label().equalsIgnoreCase("Metode Pembayaran")
                    || metaData.label().equalsIgnoreCase("Kode Pembayaran"))
                continue;
            adapterListenter.setMetaDataToCustomView(metaData);
        }
        adapterListenter.setPaymentAvatar(order.paymentData().imageUrl());
        adapterListenter.setTotal(order.paymentData().label(), order.paymentData().value(), order.paymentData().textColor());
    }


    public static String lastUpdatedDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
        String formattedTime = "";
        try {
            Date d = sdf.parse(date);
            formattedTime = output.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedTime;
    }
}
