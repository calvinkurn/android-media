package com.tokopedia.inbox.rescenter.detailv2.view.listener;

import android.app.Fragment;

/**
 * Created by hangnadi on 3/8/17.
 */

public interface DetailViewListener {
    String getResolutionID();

    void setResolutionID(String resolutionID);

    Fragment getDetailResCenterFragment();

    void setDetailResCenterFragment(Fragment detailResCenterFragment);

    void inflateFragment();
}
