package com.tokopedia.seller.instoped;

import com.tokopedia.seller.instoped.fragment.InstagramMediaFragment;

/**
 * Created by sebastianuskh on 1/3/17.
 */
public interface InstagramActivityListener {
    void triggerAppBarAnimation(boolean b);

    InstagramMediaFragment.OnGetInstagramMediaListener onGetInstagramMediaListener();
}
