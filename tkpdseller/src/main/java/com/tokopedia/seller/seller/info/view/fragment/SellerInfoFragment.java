package com.tokopedia.seller.seller.info.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.base.list.seller.view.adapter.BaseListAdapter;
import com.tokopedia.base.list.seller.view.adapter.BaseRetryDataBinder;
import com.tokopedia.base.list.seller.view.emptydatabinder.EmptyDataBinder;
import com.tokopedia.base.list.seller.view.fragment.BaseListFragment;
import com.tokopedia.base.list.seller.view.old.NoResultDataBinder;
import com.tokopedia.base.list.seller.view.old.RetryDataBinder;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.seller.info.di.component.DaggerSellerInfoComponent;
import com.tokopedia.seller.seller.info.view.SellerInfoTracking;
import com.tokopedia.seller.seller.info.view.SellerInfoView;
import com.tokopedia.seller.seller.info.view.activity.SellerInfoWebViewActivity;
import com.tokopedia.seller.seller.info.view.adapter.SellerInfoAdapter;
import com.tokopedia.seller.seller.info.view.model.SellerInfoModel;
import com.tokopedia.seller.seller.info.view.model.SellerInfoSectionModel;
import com.tokopedia.seller.seller.info.view.presenter.SellerInfoPresenter;
import com.tokopedia.seller.seller.info.view.util.SellerInfoDateUtil;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by normansyahputa on 11/30/17.
 */

public class SellerInfoFragment extends BaseListFragment<BlankPresenter, SellerInfoModel>
    implements SellerInfoView
{

    private SellerInfoDateUtil sellerInfoDateUtil;

    private static final String TAG = "SellerInfoFragment";

    @Inject
    SellerInfoPresenter sellerInfoPresenter;

    private String[] monthNamesAbrev;
    private boolean hasNextPage;

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
        monthNamesAbrev = getResources().getStringArray(R.array.lib_date_picker_month_entries);
        super.onViewCreated(view, savedInstanceState);
        sellerInfoPresenter.attachView(this);
    }

    @Override
    protected BaseListAdapter<SellerInfoModel> getNewAdapter() {
        return new SellerInfoAdapter(monthNamesAbrev);
    }

    @Override
    protected void searchForPage(int page) {
        sellerInfoPresenter.getSellerInfoList(page);
        hasNextPage = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sellerInfoPresenter.detachView();
    }

    @Override
    public void onItemClicked(SellerInfoModel sellerInfoModel) {
        if(TextUtils.isEmpty(sellerInfoModel.getExternalLink()))
            return;

        if(sellerInfoModel instanceof SellerInfoSectionModel)
            return;
        SellerInfoTracking.eventClickItemSellerInfo(sellerInfoModel.getTitle());

        startActivity(SellerInfoWebViewActivity.getCallingIntent(getContext(), sellerInfoModel.getExternalLink()));
    }


    @Override
    public void onSearchLoaded(@NonNull List<SellerInfoModel> list, int totalItem, boolean hasNext) {
        onSearchLoaded(list, totalItem);
        hasNextPage = hasNext  && list != null && !list.isEmpty() && totalItem > 0;
    }

    @Override
    protected boolean hasNextPage() {
        return hasNextPage;
    }

    @Override
    protected void onPullToRefresh() {
        if(adapter != null && adapter instanceof SellerInfoAdapter) {
            ((SellerInfoAdapter)adapter).clearRawAdapter();
        }
        super.onPullToRefresh();
    }

    @Override
    public RetryDataBinder getRetryViewDataBinder(BaseListAdapter adapter) {
        return new BaseRetryDataBinder(adapter, R.drawable.ic_cloud_error);
    }

    @Override
    protected NoResultDataBinder getEmptyViewNoResultBinder() {
        EmptyDataBinder emptyDataBinder = new EmptyDataBinder(adapter, R.drawable.ic_empty_state);
        emptyDataBinder.setEmptyTitleText(getString(R.string.seller_info_empty_title));
        emptyDataBinder.setEmptyContentText(getString(R.string.seller_info_empty_desc));
        return emptyDataBinder;
    }

    @Override
    protected NoResultDataBinder getEmptyViewDefaultBinder() {
        EmptyDataBinder emptyDataBinder = new EmptyDataBinder(adapter, R.drawable.ic_empty_state);
        emptyDataBinder.setEmptyTitleText(getString(R.string.seller_info_empty_title));
        emptyDataBinder.setEmptyContentText(getString(R.string.seller_info_empty_desc));
        return emptyDataBinder;
    }

}
