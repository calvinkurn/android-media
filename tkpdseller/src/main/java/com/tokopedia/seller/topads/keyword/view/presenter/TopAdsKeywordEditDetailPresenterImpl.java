package com.tokopedia.seller.topads.keyword.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.topads.keyword.domain.interactor.EditTopAdsKeywordDetailUseCase;
import com.tokopedia.seller.topads.keyword.domain.model.EditTopAdsKeywordDetailDomainModel;
import com.tokopedia.seller.topads.keyword.domain.model.TopAdsKeywordEditDetailInputDomainModel;
import com.tokopedia.seller.topads.keyword.view.mapper.TopAdsKeywordEditDetailMapper;
import com.tokopedia.seller.topads.keyword.view.model.TopAdsKeywordEditDetailViewModel;

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
    public void editTopAdsKeywordDetail(TopAdsKeywordEditDetailViewModel model) {
        TopAdsKeywordEditDetailInputDomainModel domainModel = TopAdsKeywordEditDetailMapper.mapViewToDomain(model);
        RequestParams params = EditTopAdsKeywordDetailUseCase.generateRequestParam(domainModel);
        editTopadsKeywordDetailUseCase.execute(params, new EditTopAdsKeywordDetailSubscriber());
    }

    private class EditTopAdsKeywordDetailSubscriber extends Subscriber<EditTopAdsKeywordDetailDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(EditTopAdsKeywordDetailDomainModel editTopAdsKeywordDetailDomainModel) {

        }
    }
}
