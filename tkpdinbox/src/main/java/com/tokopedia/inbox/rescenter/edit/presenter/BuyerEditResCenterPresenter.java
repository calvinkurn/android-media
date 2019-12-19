package com.tokopedia.inbox.rescenter.edit.presenter;

import android.content.Context;
import androidx.annotation.NonNull;

import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;

/**
 * Created on 8/24/16.
 */
public interface BuyerEditResCenterPresenter {

    void setOnLaunching(@NonNull Context context);

    void renderView(EditResCenterFormData formData);

    void setOnRadioPackageStatus(boolean received);

    void setOnRadioPackageReceived();

    void setOnRadioPackageNotReceived();

    void setOnCategoryTroubleSelected(EditResCenterFormData.TroubleCategoryData troubleCategoryData);

    void setOnCategoryTroubleNothingSelected();

    void setOnButtonNextClick(@NonNull Context context);

    void unsubscribe();
}
