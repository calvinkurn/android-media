package com.tokopedia.events.view.contractor;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.events.view.utils.ImageTextViewHolder;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;

/**
 * Created by pranaymohapatra on 28/11/17.
 */

public class EventBookTicketContract {

    public interface EventBookTicketView extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void renderFromDetails(EventsDetailsViewModel homedata);

        RequestParams getParams();

        void setHolder(int resID, String label, ImageTextViewHolder holder);

        void showPayButton(int ticketQuantity, int price);
        void hidePayButton();

    }

    public interface Presenter extends CustomerPresenter<EventBookTicketContract.EventBookTicketView> {

        void initialize();

        void onDestroy();


        void getTicketDetails();
        void validateSelection();

    }
}
