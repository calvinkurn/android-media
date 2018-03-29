package com.tokopedia.events.view.contractor;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.events.view.viewmodel.SeatLayoutViewModel;

import java.util.List;

/**
 * Created by naveengoyal on 1/25/18.
 */

public class SeatSelectionContract {

    public interface SeatSelectionView extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void renderSeatSelection(int price, int maxTickets, SeatLayoutViewModel viewModel);

        RequestParams getParams();

        void showPayButton(int ticketQuantity, int price);

        void hidePayButton();

        void showProgressBar();

        void hideProgressBar();

        void setTicketPrice(int numOfTickets);

        void setSelectedSeatText();

        void initializeSeatLayoutModel(List<String> selectedSeatText, List<String> rowIds, List<String> actualSeat);

        void setEventTitle(String text);

        void setSelectedSeatModel();

        android.view.View getRootView();

    }

    public interface Presenter extends CustomerPresenter<SeatSelectionView> {

        void initialize();

        void onDestroy();


        void validateSelection();

        void onActivityResult(int requestCode);

    }
}
