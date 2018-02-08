package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import android.content.Context;

import com.tokopedia.inbox.rescenter.createreso.view.fragment.SolutionDetailFragment;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionDetailActivityListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.EditAppealSolutionModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;

/**
 * Created by yoasfs on 24/08/17.
 */

public class SolutionDetailActivityPresenter
        implements SolutionDetailActivityListener.Presenter {
    Context context;
    SolutionDetailActivityListener.View mainView;

    public SolutionDetailActivityPresenter(Context context,
                                           SolutionDetailActivityListener.View mainView) {
        this.context = context;
        this.mainView = mainView;
    }

    @Override
    public void attachView(SolutionDetailActivityListener.View view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void initFragment(ResultViewModel resultViewModel, SolutionViewModel solutionViewModel) {
        mainView.inflateFragment(SolutionDetailFragment.newInstance(resultViewModel, solutionViewModel),
                SolutionDetailFragment.class.getSimpleName());
    }

    @Override
    public void initEditAppealFragment(EditAppealSolutionModel editAppealSolutionModel,
                                       SolutionViewModel solutionViewModel) {
        mainView.inflateFragment(SolutionDetailFragment.newEditAppealDetailInstance(
                editAppealSolutionModel,
                solutionViewModel),
                SolutionDetailFragment.class.getSimpleName());
    }
}
