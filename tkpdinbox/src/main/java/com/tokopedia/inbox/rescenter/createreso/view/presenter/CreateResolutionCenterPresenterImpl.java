package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import android.content.Context;

import com.tokopedia.inbox.rescenter.createreso.view.activity.CreateResolutionCenterView;
import com.tokopedia.inbox.rescenter.createreso.view.model.ButtonState;

/**
 * Created by yoasfs on 11/08/17.
 */

public class CreateResolutionCenterPresenterImpl implements CreateResolutionCenterPresenter {
    Context context;
    CreateResolutionCenterView mainView;

    public CreateResolutionCenterPresenterImpl(Context context,
                                               CreateResolutionCenterView mainView) {
        this.context = context;
        this.mainView = mainView;
    }

    @Override
    public void chooseProductProblemClicked(ButtonState buttonState) {
        buttonState.isChooseProductProblemButtonEnabled = true;
        buttonState.isChooseProductProblemHaveValue = true;
        buttonState.isSolutionButtonEnabled = true;
        mainView.updateView(buttonState);
    }

    @Override
    public void solutionClicked(ButtonState buttonState) {
        buttonState.isSolutionHaveValue = true;
        buttonState.isUploadProveButtonEnabled = true;
        mainView.updateView(buttonState);
    }

    @Override
    public void uploadProveClicked(ButtonState buttonState) {
        buttonState.isUploadProveHaveValue = true;
        buttonState.isCreateResolutionButtonEnabled = true;
        mainView.updateView(buttonState);
    }

    @Override
    public void createResoClicked(ButtonState buttonState) {
        mainView.showCreateResoResponse(true, "Dummy Success");
    }
}
