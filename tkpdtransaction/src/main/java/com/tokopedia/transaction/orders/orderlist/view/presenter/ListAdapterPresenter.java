package com.tokopedia.transaction.orders.orderlist.view.presenter;

import com.tokopedia.transaction.orders.orderlist.data.ActionButton;
import com.tokopedia.transaction.orders.orderlist.data.DotMenuList;
import com.tokopedia.transaction.orders.orderlist.data.Order;

import java.util.List;

/**
 * Created by baghira on 22/03/18.
 */

public interface ListAdapterPresenter {
    void setActionButtonData(List<ActionButton> actionButtons);

    void setDotMenuClick(List<DotMenuList> dotMenuLists);

    void setDotMenuVisibility(List<DotMenuList> dotMenuLists);

    void setViewData(Order order);

}
