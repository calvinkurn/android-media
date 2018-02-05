package com.tokopedia.movies.view.contractor;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.movies.view.utils.ImageTextViewHolder;
import com.tokopedia.movies.view.viewmodel.EventsDetailsViewModel;

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

        void showProgressBar();

        void hideProgressBar();

    }

    public interface Presenter extends CustomerPresenter<EventBookTicketView> {

        void initialize();

        void onDestroy();


        void getTicketDetails();
        void validateSelection();

    }
}
