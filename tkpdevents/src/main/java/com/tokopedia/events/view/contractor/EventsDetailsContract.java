package com.tokopedia.events.view.contractor;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.events.view.utils.ImageTextViewHolder;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;

import java.util.List;

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

        void renderSeatmap(String url);

        void renderFromCloud(EventsDetailsViewModel data);

        RequestParams getParams();

        void setHolder(int resID, String label, ImageTextViewHolder holder);

        void showProgressBar();

        void hideProgressBar();

        android.view.View getRootView();

    }

    public interface Presenter extends CustomerPresenter<EventDetailsView> {

        void initialize();

        void onDestroy();


        void getEventDetails();


    }

}
