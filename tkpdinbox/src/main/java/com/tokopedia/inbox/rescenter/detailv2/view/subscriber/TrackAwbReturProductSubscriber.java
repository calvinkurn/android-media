package com.tokopedia.inbox.rescenter.detailv2.view.subscriber;

import com.tokopedia.inbox.rescenter.detailv2.domain.model.DetailResCenter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;

/**
 * Created by hangnadi on 3/16/17.
 */

public class TrackAwbReturProductSubscriber extends rx.Subscriber<DetailResCenter>{

    private final DetailResCenterFragmentView fragmentView;

    public TrackAwbReturProductSubscriber(DetailResCenterFragmentView fragmentView) {
        this.fragmentView = fragmentView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(DetailResCenter detailResCenter) {

    }
}

