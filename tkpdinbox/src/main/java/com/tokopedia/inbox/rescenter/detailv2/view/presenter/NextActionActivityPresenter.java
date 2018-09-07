package com.tokopedia.inbox.rescenter.detailv2.view.presenter;

import com.tokopedia.inbox.rescenter.detailv2.view.fragment.NextActionFragment;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.NextActionActivityListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;

/**
 * Created by yoasfs on 16/10/17.
 */

public class NextActionActivityPresenter implements NextActionActivityListener.Presenter {

    private NextActionActivityListener.View mainView;
    private NextActionDomain nextActionDomain;
    private String resolutionId;
    private int resolutionStatus;

    public NextActionActivityPresenter(NextActionActivityListener.View mainView,
                                       String resolutionId,
                                       NextActionDomain nextActionDomain,
                                       int resolutionStatus) {
        this.mainView = mainView;
        this.resolutionId = resolutionId;
        this.nextActionDomain = nextActionDomain;
        this.resolutionStatus = resolutionStatus;
    }

    @Override
    public void attachView(NextActionActivityListener.View view) {
        this.mainView = view;
    }

    @Override
    public void detachView() {

    }

    @Override
    public void initFragment() {
        mainView.inflateFragment(NextActionFragment.newInstance(
                resolutionId,
                nextActionDomain,
                resolutionStatus),
                NextActionFragment.class.getSimpleName());

    }
}
