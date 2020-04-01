package com.tokopedia.inbox.rescenter.edit.presenter;

import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;

/**
 * Created on 8/24/16.
 */
public interface BuyerEditResCenterPresenter {

    void setOnRadioPackageStatus(boolean received);

    void setOnCategoryTroubleSelected(EditResCenterFormData.TroubleCategoryData troubleCategoryData);

    void setOnCategoryTroubleNothingSelected();

    void unsubscribe();
}
