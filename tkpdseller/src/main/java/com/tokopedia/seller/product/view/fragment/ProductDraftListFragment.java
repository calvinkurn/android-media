package com.tokopedia.seller.product.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.data.mapper.ProductDraftMapper;
import com.tokopedia.seller.product.data.source.db.ProductDraftDataManager;
import com.tokopedia.seller.product.data.source.db.model.ProductDraftDataBase;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.view.activity.ProductDraftAddActivity;
import com.tokopedia.seller.product.view.activity.ProductDraftEditActivity;
import com.tokopedia.seller.product.view.adapter.ProductDraftAdapter;
import com.tokopedia.seller.product.view.model.ProductDraftViewModel;
import com.tokopedia.seller.product.view.presenter.ProductDraftListPresenter;
import com.tokopedia.seller.topads.keyword.view.adapter.TopAdsKeywordAdapter;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsBaseListFragment;
import com.tokopedia.seller.topads.keyword.view.model.BaseKeywordParam;
import com.tokopedia.seller.topads.view.adapter.TopAdsBaseListAdapter;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsEmptyAdDataBinder;
import com.tokopedia.seller.topads.view.model.Ad;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by User on 6/19/2017.
 */

public class ProductDraftListFragment extends TopAdsBaseListFragment<ProductDraftListPresenter, ProductDraftViewModel> implements TopAdsEmptyAdDataBinder.Callback {
    public static final String TAG = ProductDraftListFragment.class.getSimpleName();
    public static final int DRAFT_EDIT_REQ_CODE = 701;

    public static ProductDraftListFragment newInstance() {
        return new ProductDraftListFragment();
    }

    @Override
    protected TopAdsBaseListAdapter getNewAdapter() {
        return new ProductDraftAdapter();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_draft_list;
    }

    @Override
    public void onItemClicked(ProductDraftViewModel productDraftViewModel) {
        Intent intent;
        if (productDraftViewModel.isEdit()) {
            intent = ProductDraftEditActivity.createInstance(getActivity(),
                    String.valueOf( productDraftViewModel.getProductId()));
        } else {
            intent = ProductDraftAddActivity.createInstance(getActivity(),
                    String.valueOf(productDraftViewModel.getProductId()));
        }
        startActivityForResult(intent, DRAFT_EDIT_REQ_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == DRAFT_EDIT_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // TODO draft has been submitted, go to manage product and finish this
            }
        }
    }

    @Override
    protected TopAdsEmptyAdDataBinder getEmptyViewDefaultBinder() {
        TopAdsEmptyAdDataBinder emptyGroupAdsDataBinder = new TopAdsEmptyAdDataBinder(adapter) {
            @Override
            protected int getEmptyLayout() {
                return R.layout.listview_top_ads_empty_keyword_list;
            }
        };
        emptyGroupAdsDataBinder.setEmptyTitleText(getString(R.string.top_ads_keyword_your_keyword_empty));
        emptyGroupAdsDataBinder.setEmptyContentText(getString(R.string.top_ads_keyword_please_use));
        emptyGroupAdsDataBinder.setEmptyButtonItemText(getString(R.string.top_ads_keyword_add_keyword));
        emptyGroupAdsDataBinder.setCallback(this);
        return emptyGroupAdsDataBinder;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
    }

    @Override
    protected void searchData() {
        super.searchData();
        final Observable<List<UploadProductInputDomainModel>> productDraftDataBaseList =
                new ProductDraftDataManager().getAllDraft()
                        .flatMap(new Func1<List<ProductDraftDataBase>, Observable<ProductDraftDataBase>>() {
                            @Override
                            public Observable<ProductDraftDataBase> call(List<ProductDraftDataBase> productDraftDataBases) {
                                return Observable.from(productDraftDataBases);
                            }
                        })
                        .map(new Func1<ProductDraftDataBase, UploadProductInputDomainModel>() {
                            @Override
                            public UploadProductInputDomainModel call(ProductDraftDataBase productDraftDataBase) {
                                long id = productDraftDataBase.getId();
                                String data = productDraftDataBase.getData();
                                return Observable.just(data).map(new ProductDraftMapper(id)).toBlocking().first();
                            }
                        }).toList();
        productDraftDataBaseList.subscribe(new Subscriber<List<UploadProductInputDomainModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                onLoadSearchError();
            }

            @Override
            public void onNext(List<UploadProductInputDomainModel> uploadProductInputDomainModels) {
                hideLoading();
                if (uploadProductInputDomainModels == null || uploadProductInputDomainModels.size() == 0 ) {
                    onSearchLoaded(new ArrayList(), 0);
                } else {
                    // map to View Model
                    List< ProductDraftViewModel> viewModelList = new ArrayList<>();
                    for (int i=0, sizei = uploadProductInputDomainModels.size(); i<sizei; i++) {
                        UploadProductInputDomainModel domainModel = uploadProductInputDomainModels.get(i);
                        viewModelList.add(new ProductDraftViewModel(domainModel));
                    }
                    onSearchLoaded(viewModelList, viewModelList.size());
                }

            }
        });
    }

    @Override
    public void onEmptyContentItemTextClicked() {
        // TODO EmptyContentItemText clicked
    }

    @Override
    public void onEmptyButtonClicked() {
        // TODO hendry button empty clicked
    }
}
