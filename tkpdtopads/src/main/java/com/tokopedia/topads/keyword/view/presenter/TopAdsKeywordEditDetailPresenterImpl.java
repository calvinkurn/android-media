package com.tokopedia.topads.keyword.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.exception.ResponseErrorException;
import com.tokopedia.topads.keyword.domain.interactor.EditTopAdsKeywordDetailUseCase;
import com.tokopedia.topads.keyword.domain.model.EditTopAdsKeywordDetailDomainModel;
import com.tokopedia.topads.keyword.domain.model.TopAdsKeywordEditDetailInputDomainModel;
import com.tokopedia.topads.keyword.view.mapper.TopAdsKeywordEditDetailMapper;
import com.tokopedia.topads.keyword.view.model.KeywordAd;

import rx.Subscriber;

/**
 * @author sebastianuskh on 5/29/17.
 */

public class TopAdsKeywordEditDetailPresenterImpl extends TopAdsKeywordEditDetailPresenter {

    private final EditTopAdsKeywordDetailUseCase editTopadsKeywordDetailUseCase;

    public TopAdsKeywordEditDetailPresenterImpl(EditTopAdsKeywordDetailUseCase editTopadsKeywordDetailUseCase) {
        this.editTopadsKeywordDetailUseCase = editTopadsKeywordDetailUseCase;
    }

    @Override
    public void editTopAdsKeywordDetail(KeywordAd model) {
        TopAdsKeywordEditDetailInputDomainModel domainModel = TopAdsKeywordEditDetailMapper.mapViewToDomain(model);
        RequestParams params = EditTopAdsKeywordDetailUseCase.generateRequestParam(domainModel);
        editTopadsKeywordDetailUseCase.execute(params, new EditTopAdsKeywordDetailSubscriber());
    }

    @Override
    public void unSubscribe() {
        editTopadsKeywordDetailUseCase.unsubscribe();
    }

    private class EditTopAdsKeywordDetailSubscriber extends Subscriber<EditTopAdsKeywordDetailDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (isViewAttached()) {
                getView().showError(e);
            }
        }

        @Override
        public void onNext(EditTopAdsKeywordDetailDomainModel domainModel) {
            if (isViewAttached()) {
                KeywordAd viewModel = TopAdsKeywordEditDetailMapper.mapDomainToView(domainModel);
                getView().onSuccessEditTopAdsKeywordDetail(viewModel);
            }
        }
    }
}
