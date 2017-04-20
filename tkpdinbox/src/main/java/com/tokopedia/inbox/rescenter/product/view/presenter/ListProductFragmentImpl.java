package com.tokopedia.inbox.rescenter.product.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.product.domain.interactor.GetListProductUseCase;
import com.tokopedia.inbox.rescenter.product.view.subscriber.ListProductSubsriber;

/**
 * Created by hangnadi on 3/23/17.
 */

@SuppressWarnings("ALL")
public class ListProductFragmentImpl implements ListProductFragmentPresenter {

    private ListProductFragmentView fragmentView;
    private GetListProductUseCase getListProductUseCase;

    public ListProductFragmentImpl(ListProductFragmentView fragmentView,
                                   GetListProductUseCase getListProductUseCase) {
        this.fragmentView = fragmentView;
        this.getListProductUseCase = getListProductUseCase;
    }

    @Override
    public void onFirstTimeLaunch() {
        fragmentView.setLoadingView(true);
        getListProductUseCase.execute(getListProductParams(), new ListProductSubsriber(fragmentView));
    }

    private RequestParams getListProductParams() {
        RequestParams params = RequestParams.create();
        params.putString(GetListProductUseCase.PARAM_RESOLUTION_ID, fragmentView.getResolutionID());
        return params;
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
        getListProductUseCase.unsubscribe();
    }
}
