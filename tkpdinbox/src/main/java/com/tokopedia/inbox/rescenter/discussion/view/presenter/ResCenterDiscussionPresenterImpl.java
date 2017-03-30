package com.tokopedia.inbox.rescenter.discussion.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
import com.tokopedia.core.network.apiservices.rescenter.ResolutionService;
import com.tokopedia.core.network.apiservices.user.InboxResCenterService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.data.factory.ResCenterDataSourceFactory;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.DetailResCenterMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.repository.ResCenterRepositoryImpl;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.DiscussionResCenterMapper;
import com.tokopedia.inbox.rescenter.discussion.domain.GetResCenterDiscussionUseCase;
import com.tokopedia.inbox.rescenter.discussion.view.listener.ResCenterDiscussionView;
import com.tokopedia.inbox.rescenter.discussion.view.subscriber.GetDiscussionSubscriber;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.SendReplyDiscussionParam;
import com.tokopedia.inbox.rescenter.historyaction.data.mapper.HistoryActionMapper;
import com.tokopedia.inbox.rescenter.historyaddress.data.mapper.HistoryAddressMapper;
import com.tokopedia.inbox.rescenter.historyawb.data.mapper.HistoryAwbMapper;
import com.tokopedia.inbox.rescenter.product.data.mapper.ListProductMapper;
import com.tokopedia.inbox.rescenter.product.data.mapper.ProductDetailMapper;

/**
 * Created by nisie on 3/29/17.
 */

public class ResCenterDiscussionPresenterImpl implements ResCenterDiscussionPresenter {

    private final ResCenterDiscussionView viewListener;
    private GetResCenterDiscussionUseCase getDiscussionUseCase;
    private SendReplyDiscussionParam pass;
    private Context context;

    public ResCenterDiscussionPresenterImpl(Context context, ResCenterDiscussionView viewListener) {
        this.viewListener = viewListener;
        this.context = context;

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
                discussionResCenterMapper);

        ResCenterRepository resCenterRepository
                = new ResCenterRepositoryImpl(resolutionID, dataSourceFactory);

        getDiscussionUseCase = new GetResCenterDiscussionUseCase(
                jobExecutor, uiThread, resCenterRepository);

        pass = new SendReplyDiscussionParam();
    }


    @Override
    public void initData() {
        viewListener.showLoading();
        viewListener.setViewEnabled(false);
        getDiscussionUseCase.execute(RequestParams.EMPTY, new GetDiscussionSubscriber(viewListener));

    }

    @Override
    public void sendDiscussion() {
        if (isValid()) {
            viewListener.onSuccessSendDiscussion();
        }
    }

    @Override
    public void setDiscussionText(String discussionText) {
        pass.setMessage(discussionText);
    }

    private boolean isValid() {
        boolean isValid = true;

        if (pass.getMessage().trim().length() == 0) {
            isValid = false;
            viewListener.onErrorSendReply(context.getString(R.string.error_field_required));
        }

        return isValid;
    }
}
