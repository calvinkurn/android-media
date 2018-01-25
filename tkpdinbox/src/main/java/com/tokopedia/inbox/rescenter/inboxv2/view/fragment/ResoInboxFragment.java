package com.tokopedia.inbox.rescenter.inboxv2.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.DetailResChatActivity;
import com.tokopedia.inbox.rescenter.inboxv2.view.activity.ResoInboxActivity;
import com.tokopedia.inbox.rescenter.inboxv2.view.adapter.ResoInboxAdapter;
import com.tokopedia.inbox.rescenter.inboxv2.view.di.DaggerResoInboxComponent;
import com.tokopedia.inbox.rescenter.inboxv2.view.listener.ResoInboxFragmentListener;
import com.tokopedia.inbox.rescenter.inboxv2.view.presenter.ResoInboxFragmentPresenter;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemResultViewModel;

import javax.inject.Inject;

/**
 * Created by yfsx on 24/01/18.
 */

public class ResoInboxFragment extends BaseDaggerFragment implements ResoInboxFragmentListener.View {

    public static final int REQUEST_DETAIL_RESO = 1234;
    private RecyclerView rvInbox, rvQuickFilter;
    private ResoInboxAdapter inboxAdapter;

    @Inject
    ResoInboxFragmentPresenter presenter;

    private boolean isSeller;

    public static Fragment getFragmentInstance(Bundle bundle) {
        Fragment fragment = new ResoInboxFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_reso_inbox, container, false);
        rvInbox = (RecyclerView) view.findViewById(R.id.rv_inbox);
        rvQuickFilter = (RecyclerView) view.findViewById(R.id.rv_quick_filter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        rvInbox.setLayoutManager(new LinearLayoutManager(getActivity()));
        isSeller = getArguments().getBoolean(ResoInboxActivity.PARAM_IS_SELLER);
        initView();
    }

    public void initView() {
        presenter.initPresenterData(getActivity(), isSeller);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerResoInboxComponent daggerCreateResoComponent =
                (DaggerResoInboxComponent) DaggerResoInboxComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerCreateResoComponent.inject(this);
    }

    @Override
    public void onSuccessGetInbox(InboxItemResultViewModel result) {
        inboxAdapter = new ResoInboxAdapter(getActivity(), this, result.getInboxItemViewModels());
        rvInbox.setAdapter(inboxAdapter);
        inboxAdapter.notifyDataSetChanged();

    }

    @Override
    public void onErrorGetInbox(String err) {

    }

    @Override
    public void onSuccessLoadMoreInbox(InboxItemResultViewModel result) {

    }

    @Override
    public void onErrorLoadMoreInbox(String err) {

    }

    @Override
    public void onItemClicked(int resolutionId, String sellerName, String customerName) {
        Intent intent;
        if (isSeller) {
            intent = DetailResChatActivity.newSellerInstance(getActivity(), String.valueOf(resolutionId), customerName);
        } else {
            intent = DetailResChatActivity.newBuyerInstance(getActivity(), String.valueOf(resolutionId), sellerName);
        }
        startActivityForResult(intent, REQUEST_DETAIL_RESO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DETAIL_RESO) {
            if (resultCode == Activity.RESULT_OK) {

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
