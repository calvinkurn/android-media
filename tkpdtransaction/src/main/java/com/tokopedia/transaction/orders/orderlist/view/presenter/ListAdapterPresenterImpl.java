package com.tokopedia.transaction.orders.orderlist.view.presenter;

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
    private static final int ORDER_CANCELLED = 0;
    private static final int ORDER_REFUNDED = 800;
    private static final int ORDER_FAILED = 901;
    private static final int WAITING_THIRD_PARTY = 103;
    private static final int WAITING_TRANSFER = 107;

    ListAdapterContract.View view;

    @Override
    public void setActionButtonData(List<ActionButton> actionButtons) {
        if (actionButtons.size() == 2) {
            ActionButton leftActionButton = actionButtons.get(0);
            ActionButton rightActionButton = actionButtons.get(1);
            view.setActionButtonData(leftActionButton, rightActionButton, View.VISIBLE, View.VISIBLE);
        } else if (actionButtons.size() == 1) {
            ActionButton actionButton = actionButtons.get(0);
            if (actionButton.buttonType().equals("primary")) {
                view.setActionButtonData(actionButton, null, View.VISIBLE, View.GONE);
            } else {
                view.setActionButtonData(null, actionButton, View.GONE, View.VISIBLE);
            }
        } else {
            view.setActionButtonData(null, null, View.GONE, View.GONE);
        }
    }

    @Override
    public void setDotMenuClick(List<DotMenuList> dotMenuLists) {

    }

    @Override
    public void setDotMenuVisibility(List<DotMenuList> dotMenuLists) {
        if (dotMenuLists != null) {
            view.setDotMenuVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setViewData(Order order) {
        view.setStatus(order.statusStr());
            view.setFailStatusBgColor(order.statusColor());

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
        if (!order.getItemCount().equalsIgnoreCase("0") && !order.getItemCount().equalsIgnoreCase("1")) {
            view.setItemCount(Integer.parseInt(order.getItemCount()) - 1);
        }
        List<MetaData> metaDataList = order.metaData();
        for (MetaData metaData : metaDataList) {
            if ((order.status() == WAITING_THIRD_PARTY) || (order.status() == WAITING_TRANSFER) &&
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
