package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ButtonState;

/**
 * Created by yoasfs on 11/08/17.
 */

public interface CreateResolutionCenterPresenter {

    void chooseProductProblemClicked(ButtonState buttonState);

    void solutionClicked(ButtonState buttonState);

    void uploadProveClicked(ButtonState buttonState);

    void createResoClicked(ButtonState buttonState);
}
