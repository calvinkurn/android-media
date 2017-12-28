package com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolFollowingViewModel;

import java.util.List;

/**
 * Created by yfsx on 28/12/17.
 */

public interface KolFollowingList {

    interface View extends CustomerView {

        void showLoading();

        void hideLoading();

        void onSuccessGetKolFollowingList(List<KolFollowingViewModel> itemList);

        void onErrorGetKolFollowingList(String error);

        void onListItemClicked(KolFollowingViewModel item);
    }

    interface Presenter extends CustomerPresenter<View> {
        void getKolFollowingList(int userId);
    }
}
