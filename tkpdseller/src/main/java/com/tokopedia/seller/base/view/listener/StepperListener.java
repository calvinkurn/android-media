package com.tokopedia.seller.base.view.listener;

import com.tokopedia.seller.base.view.model.StepperModel;

/**
 * Created by zulfikarrahman on 7/27/17.
 */
@Deprecated
public interface StepperListener<T extends StepperModel> {

    void goToNextPage(T object);

    void finishPage();

    T getStepperModel();

}