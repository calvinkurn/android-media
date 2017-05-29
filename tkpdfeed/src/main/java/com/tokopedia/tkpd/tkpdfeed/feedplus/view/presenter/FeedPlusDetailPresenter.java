package com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlusDetail;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.GetFeedsDetailUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber.FeedDetailSubscriber;

import javax.inject.Inject;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedPlusDetailPresenter extends BaseDaggerPresenter<FeedPlusDetail.View>
        implements FeedPlusDetail.Presenter {

    private final GetFeedsDetailUseCase getFeedsDetailUseCase;
    private FeedPlusDetail.View viewListener;

    @Inject
    FeedPlusDetailPresenter(GetFeedsDetailUseCase getFeedsDetailUseCase) {
        this.getFeedsDetailUseCase = getFeedsDetailUseCase;
    }

    @Override
    public void attachView(FeedPlusDetail.View view) {
        this.viewListener = view;
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        getFeedsDetailUseCase.unsubscribe();
    }

    public void getFeedDetail(String detailId, int page) {
        viewListener.showLoading();
        getFeedsDetailUseCase.execute(getFeedDetailParam(detailId, page),
                new FeedDetailSubscriber(viewListener));
    }

    private RequestParams getFeedDetailParam(String detailId, int page) {
        RequestParams params = RequestParams.create();
        params.putString(GetFeedsDetailUseCase.PARAM_DETAIL_ID, detailId);
        params.putInt(GetFeedsDetailUseCase.PARAM_PAGE, page);
        return params;
    }

    public void addToWishlist() {
        viewListener.showLoadingProgress();
    }
}
