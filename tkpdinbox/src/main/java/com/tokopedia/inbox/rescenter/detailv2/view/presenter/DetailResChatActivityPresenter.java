package com.tokopedia.inbox.rescenter.detailv2.view.presenter;

import com.tokopedia.inbox.rescenter.detailv2.view.fragment.DetailResChatFragment;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatActivityListener;


/**
 * Created by yoasfs on 10/6/17.
 */

public class DetailResChatActivityPresenter implements DetailResChatActivityListener.Presenter {

    private DetailResChatActivityListener.View mainView;

    private String resolutionId;

    public DetailResChatActivityPresenter(DetailResChatActivityListener.View mainView, String resolutionId) {
        this.mainView = mainView;
        this.resolutionId = resolutionId;
    }

    @Override
    public void attachView(DetailResChatActivityListener.View mainView) {
        this.mainView = mainView;

    }

    @Override
    public void detachView() {

    }

    @Override
    public void initFragment(boolean isSeller, String resolutionId, boolean isReload) {
        if (isSeller) {
            mainView.inflateFragment(DetailResChatFragment.newSellerInstance(resolutionId),
                    DetailResChatFragment.class.getSimpleName(), isReload);
        } else {
            mainView.inflateFragment(DetailResChatFragment.newBuyerInstance(resolutionId),
                    DetailResChatFragment.class.getSimpleName(), isReload);
        }
    }
}
