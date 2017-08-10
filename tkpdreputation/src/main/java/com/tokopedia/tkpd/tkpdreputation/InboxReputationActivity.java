package com.tokopedia.tkpd.tkpdreputation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.var.TkpdState;

/**
 * @author by nisie on 8/10/17.
 */

public class InboxReputationActivity extends DrawerPresenterActivity {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, InboxReputationActivity.class);
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
        return R.layout.activity_inbox_reputation;
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

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.INBOX_REVIEW;
    }
}
