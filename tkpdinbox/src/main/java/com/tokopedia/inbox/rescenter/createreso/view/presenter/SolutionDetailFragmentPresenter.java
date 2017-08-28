package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionDetailFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;

/**
 * Created by yoasfs on 28/08/17.
 */

public class SolutionDetailFragmentPresenter extends BaseDaggerPresenter<SolutionDetailFragmentListener.View> implements SolutionDetailFragmentListener.Presenter {

    Context context;
    SolutionDetailFragmentListener.View mainView;
    SolutionViewModel solutionViewModel;
    ResultViewModel resultViewModel;

    public SolutionDetailFragmentPresenter(Context context) {
        this.context = context;
    }


    @Override
    public void attachView(SolutionDetailFragmentListener.View view) {
        this.mainView = view;
        super.attachView(view);
    }

    @Override
    public void initResultViewModel(ResultViewModel resultViewModel, SolutionViewModel solutionViewModel) {
        this.solutionViewModel = solutionViewModel;
        this.resultViewModel = resultViewModel;
        mainView.updateAmountError("Maksimal " + solutionViewModel.getAmount().getIdr());
        mainView.updateBottomButton(resultViewModel);
    }

    @Override
    public void onAmountChanged(String amount) {
        if (!amount.equals("")) {
            int intAmount = Integer.parseInt(amount);
            if (intAmount > solutionViewModel.getAmount().getInteger()) {
                resultViewModel.refundAmount = 0;
            } else {
                resultViewModel.refundAmount = intAmount;
            }
            mainView.updateBottomButton(resultViewModel);
        }
    }
}
