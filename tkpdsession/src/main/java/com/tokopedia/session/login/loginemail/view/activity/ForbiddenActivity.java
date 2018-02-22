package com.tokopedia.session.login.loginemail.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.session.login.loginemail.view.fragment.ForbiddenFragment;

/**
 * Created by meyta on 2/22/18.
 */

public class ForbiddenActivity extends BaseSimpleActivity {

    public static void createInstance(Context context) {
        Intent intent = new Intent(context, ForbiddenActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected Fragment getNewFragment() {
        return ForbiddenFragment.createInstance();
    }
}
