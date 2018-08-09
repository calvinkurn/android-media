package com.tokopedia.inbox.rescenter.createreso.view.presenter;


import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.PostAppealSolutionUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.PostEditSolutionUseCase;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionDetailFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.subscriber.AppealSolutionWithRefundSubscriber;
import com.tokopedia.inbox.rescenter.createreso.view.subscriber.EditSolutionWithRefundSubscriber;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.EditAppealSolutionModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.OngkirCheckboxSolutionModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.OngkirSolutionModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.ProductSolutionModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionComplaintModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionResponseViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by yoasfs on 28/08/17.
 */

public class SolutionDetailFragmentPresenter
        extends BaseDaggerPresenter<SolutionDetailFragmentListener.View>
        implements SolutionDetailFragmentListener.Presenter {

    private SolutionDetailFragmentListener.View mainView;
    private SolutionViewModel solutionViewModel;
    private ResultViewModel resultViewModel;
    private EditAppealSolutionModel editAppealSolutionModel;
    private PostEditSolutionUseCase postEditSolutionUseCase;
    private PostAppealSolutionUseCase postAppealSolutionUseCase;

    @Inject
    public SolutionDetailFragmentPresenter(PostEditSolutionUseCase postEditSolutionUseCase,
                                           PostAppealSolutionUseCase postAppealSolutionUseCase) {
        this.postEditSolutionUseCase = postEditSolutionUseCase;
        this.postAppealSolutionUseCase = postAppealSolutionUseCase;
    }


    @Override
    public void attachView(SolutionDetailFragmentListener.View view) {
        this.mainView = view;
        super.attachView(view);
    }

    @Override
    public void initResultViewModel(ResultViewModel resultViewModel,
                                    SolutionViewModel solutionViewModel,
                                    SolutionResponseViewModel solutionResponseViewModel) {
        this.solutionViewModel = solutionViewModel;
        this.resultViewModel = resultViewModel;
        updateList(solutionResponseViewModel.getComplaints());
    }

    @Override
    public void initEditAppealSolutionModel(EditAppealSolutionModel editAppealSolutionModel,
                                            SolutionViewModel solutionViewModel,
                                            SolutionResponseViewModel solutionResponseViewModel) {
        this.editAppealSolutionModel = editAppealSolutionModel;
        this.solutionViewModel = solutionViewModel;
        updateList(solutionResponseViewModel.getComplaints());
    }

    private void updateList(List<SolutionComplaintModel> modelList) {
        boolean isSingleItem = modelList.size() == 1;
        List<Visitable> itemList = new ArrayList<>();
        for (SolutionComplaintModel model : modelList) {
            if (model.getShipping() == null) {
                itemList.add(new ProductSolutionModel(
                        model.getProblem(),
                        model.getShipping(),
                        model.getProduct(),
                        model.getOrder()));
            } else {
                if (model.getShipping().isChecked()) {
                    itemList.add(new OngkirSolutionModel(
                            model.getProblem(),
                            model.getShipping(),
                            model.getProduct(),
                            model.getOrder(),
                            isSingleItem));
                } else {
                    itemList.add(new OngkirCheckboxSolutionModel(
                            model.getProblem(),
                            model.getShipping(),
                            model.getProduct(),
                            model.getOrder(),
                            isSingleItem));
                }
            }
        }
        mainView.initDataToList(itemList);
    }

    @Override
    public void onAmountChanged(String amount) {
        if (!amount.equals("")) {
            int intAmount = Integer.parseInt(amount);
            if (intAmount > solutionViewModel.getAmount().getInteger()) {
                if (resultViewModel != null) {
                    resultViewModel.refundAmount = solutionViewModel.getAmount().getInteger();
                } else {
                    editAppealSolutionModel.refundAmount = solutionViewModel.getAmount().getInteger();
                }
                mainView.updatePriceEditText(String.valueOf(solutionViewModel.getAmount().getInteger()));
            } else {
                if (resultViewModel != null) {
                    resultViewModel.refundAmount = intAmount;
                } else {
                    editAppealSolutionModel.refundAmount = intAmount;
                }
            }
        } else {
            if (resultViewModel != null) {
                resultViewModel.refundAmount = 0;
            } else {
                editAppealSolutionModel.refundAmount = 0;
            }
            mainView.updatePriceEditText(String.valueOf(0));
        }

        if (resultViewModel != null) {
            mainView.updateBottomButton(resultViewModel.refundAmount);
        } else {
            mainView.updateBottomButton(editAppealSolutionModel.refundAmount);
        }
    }

    @Override
    public void onContinueButtonClicked() {
        if (resultViewModel != null) {
            resultViewModel.solution = solutionViewModel.getId();
            resultViewModel.solutionName = solutionViewModel.getSolutionName();
            mainView.submitData(resultViewModel);
        } else {
            editAppealSolutionModel.solution = solutionViewModel.getId();
            editAppealSolutionModel.solutionName = solutionViewModel.getSolutionName();
            mainView.showDialogCompleteEditAppeal(editAppealSolutionModel);
        }
    }

    @Override
    public void submitEditAppeal() {
        if (editAppealSolutionModel.isEdit) {
            postEditSolutionUseCase.execute(PostEditSolutionUseCase.
                            postEditSolutionUseCaseParams(editAppealSolutionModel.resolutionId,
                                    editAppealSolutionModel.solution,
                                    editAppealSolutionModel.refundAmount),
                    new EditSolutionWithRefundSubscriber(mainView));
        } else {
            postAppealSolutionUseCase.execute(PostAppealSolutionUseCase.
                            postAppealSolutionUseCaseParams(
                                    editAppealSolutionModel.resolutionId,
                                    editAppealSolutionModel.solution,
                                    editAppealSolutionModel.refundAmount),
                    new AppealSolutionWithRefundSubscriber(mainView));
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        postEditSolutionUseCase.unsubscribe();
        postAppealSolutionUseCase.unsubscribe();
    }
}
