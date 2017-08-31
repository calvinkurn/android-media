package com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.GetInboxReputationDetailUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.SendSmileyReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.GetInboxReputationDetailSubscriber;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.SendSmileySubscriber;

import javax.inject.Inject;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailPresenter
        extends BaseDaggerPresenter<InboxReputationDetail.View>
        implements InboxReputationDetail.Presenter {

    private final GetInboxReputationDetailUseCase getInboxReputationDetailUseCase;
    private final SessionHandler sessionHandler;
    private final SendSmileyReputationUseCase sendSmileyReputationUseCase;
    private InboxReputationDetail.View viewListener;
    private PagingHandler pagingHandler;

    @Inject
    InboxReputationDetailPresenter(
            GetInboxReputationDetailUseCase getInboxReputationDetailUseCase,
            SendSmileyReputationUseCase sendSmileyReputationUseCase,
            SessionHandler sessionHandler) {
        this.getInboxReputationDetailUseCase = getInboxReputationDetailUseCase;
        this.sendSmileyReputationUseCase = sendSmileyReputationUseCase;
        this.pagingHandler = new PagingHandler();
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void attachView(InboxReputationDetail.View view) {
        super.attachView(view);
        this.viewListener = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        getInboxReputationDetailUseCase.unsubscribe();
        sendSmileyReputationUseCase.unsubscribe();
    }

    @Override
    public void getInboxDetail(String id, int tab) {
        viewListener.showLoading();
        getInboxReputationDetailUseCase.execute(
                GetInboxReputationDetailUseCase.getParam(id,
                        sessionHandler.getLoginID(),
                        tab),
                new GetInboxReputationDetailSubscriber(viewListener));
    }

    @Override
    public void getNextPage(int lastItemPosition, int visibleItem) {

    }

    @Override
    public void sendSmiley(String value) {
        viewListener.showLoadingDialog();
        sendSmileyReputationUseCase.execute(SendSmileyReputationUseCase.getParam(value),
                new SendSmileySubscriber(viewListener));
    }
}
