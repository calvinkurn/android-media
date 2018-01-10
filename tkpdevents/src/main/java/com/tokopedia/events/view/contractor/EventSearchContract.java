package com.tokopedia.events.view.contractor;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.events.view.viewmodel.CategoryViewModel;
import com.tokopedia.events.view.viewmodel.PackageViewModel;

import java.util.List;

/**
 * Created by pranaymohapatra on 10/01/18.
 */

public class EventSearchContract {
    public interface EventSearchView extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void renderFromSearchResults(List<CategoryViewModel> categoryViewModels);

        void showProgressBar();

        void hideProgressBar();

        RequestParams getParams();

        android.view.View getRootView();
    }

    public interface EventSearchPresenter extends CustomerPresenter<EventSearchView>{

        void getEventsListBySearch(String searchText);

        void initialize();

        void onDestroy();

        void searchTextChanged(String searchText);

        void searchSubmitted(String searchText);
    }
}
