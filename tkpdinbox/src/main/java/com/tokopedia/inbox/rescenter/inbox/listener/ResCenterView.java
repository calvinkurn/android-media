package com.tokopedia.inbox.rescenter.inbox.listener;

import android.app.Application;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

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

    Application getApplication();

    TabLayout getTabLayout();
}
