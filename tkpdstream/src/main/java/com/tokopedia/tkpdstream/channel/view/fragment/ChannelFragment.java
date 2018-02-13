package com.tokopedia.tkpdstream.channel.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.channel.view.adapter.typefactory.ChannelTypeFactory;
import com.tokopedia.tkpdstream.channel.view.listener.ChannelContract;
import com.tokopedia.tkpdstream.channel.view.model.ChannelListViewModel;
import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;
import com.tokopedia.tkpdstream.channel.view.presenter.ChannelPresenter;
import com.tokopedia.tkpdstream.common.analytics.ChannelAnalytics;
import com.tokopedia.tkpdstream.common.di.component.StreamComponent;
import com.tokopedia.tkpdstream.common.di.component.DaggerStreamComponent;
import com.tokopedia.tkpdstream.channel.di.DaggerChannelComponent;

import javax.inject.Inject;

/**
 * @author by nisie on 2/1/18.
 */


public class ChannelFragment extends BaseListFragment<ChannelViewModel, ChannelTypeFactory> implements ChannelContract.View {


    @Inject
    ChannelPresenter presenter;

    public static Fragment createInstance() {
        return new ChannelFragment();
    }

    @Override
    protected String getScreenName() {
        return ChannelAnalytics.Screen.CHANNEL_LIST;
    }

    @Override
    protected void initInjector() {
        StreamComponent streamComponent = DaggerStreamComponent.builder().baseAppComponent(
                ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent()).build();

        DaggerChannelComponent.builder()
                .streamComponent(streamComponent)
                .build().inject(this);


        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_list, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void loadInitialData() {
        if(getAdapter().getItemCount() == 0){
            showLoading();
        }
        presenter.getChannelListFirstTime();
    }

    @Override
    public void loadData(int page) {
        presenter.getChannelList();
    }

    @Override
    protected ChannelTypeFactory getAdapterTypeFactory() {
        return new ChannelTypeFactory();
    }

    @NonNull
    @Override
    protected BaseListAdapter<ChannelViewModel, ChannelTypeFactory> createAdapterInstance() {
        BaseListAdapter<ChannelViewModel, ChannelTypeFactory> adapter = super.createAdapterInstance();
        ErrorNetworkModel errorNetworkModel = adapter.getErrorNetworkModel();
        errorNetworkModel.setIconDrawableRes(R.drawable.ic_empty_state);
        errorNetworkModel.setOnRetryListener(this);
        adapter.setErrorNetworkModel(errorNetworkModel);
        return adapter;
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onFailedGetChannelFirstTime(String errorMessage) {
        NetworkErrorHelper.showEmptyState(getContext(), getView(), errorMessage,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.getChannelListFirstTime();
                    }
                });
    }

    @Override
    public void onSuccessGetChannelFirstTime(ChannelListViewModel channelListViewModel) {
        getAdapter().clearAllElements();
        getAdapter().addElement(channelListViewModel.getChannelViewModelList());
    }


    @Override
    public void onItemClicked(ChannelViewModel channelViewModel) {

    }
}
