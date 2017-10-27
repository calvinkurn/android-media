package com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment;

import android.os.Bundle;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;

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


}
