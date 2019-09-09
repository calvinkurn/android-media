package com.tokopedia.inbox.rescenter.edit.presenter;

import android.content.Context;
import androidx.annotation.NonNull;

import com.tokopedia.inbox.rescenter.edit.model.passdata.AppealResCenterFormData;

/**
 * Created on 8/31/16.
 */
public interface AppealResCenterPresenter {
    void setOnButtonNextClick(@NonNull Context context);

    void setOnLaunching(@NonNull Context context);

    void renderView(AppealResCenterFormData formData);

    void unsubscribe();
}
