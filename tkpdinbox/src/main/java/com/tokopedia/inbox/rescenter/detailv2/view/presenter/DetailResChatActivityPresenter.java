package com.tokopedia.inbox.rescenter.detailv2.view.presenter;

import com.tokopedia.inbox.rescenter.detailv2.view.fragment.DetailResChatFragment;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatActivityListener;


/**
 * Created by yoasfs on 10/6/17.
 */

public class DetailResChatActivityPresenter implements DetailResChatActivityListener.Presenter {

    private DetailResChatActivityListener.View mainView;

    private String resolutionId;

    public DetailResChatActivityPresenter(String resolutionId) {
        this.resolutionId = resolutionId;
    }

    @Override
    public void attachView(DetailResChatActivityListener.View view) {
        this.mainView = mainView;

    }

    @Override
    public void detachView() {

    }

    @Override
    public void initFragment() {
        mainView.inflateFragment(DetailResChatFragment.newInstance(resolutionId),
                DetailResChatFragment.class.getSimpleName());
    }
}
