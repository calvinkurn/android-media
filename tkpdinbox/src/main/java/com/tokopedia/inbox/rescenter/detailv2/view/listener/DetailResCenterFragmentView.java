package com.tokopedia.inbox.rescenter.detailv2.view.listener;

import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.DetailViewModel;

/**
 * Created by hangnadi on 3/8/17.
 */

public interface DetailResCenterFragmentView {
    String getResolutionID();

    void setResolutionID(String resolutionID);

    DetailViewModel getViewData();

    void setViewData(DetailViewModel model);

    void setOnInitResCenterDetailComplete();

    void showLoading(boolean isShow);

    boolean isSeller();

    void setOnActionAcceptProductClick();

    void setOnActionAcceptSolutionClick();

    void setOnActionEditSolutionClick();

    void setOnActionMoreProductClick();

    void setOnActionDiscussClick();

    void setOnActionMoreHistoryClick();

    void setOnActionTrackAwbClick();

    void setOnActionAwbHistoryClick();

    void setOnActionAddressHistoryClick();

    void setOnActionEditAddressClick();
}
