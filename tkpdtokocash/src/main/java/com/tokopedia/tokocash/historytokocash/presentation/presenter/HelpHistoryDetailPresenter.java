package com.tokopedia.tokocash.historytokocash.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tokocash.historytokocash.domain.GetReasonHelpDataUseCase;
import com.tokopedia.tokocash.historytokocash.domain.PostHelpHistoryDetailUseCase;
import com.tokopedia.tokocash.historytokocash.presentation.contract.HelpHistoryDetailContract;
import com.tokopedia.tokocash.historytokocash.presentation.model.HelpHistoryTokoCash;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 2/19/18.
 */

public class HelpHistoryDetailPresenter extends BaseDaggerPresenter<HelpHistoryDetailContract.View>
        implements HelpHistoryDetailContract.Presenter {

    private PostHelpHistoryDetailUseCase postHelpHistoryDetailUseCase;
    private GetReasonHelpDataUseCase getReasonHelpDataUseCase;

    @Inject
    public HelpHistoryDetailPresenter(PostHelpHistoryDetailUseCase postHelpHistoryDetailUseCase,
                                      GetReasonHelpDataUseCase getReasonHelpDataUseCase) {
        this.postHelpHistoryDetailUseCase = postHelpHistoryDetailUseCase;
        this.getReasonHelpDataUseCase = getReasonHelpDataUseCase;
    }

    @Override
    public void getHelpCategoryHistory() {
        getReasonHelpDataUseCase.execute(RequestParams.EMPTY, new Subscriber<List<HelpHistoryTokoCash>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                //TODO error message from string.xml
                getView().showErrorHelpHistory(e);
            }

            @Override
            public void onNext(List<HelpHistoryTokoCash> helpHistoryTokoCashes) {
                getView().loadHelpHistoryData(helpHistoryTokoCashes);
            }
        });
    }

    @Override
    public void submitHelpHistory(String subject, String message, String category, String transactionId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PostHelpHistoryDetailUseCase.SUBJECT, subject);
        requestParams.putString(PostHelpHistoryDetailUseCase.CATEGORY, category);
        requestParams.putString(PostHelpHistoryDetailUseCase.MESSAGE, message);
        requestParams.putString(PostHelpHistoryDetailUseCase.TRANSACTION_ID, transactionId);
        postHelpHistoryDetailUseCase.execute(requestParams, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getView().hideProgressLoading();
                //TODO error message from string.xml
                getView().showErrorHelpHistory(e);
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getView().hideProgressLoading();
                getView().successSubmitHelpHistory();
            }
        });
    }

    @Override
    public void destroyView() {
        if (postHelpHistoryDetailUseCase != null) postHelpHistoryDetailUseCase.unsubscribe();
        if (getReasonHelpDataUseCase != null) getReasonHelpDataUseCase.unsubscribe();
    }
}
