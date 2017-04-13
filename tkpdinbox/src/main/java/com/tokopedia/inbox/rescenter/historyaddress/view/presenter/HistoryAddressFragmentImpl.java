package com.tokopedia.inbox.rescenter.historyaddress.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.historyaddress.domain.interactor.GetHistoryAddressUseCase;
import com.tokopedia.inbox.rescenter.historyaddress.view.subscriber.HistoryAddressSubsriber;

import javax.inject.Inject;

/**
 * Created by hangnadi on 3/23/17.
 */

@SuppressWarnings("ALL")
public class HistoryAddressFragmentImpl implements HistoryAddressFragmentPresenter {

    private HistoryAddressFragmentView fragmentView;
    private GetHistoryAddressUseCase getHistoryAddressUseCase;

    @Inject
    public HistoryAddressFragmentImpl(HistoryAddressFragmentView fragmentView,
                                      GetHistoryAddressUseCase getHistoryAddressUseCase) {
        this.fragmentView = fragmentView;
        this.getHistoryAddressUseCase = getHistoryAddressUseCase;
    }

    @Override
    public void onFirstTimeLaunch() {
        fragmentView.setLoadingView(true);
        getHistoryAddressUseCase.execute(RequestParams.EMPTY, new HistoryAddressSubsriber(fragmentView));
    }

    @Override
    public void refreshPage() {
        onFirstTimeLaunch();
    }

    @Override
    public void setOnDestroyView() {
        unSubscibeObservable();
    }

    private void unSubscibeObservable() {
        getHistoryAddressUseCase.unsubscribe();
    }
}
