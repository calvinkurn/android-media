package com.tokopedia.seller.product.view.fragment;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.view.model.etalase.MyEtalaseViewModel;

import java.util.List;

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

    void renderEtalaseList(List<MyEtalaseViewModel> etalases);

    void addNewEtalase(String newEtalaseName);

    void showLoadingDialog();

    void dismissLoadingDialog();

    void showError(String localizedMessage);

    void showRetryAddNewEtalase(String newEtalaseName);
}
