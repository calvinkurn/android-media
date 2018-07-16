package com.tokopedia.transaction.orders.orderdetails.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.transaction.orders.orderdetails.data.ActionButton;
import com.tokopedia.transaction.orders.orderdetails.data.AdditionalInfo;
import com.tokopedia.transaction.orders.orderdetails.data.ContactUs;
import com.tokopedia.transaction.orders.orderdetails.data.Detail;
import com.tokopedia.transaction.orders.orderdetails.data.Invoice;
import com.tokopedia.transaction.orders.orderdetails.data.Items;
import com.tokopedia.transaction.orders.orderdetails.data.OrderToken;
import com.tokopedia.transaction.orders.orderdetails.data.PayMethod;
import com.tokopedia.transaction.orders.orderdetails.data.Pricing;
import com.tokopedia.transaction.orders.orderdetails.data.Status;
import com.tokopedia.transaction.orders.orderdetails.data.Title;
import com.tokopedia.transaction.orders.orderlist.data.ConditionalInfo;
import com.tokopedia.transaction.orders.orderlist.data.PaymentData;

import java.util.List;

/**
 * Created by baghira on 09/05/18.
 */

public interface OrderListDetailContract {

    public interface View extends CustomerView{
        void setStatus(Status status);

        void setConditionalInfo(ConditionalInfo conditionalInfo);

        void setTitle(Title title);

        void setInvoice(Invoice invoice);

        void setOrderToken(OrderToken orderToken);

        void setDetail(Detail detail);

        void setAdditionalInfo(AdditionalInfo additionalInfo);

        void setPricing(Pricing pricing);

        void setPaymentData(PaymentData paymentData);

        void setContactUs(ContactUs contactUs);

        void setTopActionButton(ActionButton actionButton);

        void setBottomActionButton(ActionButton actionButton);

        void setMainViewVisible(int visible);

        void setAdditionInfoVisibility(int visible);

        void setActionButtonsVisibility(int topBtnVisibility, int bottomBtnVisibility);

        void setItems(List<Items> items);

        Context getAppContext();

        void setPayMethodInfo(PayMethod payMethod);

    }

    public interface Presenter extends CustomerPresenter<View>{
        void setOrderDetailsContent(String orderId, String orderCategory, String fromPayment);

       void setActionButton(List<ActionButton> actionButtons, ActionInterface view, int position);

        List<ActionButton> getActionList();
    }

    interface ActionInterface {
        void setActionButton(int position, List<ActionButton> actionButtons);

    }
}
