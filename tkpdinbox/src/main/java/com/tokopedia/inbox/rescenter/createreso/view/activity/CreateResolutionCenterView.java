package com.tokopedia.inbox.rescenter.createreso.view.activity;

import com.tokopedia.inbox.rescenter.createreso.view.model.ButtonState;

/**
 * Created by yoasfs on 11/08/17.
 */

public interface CreateResolutionCenterView {
    void updateView(ButtonState buttonState);
    void showCreateResoResponse(boolean isSuccess, String message);
}
