package com.tokopedia.inbox.rescenter.detailv2.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.GetResChatUseCase;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.GetDetailResChatSubscriber;

import javax.inject.Inject;

/**
 * Created by yoasfs on 09/10/17.
 */

public class DetailResChatFragmentPresenter
        extends BaseDaggerPresenter<DetailResChatFragmentListener.View>
        implements DetailResChatFragmentListener.Presenter {

    DetailResChatFragmentListener.View mainView;

    GetResChatUseCase getResChatUseCase;

    @Inject
    public DetailResChatFragmentPresenter(GetResChatUseCase getResChatUseCase) {
        this.getResChatUseCase = getResChatUseCase;
    }

    @Override
    public void attachView(DetailResChatFragmentListener.View view) {
        this.mainView = view;
        super.attachView(view);
    }


    @Override
    public void detachView() {
        getResChatUseCase.unsubscribe();
    }

    @Override
    public void initView(String resolutionId) {
        loadConversation(resolutionId);
    }

    public void loadConversation(String resolutionId) {
        mainView.showProgressBar();
        getResChatUseCase.execute(
                getResChatUseCase.getResChatUseCaseParam(String.valueOf(resolutionId)),
                new GetDetailResChatSubscriber(mainView));
    }
}
