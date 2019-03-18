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
        void setDotMenuVisibility(int visibility);

        void setCategoryAndTitle(String categoryName, String title);

        void setItemCount(int itemCount);

        void setTotal(String totalLabel, String totalValue, String textColor);

        void setDate(String date);

        void setInvoice(String invoice);

        void setConditionalInfo(int successCondInfoVisiblity, String successConditionalText, Color color);

        void setFailStatusBgColor(String statusColor);

        void setStatus(String statusText);

        void setMetaDataToCustomView(MetaData metaData);

        void setPaymentAvatar(String imgUrl);

        void setActionButtonData(ActionButton leftActionButton, ActionButton rightActionButton, int leftVisibility, int rightVisibility);
    }

    public interface Presenter extends CustomerPresenter<View> {
        void setActionButtonData(List<ActionButton> actionButtons);

        void setDotMenuClick(List<DotMenuList> dotMenuLists);

        void setDotMenuVisibility(List<DotMenuList> dotMenuLists);

        void setViewData(Order order);


    }
}
