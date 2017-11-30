package com.tokopedia.seller.seller.info.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.seller.info.data.model.ResponseSellerInfoModel;
import com.tokopedia.seller.seller.info.data.source.SellerInfoApi;
import com.tokopedia.seller.seller.info.di.component.DaggerSellerInfoComponent;
import com.tokopedia.seller.seller.info.view.adapter.SellerInfoAdapter;
import com.tokopedia.seller.seller.info.view.model.SellerInfoModel;
import com.tokopedia.seller.seller.info.view.util.SellerInfoDateUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by normansyahputa on 11/30/17.
 */

public class SellerInfoFragment extends BaseListFragment<BlankPresenter, SellerInfoModel> {

    private SellerInfoDateUtil sellerInfoDateUtil;

    private static final String TAG = "SellerInfoFragment";

    @Inject
    SellerInfoApi sellerInfoApi;

    public static SellerInfoFragment newInstance(){
        return new SellerInfoFragment();
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerSellerInfoComponent.builder()
                .appComponent(getComponent(AppComponent.class))
                .build().inject(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        String[] monthNamesAbrev = getResources().getStringArray(R.array.lib_date_picker_month_entries);
        sellerInfoDateUtil = new SellerInfoDateUtil(monthNamesAbrev);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected BaseListAdapter<SellerInfoModel> getNewAdapter() {
        return new SellerInfoAdapter(sellerInfoDateUtil);
    }

    @Override
    protected void searchForPage(int page) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("page",Integer.toString(page));
        sellerInfoApi.listSellerInfo(requestParams.getParamsAllValueInString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<ResponseSellerInfoModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onNext(Response<ResponseSellerInfoModel> responseSellerInfoModelResponse) {
                        Log.e(TAG, responseSellerInfoModelResponse.toString());

                        adapter.addData(conv(responseSellerInfoModelResponse));
                    }
                });
    }

    private List<SellerInfoModel> conv(Response<ResponseSellerInfoModel> responseSellerInfoModelResponse){
        ResponseSellerInfoModel body = responseSellerInfoModelResponse.body();
        List<SellerInfoModel> res = new ArrayList<>();
        for (ResponseSellerInfoModel.List list : body.getData().getList()) {
            res.add(conv(list));
        }
        return res;
    }

    private SellerInfoModel conv(ResponseSellerInfoModel.List list){
        SellerInfoModel sellerInfoModel = new SellerInfoModel();
        sellerInfoModel.setContent(list.getContent());
        sellerInfoModel.setCreateTimeUnix(list.getCreateTimeUnix());
        sellerInfoModel.setTitle(list.getTitle());
        sellerInfoModel.setInfoThumbnailUrl(list.getInfoThumbnailUrl());
        return sellerInfoModel;
    }

    @Override
    public void onItemClicked(SellerInfoModel sellerInfoModel) {

    }
}
