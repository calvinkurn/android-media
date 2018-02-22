package com.tokopedia.session.login.loginemail.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.session.R;

/**
 * Created by meyta on 2/22/18.
 */

public class ForbiddenFragment extends TkpdBaseV4Fragment {

    private String FORBIDDEN_PAGE = "Forbidden Page";

    public static ForbiddenFragment createInstance() {
        ForbiddenFragment fragment = new ForbiddenFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forbidden, container, false);
    }

    @Override
    protected String getScreenName() {
        return FORBIDDEN_PAGE;
    }
}
