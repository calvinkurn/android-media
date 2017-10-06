package com.tokopedia.inbox.rescenter.detailchat.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailchat.view.listener.DetailResChatActivityListener;
import com.tokopedia.inbox.rescenter.detailchat.view.presenter.DetailResChatActivityPresenter;

/**
 * Created by yoasfs on 10/6/17.
 */

public class DetailResChatActivity
        extends BasePresenterActivity<DetailResChatActivityListener.Presenter>
        implements DetailResChatActivityListener.View {

    public static final String RESOLUTION_ID = "resolution_id";

    private String resolutionId;

    public static Intent newInstance(Context context, String resolutionId) {
        Intent intent = new Intent(context, DetailResChatActivity.class);
        intent.putExtra(RESOLUTION_ID, resolutionId);
        return intent;
    }

    @Override
    public void inflateFragment(Fragment fragment, String TAG) {
        if (getSupportFragmentManager().findFragmentByTag(TAG) != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(com.tokopedia.core.R.id.container,
                            getSupportFragmentManager().findFragmentByTag(TAG))
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(com.tokopedia.core.R.id.container, fragment, TAG)
                    .commit();
        }
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        resolutionId = extras.getString(RESOLUTION_ID);
    }

    @Override
    protected void initialPresenter() {
        presenter = new DetailResChatActivityPresenter(resolutionId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_res_chat;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }
}
