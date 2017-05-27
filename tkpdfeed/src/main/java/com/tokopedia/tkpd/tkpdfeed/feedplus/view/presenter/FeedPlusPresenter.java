package com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.DataFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.GetFeedsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.GetFirstPageFeedsUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusPresenter
        extends BaseDaggerPresenter<FeedPlus.View>
        implements FeedPlus.Presenter {

    private final SessionHandler sessionHandler;
    private GetFeedsUseCase getFeedsUseCase;
    private GetFirstPageFeedsUseCase getFirstPageFeedsUseCase;
    private String currentCursor = "";
    private FeedPlus.View viewListener;

    @Inject
    FeedPlusPresenter(SessionHandler sessionHandler,
            GetFeedsUseCase getFeedsUseCase,
                      GetFirstPageFeedsUseCase getFirstPageFeedsUseCase) {
        this.sessionHandler = sessionHandler;
        this.getFeedsUseCase = getFeedsUseCase;
        this.getFirstPageFeedsUseCase = getFirstPageFeedsUseCase;

    }

    @Override
    public void attachView(FeedPlus.View viewListener) {
        super.attachView(viewListener);
        this.viewListener = viewListener;
    }

    @Override
    public void detachView() {
        super.detachView();
    }


    @Override
    public void fetchFirstPage() {
        viewListener.showRefresh();
        getFirstPageFeedsUseCase.execute(getFeedPlusParam(), getFirstPageFeedsSubscriber());
    }

    private RequestParams getFeedPlusParam() {
        RequestParams params = RequestParams.create();
        params.putInt(GetFeedsUseCase.PARAM_USER_ID, Integer.parseInt(sessionHandler.getLoginID()));
        params.putString(GetFeedsUseCase.PARAM_CURSOR, currentCursor);
        return params;
    }

    @Override
    public void fetchNextPage() {
        getFeedsUseCase.execute(getFeedPlusParam(), getFeedsSubscriber());
    }

    private Subscriber<FeedResult> getFirstPageFeedsSubscriber() {
        return new Subscriber<FeedResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                viewListener.onErrorGetFeedFirstPage(e.toString());
            }

            @Override
            public void onNext(FeedResult feedResult) {
                if (feedResult.getDataSource() == FeedResult.SOURCE_LOCAL) {
                    viewListener.onSuccessGetFeedFirstPage(
                            convertToViewModel(feedResult.getDataFeedDomainList()));
                } else {
                    viewListener.onSuccessGetFeedFirstPage(
                            convertToViewModel(feedResult.getDataFeedDomainList()));
                }

                if (feedResult.getDataFeedDomainList().size() > 0)
                    currentCursor = getCurrentCursor(feedResult);
            }
        };
    }

    private ArrayList<Visitable> convertToViewModel(List<DataFeedDomain> dataFeedDomainList) {
        ArrayList<Visitable> listFeed = new ArrayList<>();

        return listFeed;
    }

    private String getCurrentCursor(FeedResult feedResult) {
        int lastIndex = feedResult.getDataFeedDomainList().size() - 1;
        return feedResult.getDataFeedDomainList().get(lastIndex).getCursor();
    }

    private Subscriber<FeedResult> getFeedsSubscriber() {
        return new Subscriber<FeedResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(FeedResult feedResult) {

                currentCursor = getCurrentCursor(feedResult);
            }
        };
    }
}
