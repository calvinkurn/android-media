package com.tokopedia.events.view.presenter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.events.R;
import com.tokopedia.events.domain.GetSearchEventsListRequestUseCase;
import com.tokopedia.events.domain.model.searchdomainmodel.SearchDomainModel;
import com.tokopedia.events.domain.model.searchdomainmodel.ValuesItemDomain;
import com.tokopedia.events.view.adapter.FiltersAdapter;
import com.tokopedia.events.view.contractor.EventSearchContract;
import com.tokopedia.events.view.fragment.FilterFragment;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by pranaymohapatra on 10/01/18.
 */

public class EventSearchPresenter
        extends BaseDaggerPresenter<EventSearchContract.EventSearchView>
        implements EventSearchContract.EventSearchPresenter {

    public GetSearchEventsListRequestUseCase getSearchEventsListRequestUseCase;
    String FRAGMENT_TAG = "FILTERFRAGMENT";
    SearchDomainModel mSearchData;
    FiltersAdapter.FilterViewHolder filterViewHolder;
    ValuesItemDomain selectedTime;
    String catgoryFilters;
    String timeFilter;

    @Inject
    public EventSearchPresenter(GetSearchEventsListRequestUseCase getSearchEventsListRequestUseCase) {
        this.getSearchEventsListRequestUseCase = getSearchEventsListRequestUseCase;

    }

    @Override
    public void getEventsListBySearch(String searchText) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(getSearchEventsListRequestUseCase.TAG, searchText);
        if (!(catgoryFilters == null) && catgoryFilters.length() == 0)
            requestParams.putString(getSearchEventsListRequestUseCase.CATEGORY, catgoryFilters);
        if (!(timeFilter == null) && timeFilter.length() == 0)
            requestParams.putString(getSearchEventsListRequestUseCase.TIME, timeFilter);

        getSearchEventsListRequestUseCase.execute(requestParams, new Subscriber<SearchDomainModel>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
            }

            @Override
            public void onNext(SearchDomainModel searchDomainModel) {
                mSearchData = searchDomainModel;
                List<CategoryItemsViewModel> categoryItemsViewModels = Utils.getSingletonInstance()
                        .convertIntoCategoryListItemsVeiwModel(searchDomainModel.getEvents());
                getView().renderFromSearchResults(categoryItemsViewModels);
                CommonUtils.dumper("enter onNext");
            }
        });
    }

    @Override
    public void initialize() {
        getEventsListBySearch("EXAMPLESEARCH");
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void searchTextChanged(String searchText) {

    }

    @Override
    public void searchSubmitted(String searchText) {

    }

    @Override
    public boolean onOptionMenuClick(int id) {
        if (id == R.id.action_filter) {
            FragmentManager fragmentManager = getView().getFragmentManagerInstance();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            FilterFragment fragment = FilterFragment.newInstance(100);
            fragment.setData(mSearchData.getFilters(), this);
            transaction.add(R.id.main_content, fragment, FRAGMENT_TAG);
            transaction.addToBackStack(FRAGMENT_TAG);
            transaction.commit();
        } else {
            getView().getActivity().onBackPressed();
            return true;
        }
        return true;
    }

    @Override
    public void onClickFilterItem(ValuesItemDomain filterItem, FiltersAdapter.FilterViewHolder viewHolder) {
        if (!filterItem.isMulti()) {
            if (!filterItem.getIsSelected()) {
                if (selectedTime != null)
                    selectedTime.setIsSelected(false);
                filterItem.setIsSelected(true);
                selectedTime = filterItem;
                timeFilter = selectedTime.getName();
            } else {
                filterItem.setIsSelected(false);
                selectedTime = null;
                timeFilter = "";
            }

        } else {
            if (!filterItem.getIsSelected()) {
                filterItem.setIsSelected(true);
                if (catgoryFilters != null && catgoryFilters.length() == 0) {
                    catgoryFilters.concat(",").concat(filterItem.getName());
                } else {
                    catgoryFilters = filterItem.getName();
                }
            } else {
                filterItem.setIsSelected(false);
                catgoryFilters.replace("," + filterItem.getName(), "");
            }
        }
    }
}
