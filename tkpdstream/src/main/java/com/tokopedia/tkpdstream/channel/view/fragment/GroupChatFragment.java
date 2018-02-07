package com.tokopedia.tkpdstream.channel.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.channel.di.DaggerChannelComponent;
import com.tokopedia.tkpdstream.channel.view.listener.GroupChatContract;
import com.tokopedia.tkpdstream.channel.view.presenter.GroupChatPresenter;
import com.tokopedia.tkpdstream.common.analytics.ChannelAnalytics;
import com.tokopedia.tkpdstream.common.di.component.DaggerStreamComponent;
import com.tokopedia.tkpdstream.common.di.component.StreamComponent;

import javax.inject.Inject;

/**
 * @author by nisie on 2/6/18.
 */

public class GroupChatFragment extends BaseDaggerFragment implements GroupChatContract.View {

    @Inject
    GroupChatPresenter presenter;

    @Override
    protected String getScreenName() {
        return ChannelAnalytics.Screen.CHAT_ROOM;
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

    public static Fragment createInstance() {
        return new GroupChatFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_chat_room, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getMessagesFirstTime();
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}
