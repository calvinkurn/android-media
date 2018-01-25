package com.tokopedia.events.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.events.domain.model.searchdomainmodel.ValuesItemDomain;
import com.tokopedia.events.view.adapter.FiltersAdapter;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;

import java.util.List;

/**
 * Created by pranaymohapatra on 10/01/18.
 */

public class EventSearchContract {
    public interface EventSearchView extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void renderFromSearchResults(List<CategoryItemsViewModel> categoryItemsViewModels);

        void showProgressBar();

        void hideProgressBar();

        RequestParams getParams();

        android.view.View getRootView();

        FragmentManager getFragmentManagerInstance();
    }

    public interface EventSearchPresenter extends CustomerPresenter<EventSearchView>{

        void getEventsListBySearch(String searchText);

        void initialize();

        void onDestroy();

        void searchTextChanged(String searchText);

        void searchSubmitted(String searchText);

        boolean onOptionMenuClick(int id);

        void onClickFilterItem(ValuesItemDomain filterItem, FiltersAdapter.FilterViewHolder viewHolder);
    }
}
