package com.tokopedia.inbox.inboxchat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.di.component.DaggerAppComponent;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.adapter.DummyAdapter;
import com.tokopedia.inbox.inboxchat.adapter.InboxChatAdapter;
import com.tokopedia.inbox.inboxchat.di.DaggerInboxChatComponent;
import com.tokopedia.inbox.inboxchat.presenter.InboxChatPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by stevenfredian on 9/19/17.
 */

public class DummyFragment extends BaseDaggerFragment {

    @Inject
    InboxChatPresenter presenter;


    private RecyclerView recyclerView;
    DummyAdapter dummyAdapter;
    InboxChatAdapter adapter;
    private ArrayList<Integer> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_dummy, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.chat_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        list = new ArrayList<>();
//        list.add(1);
//        list.add(2);
//        list.add(4);
//        list.add(5);
//        dummyAdapter = new DummyAdapter(list);
        adapter = InboxChatAdapter.createAdapter(getActivity(), presenter);
        presenter.getMessage();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerAppComponent daggerAppComponent = (DaggerAppComponent) DaggerAppComponent.builder()
                .appModule(new AppModule(getContext()))
                .build();
        DaggerInboxChatComponent daggerInboxChatComponent =
                (DaggerInboxChatComponent) DaggerInboxChatComponent.builder()
                        .appComponent(daggerAppComponent).build();
        daggerInboxChatComponent.inject(this);
    }
}
