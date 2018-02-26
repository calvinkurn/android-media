package com.tokopedia.tkpdstream.channel.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.channel.data.analytics.ChannelAnalytics;
import com.tokopedia.tkpdstream.channel.di.DaggerChannelComponent;
import com.tokopedia.tkpdstream.channel.view.activity.ChannelActivity;
import com.tokopedia.tkpdstream.channel.view.adapter.typefactory.ChannelTypeFactory;
import com.tokopedia.tkpdstream.channel.view.listener.ChannelContract;
import com.tokopedia.tkpdstream.channel.view.model.ChannelListViewModel;
import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;
import com.tokopedia.tkpdstream.channel.view.presenter.ChannelPresenter;
import com.tokopedia.tkpdstream.chatroom.view.activity.GroupChatActivity;
import com.tokopedia.tkpdstream.common.design.CloseableBottomSheetDialog;
import com.tokopedia.tkpdstream.common.di.component.DaggerStreamComponent;
import com.tokopedia.tkpdstream.common.di.component.StreamComponent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 2/1/18.
 */


public class ChannelFragment extends BaseListFragment<ChannelViewModel, ChannelTypeFactory> implements ChannelContract.View {


    private static final int REQUEST_OPEN_GROUPCHAT = 111;

    @Inject
    ChannelPresenter presenter;

    private CloseableBottomSheetDialog channelInfoDialog;

    public static Fragment createInstance(Bundle bundle) {
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
        channelInfoDialog = CloseableBottomSheetDialog.createInstance(getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void loadInitialData() {
//        if(getAdapter().getItemCount() == 0){
//            showLoadingPrevious();
//        }
//        presenter.getChannelListFirstTime();

        List<ChannelViewModel> list = new ArrayList<>();
        String dummyImage = "http://www.behindthevoiceactors.com/_img/games/banner_11.jpg";
        String dummyProfile = "https://orig00.deviantart.net/80ce/f/2007/349/d/f/__kingdom_hearts___coded___by_mazjojo.jpg";
        for (int i = 0; i < 10; i++) {
            ChannelViewModel channelViewModel = new ChannelViewModel("id" + i, "name" + i, dummyImage, dummyProfile, "title" + i, "subtitle" + i, i);
            list.add(channelViewModel);
        }

        onSuccessGetChannelFirstTime(new ChannelListViewModel(list));
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
//        getAdapter().clearAllElements();
//        getAdapter().addElement(channelListViewModel.getChannelViewModelList());
        renderList(channelListViewModel.getChannelViewModelList(), true);
    }

    @Override
    public void onSuccessGetChannel(ChannelListViewModel channelListViewModel) {
//        getAdapter().addElement(channelListViewModel.getChannelViewModelList());
//        getAdapter().clearAllNonDataElement();
//        getAdapter().addMoreData(channelListViewModel.getChannelViewModelList());
        renderList(channelListViewModel.getChannelViewModelList(), true);
    }

    @Override
    public void onFailedGetChannel(String errorMessage) {

    }


    @Override
    public void onItemClicked(ChannelViewModel channelViewModel) {

        channelInfoDialog.setContentView(createBottomSheetView(channelViewModel));
        channelInfoDialog.show();

    }

    private View createBottomSheetView(final ChannelViewModel channelViewModel) {
        View view = getLayoutInflater().inflate(R.layout.channel_info_bottom_sheet_dialog, null);
        Button actionButton = view.findViewById(R.id.action_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChannel(channelViewModel);
                channelInfoDialog.dismiss();
            }
        });
        actionButton.setText("Ikutan Vote Yuk!");
        return view;
    }

    private void goToChannel(ChannelViewModel channelViewModel) {
        startActivityForResult(GroupChatActivity.getCallingIntent(getActivity()),
                REQUEST_OPEN_GROUPCHAT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OPEN_GROUPCHAT
                && resultCode == ChannelActivity.RESULT_ERROR_LOGIN
                && data != null
                && data.getExtras() != null) {
            String errorMessage = data.getExtras().getString(ChannelActivity.RESULT_MESSAGE, "");
            if (!TextUtils.isEmpty(errorMessage)) {
                NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
            } else {
                NetworkErrorHelper.showSnackbar(getActivity());
            }
        }
    }

    @Override
    public void onSwipeRefresh() {
        super.onSwipeRefresh();
        hideLoading();
    }
}
