package com.tokopedia.inbox.rescenter.product.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
import com.tokopedia.core.network.apiservices.rescenter.ResolutionService;
import com.tokopedia.core.network.apiservices.user.InboxResCenterService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.rescenter.detailv2.data.factory.ResCenterDataSourceFactory;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.DetailResCenterMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.LoadMoreMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.repository.ResCenterRepositoryImpl;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.DiscussionResCenterMapper;
import com.tokopedia.inbox.rescenter.historyaction.data.mapper.HistoryActionMapper;
import com.tokopedia.inbox.rescenter.historyaddress.data.mapper.HistoryAddressMapper;
import com.tokopedia.inbox.rescenter.historyawb.data.mapper.HistoryAwbMapper;
import com.tokopedia.inbox.rescenter.product.data.mapper.ListProductMapper;
import com.tokopedia.inbox.rescenter.product.data.mapper.ProductDetailMapper;
import com.tokopedia.inbox.rescenter.product.domain.interactor.GetProductDetailUseCase;
import com.tokopedia.inbox.rescenter.product.view.subscriber.GetProductDetailSubscriber;

/**
 * Created by hangnadi on 3/28/17.
 */

public class ProductDetailFragmentImpl implements ProductDetailFragmentContract.Presenter {

    private final ProductDetailFragmentContract.ViewListener viewListener;
    private final GetProductDetailUseCase getProductDetailUseCase;

    public ProductDetailFragmentImpl(Context context, ProductDetailFragmentContract.ViewListener viewListener) {
        this.viewListener = viewListener;
        String resolutionID = viewListener.getResolutionID();
        String accessToken = new SessionHandler(context).getAccessToken(context);

        JobExecutor jobExecutor = new JobExecutor();
        UIThread uiThread = new UIThread();

        InboxResCenterService inboxResCenterService = new InboxResCenterService();

        ResCenterActService resCenterActService = new ResCenterActService();
        ResolutionService resolutionService = new ResolutionService(accessToken);

        DetailResCenterMapper detailResCenterMapper = new DetailResCenterMapper();
        HistoryAwbMapper historyAwbMapper = new HistoryAwbMapper();
        HistoryAddressMapper historyAddressMapper = new HistoryAddressMapper();
        HistoryActionMapper historyActionMapper = new HistoryActionMapper();
        ListProductMapper listProductMapper = new ListProductMapper();
        ProductDetailMapper productDetailMapper = new ProductDetailMapper();
        DiscussionResCenterMapper discussionResCenterMapper = new DiscussionResCenterMapper();
        LoadMoreMapper loadMoreMapper = new LoadMoreMapper();

        ResCenterDataSourceFactory dataSourceFactory = new ResCenterDataSourceFactory(context,
                resolutionService,
                inboxResCenterService,
                resCenterActService,
                detailResCenterMapper,
                historyAwbMapper,
                historyAddressMapper,
                historyActionMapper,
                listProductMapper,
                productDetailMapper,
                discussionResCenterMapper,
                loadMoreMapper
        );

        ResCenterRepository resCenterRepository
                = new ResCenterRepositoryImpl(resolutionID, dataSourceFactory);

        this.getProductDetailUseCase
                = new GetProductDetailUseCase(jobExecutor, uiThread, resCenterRepository);
    }

    @Override
    public void onFirstTimeLaunched() {
        viewListener.setLoadingView(true);
        viewListener.setMainView(false);
        getProductDetailUseCase.execute(getProductDetailParams(),
                new GetProductDetailSubscriber(viewListener));
    }

    private RequestParams getProductDetailParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetProductDetailUseCase.PARAM_TROUBLE_ID, viewListener.getTroubleID());
        return requestParams;
    }
}
