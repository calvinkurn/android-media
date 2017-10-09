package com.tokopedia.inbox.rescenter.detailv2.view.fragment;

import android.os.Bundle;
import android.view.View;

import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragmentV4;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.DetailResChatActivity;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;

/**
 * Created by yoasfs on 10/6/17.
 */

public class DetailResChatFragment
        extends BaseDaggerFragmentV4
        implements DetailResChatFragmentListener.View{

    private String resolutionId;

    public static DetailResChatFragment newInstance(String resolutionId) {
        DetailResChatFragment fragment = new DetailResChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DetailResChatActivity.RESOLUTION_ID, resolutionId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void populateView() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        resolutionId = arguments.getString(DetailResChatActivity.RESOLUTION_ID);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_detail_res_chat;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {

    }
}
