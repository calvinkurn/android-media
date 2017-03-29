package com.tokopedia.inbox.rescenter.discussion.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.discussion.adapter.ResCenterDiscussionAdapter;
import com.tokopedia.inbox.rescenter.discussion.listener.ResCenterDiscussionView;
import com.tokopedia.inbox.rescenter.discussion.presenter.ResCenterDiscussionPresenter;
import com.tokopedia.inbox.rescenter.discussion.presenter.ResCenterDiscussionPresenterImpl;
import com.tokopedia.inbox.rescenter.discussion.viewmodel.ResCenterDiscussionItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisie on 3/29/17.
 */

public class ResCenterDiscussionFragment extends BasePresenterFragment<ResCenterDiscussionPresenter>
        implements ResCenterDiscussionView {

    ResCenterDiscussionAdapter adapter;
    RecyclerView discussionList;

    public static Fragment createInstance() {
        return new ResCenterDiscussionFragment();
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.initData();
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ResCenterDiscussionPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_res_center_discussion;
    }

    @Override
    protected void initView(View view) {
        adapter = ResCenterDiscussionAdapter.createAdapter(getActivity());
        discussionList = (RecyclerView) view.findViewById(R.id.discussion_list);
        discussionList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        discussionList.setAdapter(adapter);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onSuccessGetDiscussion() {
        List<ResCenterDiscussionItemViewModel> list = new ArrayList<>();

        list.add(new ResCenterDiscussionItemViewModel("Message 1", "24 Jul 2016 11:45 WIB", "3045173"));
        list.add(new ResCenterDiscussionItemViewModel("Message 2", "24 Jul 2016 11:45 WIB"));
        list.add(new ResCenterDiscussionItemViewModel("Message 3", "24 Jul 2016 11:45 WIB","3045173"));
        list.add(new ResCenterDiscussionItemViewModel("Message 4", "24 Jul 2016 11:45 WIB"));
        list.add(new ResCenterDiscussionItemViewModel("Message 5", "24 Jul 2016 11:45 WIB"));
        list.add(new ResCenterDiscussionItemViewModel("Message 6", "24 Jul 2016 11:45 WIB"));
        list.add(new ResCenterDiscussionItemViewModel("Message 7", "25 Jul 2016 11:45 WIB"));
        list.add(new ResCenterDiscussionItemViewModel("Message 8", "25 Jul 2016 11:45 WIB"));
        list.add(new ResCenterDiscussionItemViewModel("Message 3", "26 Jul 2016 11:45 WIB","3045173"));





        adapter.setList(list);
    }
}
