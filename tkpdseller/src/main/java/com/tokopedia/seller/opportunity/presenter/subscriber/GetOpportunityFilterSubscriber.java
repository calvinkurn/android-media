package com.tokopedia.seller.opportunity.presenter.subscriber;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.CategoryList;
import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.OpportunityCategoryData;
import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.ShippingType;
import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.SortingType;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.data.OpportunityCategoryModel;
import com.tokopedia.seller.opportunity.listener.OpportunityListView;
import com.tokopedia.seller.opportunity.viewmodel.CategoryViewModel;
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
        viewModel.setListCategory(getListCategory(opportunityCategoryData.getCategoryList()));
        viewModel.setListShippingType(getListShippingType(opportunityCategoryData.getShippingType()));
        viewModel.setListSortingType(getListSortingType(opportunityCategoryData.getSortingType()));
        return viewModel;
    }


    private List<SortingTypeViewModel> getListSortingType(
            List<SortingType> sortingTypes) {
        List<SortingTypeViewModel> list = new ArrayList<>();
        for (SortingType sortingType : sortingTypes) {
            list.add(new SortingTypeViewModel(sortingType.getSortingTypeName(),
                    sortingType.getSortingTypeID()));
        }
        return list;
    }

    private List<ShippingTypeViewModel> getListShippingType(
            List<ShippingType> shippingTypes) {
        List<ShippingTypeViewModel> list = new ArrayList<>();
        for (ShippingType shippingType : shippingTypes) {
            list.add(new ShippingTypeViewModel(shippingType.getShippingTypeName(),
                    shippingType.getShippingTypeID()));
        }
        return list;
    }

    private List<CategoryViewModel> getListCategory(
            List<CategoryList> categoryLists) {
        ArrayList<CategoryViewModel> list = new ArrayList<>();

        for (CategoryList categoryList : categoryLists) {
            CategoryViewModel categoryViewModel = new CategoryViewModel();
            categoryViewModel.setCategoryId(Integer.parseInt(categoryList.getId()));
            categoryViewModel.setCategoryName(categoryList.getName());
            categoryViewModel.setParent(categoryList.getParent());
            categoryViewModel.setIsHidden(categoryList.getHidden());
            categoryViewModel.setTreeLevel(categoryList.getTree());
            categoryViewModel.setIdentifier(categoryList.getIdentifier());
            if (categoryList.getChild() != null)
                categoryViewModel.setListChild(getListCategory(categoryList.getChild()));
            else
                categoryViewModel.setListChild(new ArrayList<CategoryViewModel>());

            list.add(categoryViewModel);
        }
        return list;
    }
}
