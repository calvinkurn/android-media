package com.tokopedia.inbox.rescenter.detailv2.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.AcceptSolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.AskHelpResolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.CancelResolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.EditAddressUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.FinishResolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.GetResChatMoreUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.GetResChatUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.InputAddressUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.SendDiscussionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.SendDiscussionV2UseCase;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.GetDetailResChatSubscriber;

import javax.inject.Inject;

/**
 * Created by yoasfs on 09/10/17.
 */

public class DetailResChatFragmentPresenter
        extends BaseDaggerPresenter<DetailResChatFragmentListener.View>
        implements DetailResChatFragmentListener.Presenter {

    private static final int PARAM_LIMIT_CONVERSATION = 20;
    private DetailResChatFragmentListener.View mainView;
    private GetResChatUseCase getResChatUseCase;
    private GetResChatMoreUseCase getResChatMoreUseCase;
    private SendDiscussionUseCase sendDiscussionUseCase;
    private SendDiscussionV2UseCase sendDiscussionV2UseCase;
    private AcceptSolutionUseCase acceptSolutionUseCase;
    private AskHelpResolutionUseCase askHelpResolutionUseCase;
    private CancelResolutionUseCase cancelResolutionUseCase;
    private InputAddressUseCase inputAddressUseCase;
    private EditAddressUseCase editAddressUseCase;
    private FinishResolutionUseCase finishResolutionUseCase;
    private String resolutionId;
    private Context context;
    private String[] videoExtensions = {
            "mp4", "m4v", "mov", "ogv"
    };

    @Inject
    public DetailResChatFragmentPresenter(GetResChatUseCase getResChatUseCase,
                                          GetResChatMoreUseCase getResChatMoreUseCase,
                                          SendDiscussionUseCase sendDiscussionUseCase,
                                          SendDiscussionV2UseCase sendDiscussionV2UseCase,
                                          AcceptSolutionUseCase acceptSolutionUseCase,
                                          AskHelpResolutionUseCase askHelpResolutionUseCase,
                                          CancelResolutionUseCase cancelResolutionUseCase,
                                          InputAddressUseCase inputAddressUseCase,
                                          EditAddressUseCase editAddressUseCase,
                                          FinishResolutionUseCase finishResolutionUseCase) {
        this.getResChatUseCase = getResChatUseCase;
        this.getResChatMoreUseCase = getResChatMoreUseCase;
        this.acceptSolutionUseCase = acceptSolutionUseCase;
        this.sendDiscussionUseCase = sendDiscussionUseCase;
        this.sendDiscussionV2UseCase = sendDiscussionV2UseCase;
        this.askHelpResolutionUseCase = askHelpResolutionUseCase;
        this.cancelResolutionUseCase = cancelResolutionUseCase;
        this.inputAddressUseCase = inputAddressUseCase;
        this.editAddressUseCase = editAddressUseCase;
        this.finishResolutionUseCase = finishResolutionUseCase;
    }

    @Override
    public void attachView(DetailResChatFragmentListener.View view) {
        this.mainView = view;
        super.attachView(view);
    }


    @Override
    public void detachView() {
        getResChatUseCase.unsubscribe();
        getResChatMoreUseCase.unsubscribe();
        sendDiscussionUseCase.unsubscribe();
        sendDiscussionV2UseCase.unsubscribe();
        acceptSolutionUseCase.unsubscribe();
        askHelpResolutionUseCase.unsubscribe();
        cancelResolutionUseCase.unsubscribe();
        inputAddressUseCase.unsubscribe();
        editAddressUseCase.unsubscribe();
        finishResolutionUseCase.unsubscribe();
    }

    @Override
    public void initView(String resolutionId) {
        this.resolutionId = resolutionId;
        loadConversation(resolutionId);
    }

    public void loadConversation(String resolutionId) {
        mainView.showProgressBar();
        getResChatUseCase.execute(
                GetResChatUseCase.getResChatUseCaseParam(
                        String.valueOf(resolutionId),
                        PARAM_LIMIT_CONVERSATION),
                new GetDetailResChatSubscriber(mainView));
    }
}
