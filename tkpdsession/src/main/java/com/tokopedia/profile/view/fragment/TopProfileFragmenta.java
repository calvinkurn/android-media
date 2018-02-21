package com.tokopedia.profile.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.session.R;

/**
 * @author by milhamj on 08/02/18.
 */

public class TopProfileFragmenta extends BaseDaggerFragment {

    public static TopProfileFragmenta newInstance() {
        TopProfileFragmenta fragment = new TopProfileFragmenta();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_profile, container, false);

        return view;
    }

    @Override
    protected void initInjector() {
        getChildFragmentManager().findFragmentByTag("HAI");
        //TODO milhamj
    }

    @Override
    protected String getScreenName() {
        //TODO milhamj
        return null;
    }
}
