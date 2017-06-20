package com.tokopedia.seller.product.view.fragment;

import android.os.Bundle;
import android.util.Log;

import com.tokopedia.seller.product.data.mapper.ProductDraftMapper;
import com.tokopedia.seller.product.data.source.db.ProductDraftDataManager;
import com.tokopedia.seller.product.data.source.db.model.ProductDraftDataBase;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.view.presenter.ProductDraftListPresenter;
import com.tokopedia.seller.topads.keyword.view.adapter.TopAdsKeywordAdapter;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsBaseListFragment;
import com.tokopedia.seller.topads.view.adapter.TopAdsBaseListAdapter;
import com.tokopedia.seller.topads.view.model.Ad;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by User on 6/19/2017.
 */

public class ProductDraftListFragment extends TopAdsBaseListFragment<ProductDraftListPresenter, UploadProductInputDomainModel>{
    public static final String TAG = ProductDraftListFragment.class.getSimpleName();

    public static ProductDraftListFragment newInstance() {
        return new ProductDraftListFragment();
    }

    @Override
    protected TopAdsBaseListAdapter getNewAdapter() {
        return new TopAdsKeywordAdapter();
    }

    @Override
    public void onItemClicked(UploadProductInputDomainModel s) {

    }

    @Override
    public void onResume() {
        super.onResume();
        Observable<List<UploadProductInputDomainModel>> productDraftDataBaseList =
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

            }

            @Override
            public void onNext(List<UploadProductInputDomainModel> uploadProductInputDomainModels) {
                Log.d("Test", "test");
            }
        });
    }
}
