package com.tokopedia.profile.view.activity;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.activity.BaseEmptyActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.session.R;

/**
 * @author by milhamj on 08/02/18.
 */

public class TopProfileActivity extends BaseEmptyActivity implements HasComponent {

    public static Intent newInstance(Context context) {
        return new Intent(context, TopProfileActivity.class);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_top_profile;
    }

    @Override
    public Object getComponent() {
        //TODO milhamj
        return null;
    }
}
