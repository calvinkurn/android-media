package com.tokopedia.transaction.orders.orderlist.view.presenter;


import com.tokopedia.transaction.orders.orderlist.data.Color;
import com.tokopedia.transaction.orders.orderlist.data.MetaData;
import com.tokopedia.transaction.orders.orderlist.data.Popup;

/**
 * Created by baghira on 22/03/18.
 */

public interface ListAdapterListenter {
    void setButtonData(int leftVisibility, int rightVisibility, String leftText, String rightText, String leftButtonUri, String rightButtonUri, Popup leftPopup, Popup rightPopup, Color leftButtonColor, Color rightButtonColor);

    void setDotMenuVisibility(int visibility);

    void setCategoryAndTitle(String categoryName, String title);

    void setTotal(String totalLabel, String totalValue, String textColor);

    void setDate(String date);

    void setInvoice(String invoice);

    void setConditionalInfo(int successCondInfoVisiblity, String successConditionalText, Color color);

    void setStatusBgColor(int statusColor);

    void setStatus(String statusText);

    void setMetaDataToCustomView(MetaData metaData);

    void setPaymentAvatar(String imgUrl);
}
