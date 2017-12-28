package com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.KolFollowingList;

import javax.inject.Inject;

/**
 * Created by yfsx on 28/12/17.
 */

public class KolFollowingListPresenter extends BaseDaggerPresenter<KolFollowingList.View>
        implements KolFollowingList.Presenter {

    @Inject
    public KolFollowingListPresenter() {
    }

    @Override
    public void getKolFollowingList(int userId) {

    }
}
