package com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.CheckShopFavoritedUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.GetInboxReputationDetailUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.SendSmileyReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.GetInboxReputationDetailSubscriber;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.RefreshInboxReputationDetailSubscriber;
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
    private final CheckShopFavoritedUseCase checkShopFavoritedUseCase;
    private InboxReputationDetail.View viewListener;

    @Inject
    InboxReputationDetailPresenter(
            GetInboxReputationDetailUseCase getInboxReputationDetailUseCase,
            SendSmileyReputationUseCase sendSmileyReputationUseCase,
            CheckShopFavoritedUseCase checkShopFavoritedUseCase,
            SessionHandler sessionHandler) {
        this.getInboxReputationDetailUseCase = getInboxReputationDetailUseCase;
        this.sendSmileyReputationUseCase = sendSmileyReputationUseCase;
        this.checkShopFavoritedUseCase = checkShopFavoritedUseCase;
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
    public void getInboxDetail(String reputationId, int tab) {
        viewListener.showLoading();
        getInboxReputationDetailUseCase.execute(
                GetInboxReputationDetailUseCase.getParam(reputationId,
                        sessionHandler.getLoginID(),
                        tab),
                new GetInboxReputationDetailSubscriber(viewListener));
    }


    @Override
    public void sendSmiley(String reputationId, String score, int role) {
        viewListener.showLoadingDialog();
        sendSmileyReputationUseCase.execute(SendSmileyReputationUseCase.getParam(reputationId,
                score,
                role),
                new SendSmileySubscriber(viewListener, score));
    }

    @Override
    public void checkShopFavorited(int shopId) {
        checkShopFavoritedUseCase.execute(CheckShopFavoritedUseCase.getParam(sessionHandler
                .getLoginID(), shopId), new CheckShopFavoriteSubscriber(viewListener));
    }

    public void refreshPage(String reputationId, int tab) {
        viewListener.showRefresh();
        getInboxReputationDetailUseCase.execute(
                GetInboxReputationDetailUseCase.getParam(reputationId,
                        sessionHandler.getLoginID(),
                        tab),
                new RefreshInboxReputationDetailSubscriber(viewListener));
    }
}
