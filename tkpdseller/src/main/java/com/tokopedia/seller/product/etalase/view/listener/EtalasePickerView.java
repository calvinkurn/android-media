package com.tokopedia.seller.product.etalase.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.etalase.view.model.MyEtalaseViewModel;

/**
 * @author sebastianuskh on 4/5/17.
 */

public interface EtalasePickerView extends CustomerView {
    void refreshEtalaseData();

    void showListLoading();

    void dismissListLoading();

    void clearEtalaseList();

    void showListRetry();

    void dismissListRetry();

    void renderEtalaseList(MyEtalaseViewModel etalases);

    void addNewEtalase(String newEtalaseName);

    void showLoadingDialog();

    void dismissLoadingDialog();

    void showError(Throwable exception);

    void showRetryAddNewEtalase(String newEtalaseName);

    void showNextListLoading();
}
