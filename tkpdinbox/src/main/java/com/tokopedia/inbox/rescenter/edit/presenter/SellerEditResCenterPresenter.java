package com.tokopedia.inbox.rescenter.edit.presenter;

import android.content.Context;
import androidx.annotation.NonNull;

import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;

/**
 * Created on 8/29/16.
 */
public interface SellerEditResCenterPresenter {

    void setOnLaunching(@NonNull Context context);

    void renderView(EditResCenterFormData formData);

    void setOnButtonNextClick(@NonNull Context context);

    void unsubscribe();
}
