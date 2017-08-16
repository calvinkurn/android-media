package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProductProblemResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetProductProblemUseCase;
import com.tokopedia.inbox.rescenter.createreso.view.listener.CreateResolutionCenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ButtonState;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by yoasfs on 11/08/17.
 */

public class CreateResolutionCenterPresenter extends BaseDaggerPresenter<CreateResolutionCenter.View> implements CreateResolutionCenter.Presenter {
    private CreateResolutionCenter.View mainView;
    private GetProductProblemUseCase getProductProblemUseCase;

    @Inject
    public CreateResolutionCenterPresenter(GetProductProblemUseCase getProductProblemUseCase) {
        this.getProductProblemUseCase = getProductProblemUseCase;
    }

    @Override
    public void attachView(CreateResolutionCenter.View view) {
        this.mainView = view;
        super.attachView(view);
    }

    @Override
    public void loadProductProblem(int orderId) {
        getProductProblemUseCase.execute(getProductProblemUseCase.getProductProblemUseCaseParam(orderId), new Subscriber<ProductProblemResponseDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mainView.showErrorToast(e.getLocalizedMessage());
            }

            @Override
            public void onNext(ProductProblemResponseDomain productProblemResponseDomain) {
                if (productProblemResponseDomain != null) {
                    mainView.showSuccessToast();
                }
            }
        });
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
