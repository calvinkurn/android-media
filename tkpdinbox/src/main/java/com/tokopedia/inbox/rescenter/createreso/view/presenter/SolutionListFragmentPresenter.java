package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetAppealSolutionUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetEditSolutionUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetSolutionUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.PostAppealSolutionUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.PostEditSolutionUseCase;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionListFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.subscriber.AppealSolutionSubscriber;
import com.tokopedia.inbox.rescenter.createreso.view.subscriber.AppealSolutionWithoutRefundSubscriber;
import com.tokopedia.inbox.rescenter.createreso.view.subscriber.EditSolutionSubscriber;
import com.tokopedia.inbox.rescenter.createreso.view.subscriber.EditSolutionWithoutRefundSubscriber;
import com.tokopedia.inbox.rescenter.createreso.view.subscriber.SolutionSubscriber;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.EditAppealSolutionModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionResponseViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;

import javax.inject.Inject;

/**
 * Created by yoasfs on 24/08/17.
 */

public class SolutionListFragmentPresenter
        extends BaseDaggerPresenter<SolutionListFragmentListener.View>
        implements SolutionListFragmentListener.Presenter {

    private SolutionListFragmentListener.View mainView;
    private GetSolutionUseCase getSolutionUseCase;
    private GetEditSolutionUseCase getEditSolutionUseCase;
    private PostEditSolutionUseCase postEditSolutionUseCase;
    private GetAppealSolutionUseCase getAppealSolutionUseCase;
    private PostAppealSolutionUseCase postAppealSolutionUseCase;

    private ResultViewModel resultViewModel;
    private EditAppealSolutionModel editAppealSolutionModel;

    private boolean isEditAppeal;


    @Inject
    public SolutionListFragmentPresenter(GetSolutionUseCase getSolutionUseCase,
                                         GetEditSolutionUseCase getEditSolutionUseCase,
                                         PostEditSolutionUseCase postEditSolutionUseCase,
                                         GetAppealSolutionUseCase getAppealSolutionUseCase,
                                         PostAppealSolutionUseCase postAppealSolutionUseCase) {
        this.getSolutionUseCase = getSolutionUseCase;
        this.getEditSolutionUseCase = getEditSolutionUseCase;
        this.postEditSolutionUseCase = postEditSolutionUseCase;
        this.getAppealSolutionUseCase = getAppealSolutionUseCase;
        this.postAppealSolutionUseCase = postAppealSolutionUseCase;
    }

    @Override
    public void attachView(SolutionListFragmentListener.View view) {
        this.mainView = view;
        super.attachView(view);
    }

    @Override
    public void initResultViewModel(final ResultViewModel resultViewModel) {
        this.resultViewModel = resultViewModel;
        mainView.showLoading();
        getSolutionUseCase.execute(getSolutionUseCase.getSolutionUseCaseParams(resultViewModel),
                new SolutionSubscriber(mainView));
    }

    @Override
    public void initEditAppeal(EditAppealSolutionModel editAppealSolutionModel) {
        this.editAppealSolutionModel = editAppealSolutionModel;
        mainView.showLoading();
        isEditAppeal = true;
        if (editAppealSolutionModel.isEdit) {
            getEditSolutionUseCase.execute(getEditSolutionUseCase.getEditSolutionUseCaseParams(
                    editAppealSolutionModel.resolutionId),
                    new EditSolutionSubscriber(mainView));
        } else {
            getAppealSolutionUseCase.execute(getAppealSolutionUseCase.getAppealSolutionUseCaseParams(
                    editAppealSolutionModel.resolutionId),
                    new AppealSolutionSubscriber(mainView));
        }
    }

    @Override
    public void updateLocalData(SolutionResponseViewModel solutionResponseViewModel) {
        if (solutionResponseViewModel.getRequire() != null) {
            resultViewModel.isAttachmentRequired = solutionResponseViewModel.getRequire().isAttachment();
        }
    }

    @Override
    public void submitEditAppeal(SolutionViewModel solutionViewModel) {
        editAppealSolutionModel.name = solutionViewModel.getName();
        editAppealSolutionModel.solutionName = solutionViewModel.getSolutionName();
        editAppealSolutionModel.solution = solutionViewModel.getId();
        if (editAppealSolutionModel.isEdit) {
            postEditSolutionUseCase.execute(PostEditSolutionUseCase.
                            postEditSolutionUseCaseParamsWithoutRefund(
                                    editAppealSolutionModel.resolutionId,
                                    editAppealSolutionModel.solution),
                    new EditSolutionWithoutRefundSubscriber(mainView));
        } else {
            postAppealSolutionUseCase.execute(PostAppealSolutionUseCase.
                            postAppealSolutionUseCaseParamsWithoutRefund(
                                    editAppealSolutionModel.resolutionId,
                                    editAppealSolutionModel.solution),
                    new AppealSolutionWithoutRefundSubscriber(mainView));
        }
    }

    @Override
    public void solutionClicked(SolutionViewModel solutionViewModel) {
        if (solutionViewModel.getAmount() == null) {
            if (!isEditAppeal) {
                resultViewModel.solution = solutionViewModel.getId();
                resultViewModel.solutionName = solutionViewModel.getSolutionName();
                resultViewModel.refundAmount = 0;
                mainView.submitData(resultViewModel);
            } else {
                mainView.showDialogCompleteEditAppeal(solutionViewModel);
            }
        } else {
            mainView.moveToSolutionDetail(solutionViewModel);
        }

    }

    @Override
    public void detachView() {
        super.detachView();
        getSolutionUseCase.unsubscribe();
        getEditSolutionUseCase.unsubscribe();
        postEditSolutionUseCase.unsubscribe();
        postAppealSolutionUseCase.unsubscribe();
    }
}
