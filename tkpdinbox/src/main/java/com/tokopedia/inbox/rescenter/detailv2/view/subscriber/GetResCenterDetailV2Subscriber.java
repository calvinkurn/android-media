package com.tokopedia.inbox.rescenter.detailv2.view.subscriber;

import android.util.Log;

import com.tkpd.library.utils.Logger;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.DetailViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.DetailData;

import java.io.IOException;

/**
 * Created by hangnadi on 3/16/17.
 */

public class GetResCenterDetailV2Subscriber extends rx.Subscriber<DetailData> {

    private final DetailResCenterFragmentView fragmentView;

    public GetResCenterDetailV2Subscriber(DetailResCenterFragmentView fragmentView) {
        this.fragmentView = fragmentView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Log.d(this.getClass().getSimpleName(), e.getMessage());
        for (int i = 0; i < e.getStackTrace().length; i++) {
            StackTraceElement element = e.getStackTrace()[i];
            Logger.dump(this.getClass().getSimpleName(), element.toString());
        }
        if (e instanceof IOException) {
            fragmentView.setViewData(mappingTimeOutViewModel());
            fragmentView.doOnInitTimeOut();
        } else {
            fragmentView.setViewData(mappingDefaultErrorViewModel());
            fragmentView.doOnInitFailed();
        }
    }

    @Override
    public void onNext(DetailData detailData) {
//        fragmentView.setViewData(mappingViewModel(detailResCenter));
        fragmentView.doOnInitSuccess();
    }


    private DetailViewModel mappingDefaultErrorViewModel() {
        return mappingTimeOutViewModel();
    }

    private DetailViewModel mappingTimeOutViewModel() {
        DetailViewModel model = new DetailViewModel();
        model.setSuccess(false);
        model.setTimeOut(true);
        return model;
    }
}
