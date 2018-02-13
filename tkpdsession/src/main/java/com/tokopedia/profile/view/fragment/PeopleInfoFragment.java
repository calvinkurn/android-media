package com.tokopedia.profile.view.fragment;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;

/**
 * @author by milhamj on 08/02/18.
 */

public class PeopleInfoFragment extends BaseDaggerFragment {

    public static PeopleInfoFragment newInstance() {
        PeopleInfoFragment fragment = new PeopleInfoFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        //TODO milhamj
    }

    @Override
    protected String getScreenName() {
        //TODO milhamj
        return null;
    }
}
