package com.tokopedia.seller.opportunity.presenter.subscriber;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.FilterData;
import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.OpportunityCategoryData;
import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.OptionItem;
import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.SearchData;
import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.ShippingType;
import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.SortData;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.data.OpportunityCategoryModel;
import com.tokopedia.seller.opportunity.listener.OpportunityListView;
import com.tokopedia.seller.opportunity.viewmodel.FilterViewModel;
import com.tokopedia.seller.opportunity.viewmodel.OptionViewModel;
import com.tokopedia.seller.opportunity.viewmodel.SearchViewModel;
import com.tokopedia.seller.opportunity.viewmodel.ShippingTypeViewModel;
import com.tokopedia.seller.opportunity.viewmodel.SortingTypeViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityFilterViewModel;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by nisie on 4/5/17.
 */

public class GetOpportunityFilterSubscriber extends Subscriber<OpportunityCategoryModel> {

    private final OpportunityListView viewListener;

    public GetOpportunityFilterSubscriber(OpportunityListView viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        CommonUtils.dumper("NISNIS error Filter" + e.toString());

        if (e instanceof UnknownHostException) {
            viewListener.onErrorGetFilter(viewListener.getString(R.string.msg_no_connection));
        } else if (e instanceof SocketTimeoutException) {
            viewListener.onErrorGetFilter(viewListener.getString(R.string.default_request_error_timeout));
        } else if (e instanceof IOException) {
            viewListener.onErrorGetFilter(viewListener.getString(R.string.default_request_error_internal_server));
        } else if (e.getLocalizedMessage() != null
                && e instanceof ErrorMessageException) {
            viewListener.onErrorGetFilter(e.getLocalizedMessage());
        } else if (e instanceof RuntimeException
                && e.getLocalizedMessage() != null &&
                e.getLocalizedMessage().length() <= 3) {
            new ErrorHandler(new ErrorListener() {
                @Override
                public void onUnknown() {
                    viewListener.onErrorGetFilter(viewListener.getString(R.string.default_request_error_unknown));
                }

                @Override
                public void onTimeout() {
                    viewListener.onErrorGetFilter(viewListener.getString(R.string.default_request_error_timeout));

                }

                @Override
                public void onServerError() {
                    viewListener.onErrorGetFilter(viewListener.getString(R.string.default_request_error_internal_server));

                }

                @Override
                public void onBadRequest() {
                    viewListener.onErrorGetFilter(viewListener.getString(R.string.default_request_error_bad_request));

                }

                @Override
                public void onForbidden() {
                    viewListener.onErrorGetFilter(viewListener.getString(R.string.default_request_error_forbidden_auth));

                }
            }, Integer.parseInt(e.toString()));
        } else {
            viewListener.onErrorGetFilter(viewListener.getString(R.string.default_request_error_unknown));
        }
    }

    @Override
    public void onNext(OpportunityCategoryModel opportunityCategoryModel) {
        viewListener.onSuccessGetFilter(
                mappingToViewModel(opportunityCategoryModel.getOpportunityCategoryData()));

    }

    private OpportunityFilterViewModel mappingToViewModel(OpportunityCategoryData opportunityCategoryData) {
        OpportunityFilterViewModel viewModel = new OpportunityFilterViewModel();
        viewModel.setListFilter(mapplingFilterToViewModel(opportunityCategoryData.getFilter()));
        viewModel.setListSortingType(mappingSortToViewModel(opportunityCategoryData.getSort()));
        return viewModel;
    }

    private ArrayList<SortingTypeViewModel> mappingSortToViewModel(ArrayList<SortData> sort) {
        ArrayList<SortingTypeViewModel> list = new ArrayList<>();

        for (SortData sortData : sort) {
            SortingTypeViewModel sortingTypeViewModel = new SortingTypeViewModel();
            sortingTypeViewModel.setName(sortData.getName());
            sortingTypeViewModel.setValue(sortData.getValue());
            sortingTypeViewModel.setKey(sortData.getKey());
            list.add(sortingTypeViewModel);
        }
        return list;
    }

    private ArrayList<FilterViewModel> mapplingFilterToViewModel(
            ArrayList<FilterData> filterDatas) {
        ArrayList<FilterViewModel> list = new ArrayList<>();

        for (FilterData filterData : filterDatas) {
            FilterViewModel filterViewModel = new FilterViewModel();
            filterViewModel.setName(filterData.getTitle());
            filterViewModel.setListChild(mappingListOption(filterData.getOptionItemList()));
            filterViewModel.setSearchViewModel(mappingSearch(filterData.getSearchData()));
            list.add(filterViewModel);
        }
        return list;
    }

    private SearchViewModel mappingSearch(SearchData searchData) {
        SearchViewModel searchViewModel = new SearchViewModel();
        searchViewModel.setIsSearchable(searchData.getSearchable());
        searchViewModel.setPlaceholder(searchData.getPlaceholder());
        return searchViewModel;
    }

    private ArrayList<OptionViewModel> mappingListOption(List<OptionItem> optionItemList) {
        ArrayList<OptionViewModel> list = new ArrayList<>();
        for (OptionItem optionItem : optionItemList) {
            OptionViewModel viewModel = new OptionViewModel();
            viewModel.setName(optionItem.getName());
            viewModel.setIdentifier(optionItem.getIdentifier());
            viewModel.setIsHidden(optionItem.getHidden());
            viewModel.setKey(optionItem.getKey());
            viewModel.setParent(optionItem.getParent());
            viewModel.setValue(optionItem.getValue());
            viewModel.setTreeLevel(optionItem.getTree());
            viewModel.setListChild(mappingListOption(optionItem.getListChild()));
            list.add(viewModel);
        }
        return list;
    }
}
