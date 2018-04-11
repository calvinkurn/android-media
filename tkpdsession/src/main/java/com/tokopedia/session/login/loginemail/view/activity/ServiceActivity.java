package com.tokopedia.session.login.loginemail.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.session.login.loginemail.view.fragment.ServiceFragment;

/**
 * Created by nakama on 28/02/18.
 */

public class ServiceActivity extends BaseSimpleActivity {

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, ServiceActivity.class);
        intent.putExtra(ServiceActivity.class.getSimpleName(), url);
        context.startActivity(intent);
    }

    @Override
    protected Fragment getNewFragment() {
        String url = getIntent().getStringExtra(ServiceActivity.class.getSimpleName());
        return ServiceFragment.createInstance(url);
    }
}
