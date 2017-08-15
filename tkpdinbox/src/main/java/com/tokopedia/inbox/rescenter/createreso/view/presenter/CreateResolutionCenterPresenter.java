package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.listener.CreateResolutionCenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ButtonState;

import javax.inject.Inject;

/**
 * Created by yoasfs on 11/08/17.
 */

public class CreateResolutionCenterPresenter extends BaseDaggerPresenter<CreateResolutionCenter.View> implements CreateResolutionCenter.Presenter {
    private CreateResolutionCenter.View mainView;

    @Inject
    public CreateResolutionCenterPresenter() {
    }

    @Override
    public void attachView(CreateResolutionCenter.View view) {
        this.mainView = view;
        super.attachView(view);
    }

    @Override
    public void chooseProductProblemClicked(ButtonState buttonState) {
//        buttonState.isChooseProductProblemButtonEnabled = true;
//        buttonState.isChooseProductProblemHaveValue = true;
//        buttonState.isSolutionButtonEnabled = true;
//        mainView.updateView(buttonState);
        mainView.transitionToChooseProductAndProblemPage();

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
