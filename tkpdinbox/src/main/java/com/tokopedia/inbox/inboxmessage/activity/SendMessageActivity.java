package com.tokopedia.inbox.inboxmessage.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.inbox.inboxmessage.fragment.SendMessageFragment;

/**
 * Created by Nisie on 5/26/16.
 */
public class SendMessageActivity extends BasePresenterActivity {

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_PEOPLE_SEND_MESSAGE;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_message;
    }

    @Override
    protected void initView() {
        Fragment fragment = getFragmentManager().findFragmentByTag(SendMessageFragment.class.getSimpleName());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        if (fragment == null) {
            fragment = SendMessageFragment.createInstance(getIntent().getExtras());
        }

        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
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
