package com.tokopedia.inbox.rescenter.create.presenter;

import android.content.Context;
import androidx.annotation.NonNull;

import com.tokopedia.inbox.rescenter.create.model.passdata.ActionParameterPassData;

/**
 * Created on 6/16/16.
 */
public interface ChooseTroublePresenter {

    void setOnChooseSolutionClick(@NonNull Context context);

    void setOnFirstTimeLaunched(@NonNull Context context, ActionParameterPassData passData);

    void onsubscribe();
}
