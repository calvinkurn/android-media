package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionListFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;

import javax.inject.Inject;

/**
 * Created by yoasfs on 24/08/17.
 */

public class SolutionListFragmentPresenter extends BaseDaggerPresenter<SolutionListFragmentListener.View> implements SolutionListFragmentListener.Presenter {

    private SolutionListFragmentListener.View mainView;
    private ResultViewModel resultViewModel;


    @Inject
    public SolutionListFragmentPresenter(){

    }

    @Override
    public void attachView(SolutionListFragmentListener.View view) {
        this.mainView = view;
        super.attachView(view);
    }

    @Override
    public void initResultViewModel(ResultViewModel resultViewModel) {
        this.resultViewModel = resultViewModel;
    }
}
