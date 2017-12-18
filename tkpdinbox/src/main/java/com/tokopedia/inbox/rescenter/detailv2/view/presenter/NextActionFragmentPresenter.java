package com.tokopedia.inbox.rescenter.detailv2.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.NextActionFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;

import javax.inject.Inject;

/**
 * Created by yoasfs on 16/10/17.
 */

public class NextActionFragmentPresenter
        extends BaseDaggerPresenter<NextActionFragmentListener.View>
        implements NextActionFragmentListener.Presenter {

    NextActionFragmentListener.View mainView;

    @Inject
    public NextActionFragmentPresenter() {
    }

    @Override
    public void attachView(NextActionFragmentListener.View view) {
        this.mainView = view;
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void initPresenter(NextActionDomain nextActionDomain) {
        mainView.populateMainView(nextActionDomain);
    }

    @Override
    public void initPresenter(String resolutionId) {

    }
}
