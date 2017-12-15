package com.tokopedia.inbox.rescenter.historyawb.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.TrackingAwbReturProduct;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.TrackingAwbReturProductHistory;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.TrackingDialogViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.TrackingHistoryDialogViewModel;
import com.tokopedia.inbox.rescenter.historyawb.domain.interactor.GetHistoryAwbUseCase;
import com.tokopedia.inbox.rescenter.historyawb.domain.interactor.TrackAwbReturProductUseCase;
import com.tokopedia.inbox.rescenter.historyawb.view.subscriber.HistoryAwbSubsriber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by hangnadi on 3/23/17.
 */

@SuppressWarnings("ALL")
public class HistoryShippingFragmentImpl implements HistoryShippingFragmentPresenter {

    private HistoryShippingFragmentView fragmentView;
    private GetHistoryAwbUseCase getHistoryAwbUseCase;

    @Inject
    public HistoryShippingFragmentImpl(HistoryShippingFragmentView fragmentView,
                                       GetHistoryAwbUseCase getHistoryAwbUseCase,
                                       TrackAwbReturProductUseCase trackAwbReturProductUseCase) {
        this.fragmentView = fragmentView;
        this.getHistoryAwbUseCase = getHistoryAwbUseCase;
    }

    @Override
    public void onFirstTimeLaunch() {
        fragmentView.setLoadingView(true);
        fragmentView.showInpuNewShippingAwb(false);
        getHistoryAwbUseCase.execute(getHistoryAwbParams(), new HistoryAwbSubsriber(fragmentView));
    }

    private RequestParams getHistoryAwbParams() {
        RequestParams params = RequestParams.create();
        params.putString(GetHistoryAwbUseCase.PARAM_RESOLUTION_ID, fragmentView.getResolutionID());
        return params;
    }

    @Override
    public void refreshPage() {
        fragmentView.resetList();
        onFirstTimeLaunch();
    }

    @Override
    public void setOnDestroyView() {
        unSubscibeObservable();
    }

    private void unSubscibeObservable() {
        getHistoryAwbUseCase.unsubscribe();
    }

}
