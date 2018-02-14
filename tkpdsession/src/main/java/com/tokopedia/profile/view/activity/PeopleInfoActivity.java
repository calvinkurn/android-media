package com.tokopedia.profile.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseEmptyActivity;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.profile.view.fragment.PeopleInfoFragment;
import com.tokopedia.session.R;

/**
 * @author by milhamj on 08/02/18.
 */

public class PeopleInfoActivity extends BaseEmptyActivity implements HasComponent {

    public static Intent newInstance(Context context) {
        return new Intent(context, PeopleInfoActivity.class);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_people_info;
    }

    @Override
    public Object getComponent() {
        //TODO milhamj
        return null;
    }
}
