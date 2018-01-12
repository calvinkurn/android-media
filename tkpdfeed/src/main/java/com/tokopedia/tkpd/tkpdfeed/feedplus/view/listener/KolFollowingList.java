package com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolFollowingResultViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolFollowingViewModel;

import java.util.List;

/**
 * Created by yfsx on 28/12/17.
 */

public interface KolFollowingList {

    interface View extends CustomerView {

        void showLoading();

        void hideLoading();

        void onSuccessGetKolFollowingList(KolFollowingResultViewModel itemList);

        void onSuccessGetKolFollowingListEmptyState();

        void onErrorGetKolFollowingList(String error);

        void onSuccessLoadMoreKolFollowingList(KolFollowingResultViewModel itemList);

        void onErrorLoadMoreKolFollowingList(String error);

        void onListItemClicked(KolFollowingViewModel item);
    }

    interface Presenter extends CustomerPresenter<View> {
        void getKolFollowingList(int userId);

        void getKolLoadMore(int userId, String cursor);
    }
}
