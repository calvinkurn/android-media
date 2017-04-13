package com.tokopedia.inbox.rescenter.historyaction.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.historyaction.domain.interactor.GetHistoryActionUseCase;
import com.tokopedia.inbox.rescenter.historyaction.view.subscriber.HistoryActionSubsriber;

import javax.inject.Inject;

/**
 * Created by hangnadi on 3/23/17.
 */

@SuppressWarnings("ALL")
public class HistoryActionFragmentImpl implements HistoryActionFragmentPresenter {

    private HistoryActionFragmentView fragmentView;
    private GetHistoryActionUseCase getHistoryActionUseCase;

    @Inject
    public HistoryActionFragmentImpl(HistoryActionFragmentView fragmentView,
                                     GetHistoryActionUseCase getHistoryActionUseCase) {
        this.fragmentView = fragmentView;
        this.getHistoryActionUseCase = getHistoryActionUseCase;
    }

    @Override
    public void onFirstTimeLaunch() {
        fragmentView.setLoadingView(true);
        getHistoryActionUseCase.execute(RequestParams.EMPTY, new HistoryActionSubsriber(fragmentView));
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
        getHistoryActionUseCase.unsubscribe();
    }

}
