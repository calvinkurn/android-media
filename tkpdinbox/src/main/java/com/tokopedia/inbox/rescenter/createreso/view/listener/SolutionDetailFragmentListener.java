package com.tokopedia.inbox.rescenter.createreso.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ComplaintResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.EditAppealSolutionModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionComplaintModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionOrderModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionResponseViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;

import java.util.List;

/**
 * Created by yoasfs on 24/08/17.
 */

public interface SolutionDetailFragmentListener {

    interface View extends CustomerView {

        void submitData(ResultViewModel resultViewModel);

        void showDialogCompleteEditAppeal(EditAppealSolutionModel editAppealSolutionModel);

        void successEditSolution(String message);

        void errorEditSolution(String error);

        void showLoading();

        void hideLoading();

        void initDataToList(List<Visitable> itemList);

        Context getContext();

        ComplaintResult getComplaintResult(SolutionOrderModel orderModel);

        void calculateTotalRefund(ComplaintResult complaintResult);

        void initAmountToResult(ComplaintResult complaintResult);

        void initCheckedItem();

        void addRemoveOngkirComplaint(SolutionComplaintModel model);
    }

    interface Presenter extends CustomerPresenter<View> {

        void initData(SolutionViewModel solutionViewModel,
                      SolutionResponseViewModel solutionResponseViewModel);



        void onContinueButtonClicked(ResultViewModel resultViewModel, EditAppealSolutionModel editAppealSolutionModel);

        void submitEditAppeal(EditAppealSolutionModel editAppealSolutionModel);
    }
}
