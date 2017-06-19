package com.tokopedia.seller.product.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.data.mapper.ProductDraftMapper;
import com.tokopedia.seller.product.data.source.db.ProductDraftDataManager;
import com.tokopedia.seller.product.data.source.db.model.ProductDraftDataBase;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.view.fragment.ProductDraftListFragment;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsBaseSimpleActivity;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by User on 6/19/2017.
 */

public class ProductDraftListActivity extends TopAdsBaseSimpleActivity {
    public static final String TAG = ProductDraftListActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return ProductDraftListFragment.newInstance();
    }

    @Override
    protected String getTagFragment() {
        return ProductDraftListFragment.TAG;
    }

    @Override
    protected void onResume() {
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
