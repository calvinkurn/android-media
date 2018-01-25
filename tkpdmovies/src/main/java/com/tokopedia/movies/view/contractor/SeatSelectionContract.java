package com.tokopedia.movies.view.contractor;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.movies.view.customview.SeatLayoutInfo;
import com.tokopedia.movies.view.utils.ImageTextViewHolder;
import com.tokopedia.movies.view.viewmodel.EventsDetailsViewModel;
import com.tokopedia.movies.view.viewmodel.SeatLayoutViewModel;

/**
 * Created by naveengoyal on 1/16/18.
 */

public class SeatSelectionContract {

    public interface SeatSelectionView extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void renderSeatSelection(SeatLayoutInfo seatLayoutInfo, SeatLayoutViewModel viewModel);

        RequestParams getParams();

        void showPayButton(int ticketQuantity, int price);
        void hidePayButton();

        void showProgressBar();

        void hideProgressBar();

        void setTicketPrice(int numOfTickets);

    }

    public interface Presenter extends CustomerPresenter<SeatSelectionContract.SeatSelectionView> {

        void initialize();

        void onDestroy();


        void getSeatSelectionDetails();
        void validateSelection();

    }
}
