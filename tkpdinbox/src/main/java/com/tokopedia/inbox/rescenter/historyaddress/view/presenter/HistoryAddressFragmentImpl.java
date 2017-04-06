package com.tokopedia.inbox.rescenter.historyaddress.view.presenter;

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
import com.tokopedia.inbox.rescenter.discussion.data.mapper.LoadMoreMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.repository.ResCenterRepositoryImpl;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.DiscussionResCenterMapper;
import com.tokopedia.inbox.rescenter.historyaction.data.mapper.HistoryActionMapper;
import com.tokopedia.inbox.rescenter.historyaddress.data.mapper.HistoryAddressMapper;
import com.tokopedia.inbox.rescenter.historyaddress.domain.interactor.GetHistoryAddressUseCase;
import com.tokopedia.inbox.rescenter.historyaddress.view.subscriber.HistoryAddressSubsriber;
import com.tokopedia.inbox.rescenter.historyawb.data.mapper.HistoryAwbMapper;
import com.tokopedia.inbox.rescenter.product.data.mapper.ListProductMapper;
import com.tokopedia.inbox.rescenter.product.data.mapper.ProductDetailMapper;

/**
 * Created by hangnadi on 3/23/17.
 */

@SuppressWarnings("ALL")
public class HistoryAddressFragmentImpl implements HistoryAddressFragmentPresenter {

    private final HistoryAddressFragmentView fragmentView;
    private final GetHistoryAddressUseCase historyAddressUseCase;

    public HistoryAddressFragmentImpl(Context context, HistoryAddressFragmentView fragmentView) {
        this.fragmentView = fragmentView;
        String resolutionID = fragmentView.getResolutionID();
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

        this.historyAddressUseCase
                = new GetHistoryAddressUseCase(jobExecutor, uiThread, resCenterRepository);

    }

    @Override
    public void onFirstTimeLaunch() {
        fragmentView.setLoadingView(true);
        historyAddressUseCase.execute(RequestParams.EMPTY, new HistoryAddressSubsriber(fragmentView));
    }

    @Override
    public void refreshPage() {
        onFirstTimeLaunch();
    }

    @Override
    public void setOnDestroyView() {
        unSubscibeObservable();
    }

    private void unSubscibeObservable() {
        historyAddressUseCase.unsubscribe();
    }
}
