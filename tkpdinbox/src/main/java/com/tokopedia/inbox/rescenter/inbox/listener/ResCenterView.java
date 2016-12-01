package com.tokopedia.inbox.rescenter.inbox.listener;

import android.os.Bundle;

/**
 * Created on 3/23/16.
 */
public interface ResCenterView {

    void initFragmentList();

    void initFragmentAdapter();

    void setAdapter();

    void setTabLayout();

    void setOffScreenPageLimit();

    Bundle getBundleArguments();

    void setTabPosition(int i);
}
