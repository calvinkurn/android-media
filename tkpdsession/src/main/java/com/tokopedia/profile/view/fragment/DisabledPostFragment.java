package com.tokopedia.profile.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.session.R;

/**
 * @author by alvinatin on 08/03/18.
 */

public class DisabledPostFragment extends TkpdBaseV4Fragment{

    private View rootView;

    public static DisabledPostFragment newInstance(){
        DisabledPostFragment fragment = new DisabledPostFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_disabled_post, container, false);
        return rootView;
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
