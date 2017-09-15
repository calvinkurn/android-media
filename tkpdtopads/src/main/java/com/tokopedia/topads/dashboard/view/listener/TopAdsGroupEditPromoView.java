package com.tokopedia.topads.dashboard.view.listener;

/**
 * Created by zulfikarrahman on 3/1/17.
 */

public interface TopAdsGroupEditPromoView extends TopAdsManageGroupPromoView {
    void showErrorSnackBar(String message);

    void onErrorMoveOutProductGroup();

    void onSuccessMoveOutProductGroup();

    void onSuccessMoveToNewProductGroup();

    void onErrorMoveToNewProductGroup();

    void onSuccessMoveToExistProductGroup();

    void onErrorMoveToExistProductGroup();
}
