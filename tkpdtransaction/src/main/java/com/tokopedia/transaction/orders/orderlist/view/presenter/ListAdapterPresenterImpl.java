package com.tokopedia.transaction.orders.orderlist.view.presenter;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
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

public class ListAdapterPresenterImpl extends BaseDaggerPresenter<ListAdapterContract.View> implements ListAdapterContract.Presenter {
    ListAdapterContract.View view;

    @Override
    public void setActionButtonData(List<ActionButton> actionButtons) {
        if (actionButtons.size() == 2) {
            ActionButton leftActionButton = actionButtons.get(0);
            ActionButton rightActionButton = actionButtons.get(1);
            view.setButtonData(View.VISIBLE, View.VISIBLE, leftActionButton.label(), rightActionButton.label(), leftActionButton.uri(), rightActionButton.uri(), null, null, leftActionButton.color(), rightActionButton.color());
        } else if (actionButtons.size() == 1) {
            ActionButton actionButton = actionButtons.get(0);
            if (actionButton.buttonType().equals("primary")) {
                view.setButtonData(View.VISIBLE, View.GONE, actionButton.label(), null, actionButton.uri(), null, null, null, actionButton.color(), null);
            } else {
                view.setButtonData(View.GONE, View.VISIBLE, null, actionButton.label(), null, actionButton.uri(), null, null, null, actionButton.color());
            }
        } else {
            view.setButtonData(View.GONE, View.GONE, null, null, null, null, null, null, null, null);
        }
    }

    @Override
    public void setDotMenuClick(List<DotMenuList> dotMenuLists) {

    }

    @Override
    public void setDotMenuVisibility(List<DotMenuList> dotMenuLists) {
        if (dotMenuLists != null) {
            view.setDotMenuVisibility(dotMenuLists.size() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void setViewData(Order order) {
        view.setStatus(order.statusStr());
        if (order.status() == 0 || order.status() == 800 || order.status() == 901) {
            view.setStatusBgColor(0);
        }
        if (!order.statusColor().equals("")) {
            view.setStatusBgColor(Color.parseColor(order.statusColor()));
        }
        if (!order.conditionalInfo().text().equals("")) {
            ConditionalInfo conditionalInfo = order.conditionalInfo();
            view.setConditionalInfo(View.VISIBLE, conditionalInfo.text(), conditionalInfo.color());
        } else {
            view.setConditionalInfo(View.GONE, null, null);
        }
        view.setInvoice(order.invoiceRefNum());
        String date = order.createdAt();
        if (date != null && date.contains("T")) {
            date = lastUpdatedDate(order.createdAt());
        }
        view.setDate(date);

        view.setCategoryAndTitle(order.categoryName(), order.title());
        List<MetaData> metaDataList = order.metaData();
        for (MetaData metaData : metaDataList) {
            if ((order.status() == 103) || (order.status() == 107) &&
                    (metaData.label().equalsIgnoreCase("Metode Pembayaran")
                            || metaData.label().equalsIgnoreCase("Kode Pembayaran")))
                continue;
            view.setMetaDataToCustomView(metaData);
        }
        view.setPaymentAvatar(order.paymentData().imageUrl());
        view.setTotal(order.paymentData().label(), order.paymentData().value(), order.paymentData().textColor());
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

    @Override
    public void attachView(ListAdapterContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }
}
