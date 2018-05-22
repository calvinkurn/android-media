package com.tokopedia.transaction.orders.orderlist.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.transaction.orders.orderlist.data.ActionButton;
import com.tokopedia.transaction.orders.orderlist.data.Color;
import com.tokopedia.transaction.orders.orderlist.data.DotMenuList;
import com.tokopedia.transaction.orders.orderlist.data.MetaData;
import com.tokopedia.transaction.orders.orderlist.data.Order;
import com.tokopedia.transaction.orders.orderlist.data.Popup;

import java.util.List;

/**
 * Created by baghira on 07/05/18.
 */

public class ListAdapterContract {
    public interface View extends CustomerView {
        void setButtonData(int leftVisibility, int rightVisibility, String leftText, String rightText, String leftButtonUri, String rightButtonUri, Popup leftPopup, Popup rightPopup, Color leftButtonColor, Color rightButtonColor);

        void setDotMenuVisibility(int visibility);

        void setCategoryAndTitle(String categoryName, String title);

        void setTotal(String totalLabel, String totalValue, String textColor);

        void setDate(String date);

        void setInvoice(String invoice);

        void setConditionalInfo(int successCondInfoVisiblity, String successConditionalText, Color color);

        void setFailStatusBgColor(boolean statusFail);

        void setStatus(String statusText);

        void setMetaDataToCustomView(MetaData metaData);

        void setPaymentAvatar(String imgUrl);

    }
    public interface Presenter extends CustomerPresenter<View> {
        void setActionButtonData(List<ActionButton> actionButtons);

        void setDotMenuClick(List<DotMenuList> dotMenuLists);

        void setDotMenuVisibility(List<DotMenuList> dotMenuLists);

        void setViewData(Order order);


    }
}
