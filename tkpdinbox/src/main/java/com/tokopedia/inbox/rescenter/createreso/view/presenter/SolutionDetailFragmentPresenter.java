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
    public void initData(SolutionViewModel solutionViewModel,
                         SolutionResponseViewModel solutionResponseViewModel) {
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
    public void onContinueButtonClicked(ResultViewModel resultViewModel, EditAppealSolutionModel editAppealSolutionModel) {
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
    public void submitEditAppeal(EditAppealSolutionModel editAppealSolutionModel) {
        if (editAppealSolutionModel.isEdit) {
            postEditSolutionUseCase.execute(PostEditSolutionUseCase.
                            postEditSolution(editAppealSolutionModel),
                    new EditSolutionWithRefundSubscriber(mainView));
        } else {
            postAppealSolutionUseCase.execute(PostAppealSolutionUseCase.
                            postAppealSolution(editAppealSolutionModel),
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
