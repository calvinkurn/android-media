package com.tokopedia.profile.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.profile.view.fragment.PeopleInfoFragment;

/**
 * @author by milhamj on 08/02/18.
 */

public class PeopleInfoActivity extends BaseSimpleActivity implements HasComponent {
    @Override
    protected Fragment getNewFragment() {
        return PeopleInfoFragment.newInstance();
    }

    @Override
    public Object getComponent() {
        //TODO milhamj
        return null;
    }
}
