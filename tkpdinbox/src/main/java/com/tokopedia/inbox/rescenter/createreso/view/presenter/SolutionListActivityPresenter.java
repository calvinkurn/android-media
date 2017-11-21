package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import android.content.Context;

import com.tokopedia.inbox.rescenter.createreso.view.fragment.SolutionListFragment;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionListActivityListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.EditAppealSolutionModel;

/**
 * Created by yoasfs on 24/08/17.
 */

public class SolutionListActivityPresenter
        implements SolutionListActivityListener.Presenter {
    Context context;
    SolutionListActivityListener.View mainView;

    public SolutionListActivityPresenter(Context context, SolutionListActivityListener.View mainView) {
        this.context = context;
        this.mainView = mainView;
    }

    @Override
    public void attachView(SolutionListActivityListener.View view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void initFragment(ResultViewModel resultViewModel) {
        mainView.inflateFragment(SolutionListFragment.newInstance(resultViewModel),
                SolutionListFragment.class.getSimpleName());
    }

    @Override
    public void initEditAppealFragment(EditAppealSolutionModel editAppealSolutionModel) {
        mainView.inflateFragment(SolutionListFragment.newEditAppealInstance(editAppealSolutionModel),
                SolutionListFragment.class.getSimpleName());
    }
}
