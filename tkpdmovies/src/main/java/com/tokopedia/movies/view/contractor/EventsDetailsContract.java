package com.tokopedia.movies.view.contractor;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.movies.view.utils.ImageTextViewHolder;
import com.tokopedia.movies.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.movies.view.viewmodel.EventsDetailsViewModel;

/**
 * Created by ashwanityagi on 23/11/17.
 */

public class EventsDetailsContract {
    public interface EventDetailsView extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void renderFromHome(CategoryItemsViewModel homedata);

        void renderSeatLayout(String url);

        void renderFromCloud(EventsDetailsViewModel data);

        RequestParams getParams();

        void setHolder(int resID, String label, ImageTextViewHolder holder);

        void showProgressBar();

        void hideProgressBar();

    }

    public interface Presenter extends CustomerPresenter<EventDetailsView> {

        void initialize();

        void onDestroy();


        void getEventDetails();


    }

}
