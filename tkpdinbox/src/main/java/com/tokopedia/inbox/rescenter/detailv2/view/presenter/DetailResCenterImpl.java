package com.tokopedia.inbox.rescenter.detailv2.view.presenter;

import com.tokopedia.inbox.rescenter.detailv2.view.DetailResCenterFragment;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailViewListener;

/**
 * Created by hangnadi on 3/8/17.
 */

public class DetailResCenterImpl implements DetailResCenterPresenter {

    private final DetailViewListener viewListener;

    public DetailResCenterImpl(DetailViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void generateDetailResCenterFragment() {
        viewListener.setDetailResCenterFragment(DetailResCenterFragment.createInstance(generateExtras()));
    }

    private String generateExtras() {
        return viewListener.getResolutionID();
    }
}
