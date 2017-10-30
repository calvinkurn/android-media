package com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.tkpd.tkpdfeed.R;

/**
 * @author by nisie on 10/27/17.
 */

public class KolCommentFragment extends BaseDaggerFragment {

    public static KolCommentFragment createInstance(Bundle bundle) {
        KolCommentFragment fragment = new KolCommentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_KOL_COMMENTS;
    }

    @Override
    protected void initInjector() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_kol_comment, container, false);

        return parentView;

    }

}
