package com.tokopedia.tkpd.tkpdreputation.inbox.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;

import static com.tokopedia.core.inboxreputation.InboxReputationConstant.PARAM_NAV;

/**
 * @author by nisie on 8/11/17.
 */

public class InboxReputationFragment extends BaseDaggerFragment {

    RecyclerView mainList;
    SwipeToRefresh swipeToRefresh;

    public static InboxReputationFragment createInstance(String navigation) {
        InboxReputationFragment fragment = new InboxReputationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_NAV, navigation);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_INBOX_REPUTATION;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);

//        DaggerReputationComponent reputationComponent =
//                (DaggerReputationComponent) DaggerReputationComponent
//                        .builder()
//                        .appComponent(appComponent)
//                        .build();
//
//        reputationComponent.inject(this);
    }
}
