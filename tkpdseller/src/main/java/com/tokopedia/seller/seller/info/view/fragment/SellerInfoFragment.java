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
import com.tokopedia.seller.seller.info.domain.interactor.SellerInfoUseCase;
import com.tokopedia.seller.seller.info.view.SellerInfoView;
import com.tokopedia.seller.seller.info.view.adapter.SellerInfoAdapter;
import com.tokopedia.seller.seller.info.view.model.SellerInfoModel;
import com.tokopedia.seller.seller.info.view.presenter.SellerInfoPresenter;
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

public class SellerInfoFragment extends BaseListFragment<BlankPresenter, SellerInfoModel>{

    private SellerInfoDateUtil sellerInfoDateUtil;

    private static final String TAG = "SellerInfoFragment";

    @Inject
    SellerInfoPresenter sellerInfoPresenter;

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
        sellerInfoPresenter.attachView(this);
    }

    @Override
    protected BaseListAdapter<SellerInfoModel> getNewAdapter() {
        return new SellerInfoAdapter(sellerInfoDateUtil);
    }

    @Override
    protected void searchForPage(int page) {
        sellerInfoPresenter.getSellerInfoList(page);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sellerInfoPresenter.detachView();
    }

    @Override
    public void onItemClicked(SellerInfoModel sellerInfoModel) {

    }
}
