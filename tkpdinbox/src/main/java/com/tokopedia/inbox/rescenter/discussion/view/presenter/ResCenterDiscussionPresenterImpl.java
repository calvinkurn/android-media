package com.tokopedia.inbox.rescenter.discussion.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
import com.tokopedia.core.network.apiservices.rescenter.ResolutionService;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.apiservices.upload.UploadImageService;
import com.tokopedia.core.network.apiservices.user.InboxResCenterService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.data.factory.ResCenterDataSourceFactory;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.DetailResCenterMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.repository.ResCenterRepositoryImpl;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.UploadImageRepository;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.CreatePictureMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.DiscussionResCenterMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.GenerateHostMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.LoadMoreMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.SubmitImageMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.UploadImageMapper;
import com.tokopedia.inbox.rescenter.discussion.data.repository.UploadImageRepositoryImpl;
import com.tokopedia.inbox.rescenter.discussion.data.source.UploadImageSourceFactory;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.CreatePictureUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.GenerateHostUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.GetResCenterDiscussionUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.LoadMoreDiscussionUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.ReplyDiscussionValidationUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.SendDiscussionUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.ReplyDiscussionSubmitUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.UploadImageUseCase;
import com.tokopedia.inbox.rescenter.discussion.view.listener.ResCenterDiscussionView;
import com.tokopedia.inbox.rescenter.discussion.view.subscriber.GetDiscussionSubscriber;
import com.tokopedia.inbox.rescenter.discussion.view.subscriber.LoadMoreSubscriber;
import com.tokopedia.inbox.rescenter.discussion.view.subscriber.ReplyDiscussionSubscriber;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.AttachmentViewModel;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.SendReplyDiscussionParam;
import com.tokopedia.inbox.rescenter.historyaction.data.mapper.HistoryActionMapper;
import com.tokopedia.inbox.rescenter.historyaddress.data.mapper.HistoryAddressMapper;
import com.tokopedia.inbox.rescenter.historyawb.data.mapper.HistoryAwbMapper;
import com.tokopedia.inbox.rescenter.product.data.mapper.ListProductMapper;
import com.tokopedia.inbox.rescenter.product.data.mapper.ProductDetailMapper;

import java.util.ArrayList;

/**
 * Created by nisie on 3/29/17.
 */

public class ResCenterDiscussionPresenterImpl implements ResCenterDiscussionPresenter {

    private final ResCenterDiscussionView viewListener;
    private GetResCenterDiscussionUseCase getDiscussionUseCase;
    private LoadMoreDiscussionUseCase loadMoreUseCase;
    private SendDiscussionUseCase sendDiscussionUseCase;
    private ReplyDiscussionValidationUseCase replyDiscussionValidationUseCase;
    private GenerateHostUseCase generateHostUseCase;
    private UploadImageUseCase uploadImageUseCase;
    private CreatePictureUseCase createPictureUseCase;
    private ReplyDiscussionSubmitUseCase replyDiscussionSubmitUseCase;

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

        getDiscussionUseCase = new GetResCenterDiscussionUseCase(
                jobExecutor, uiThread, resCenterRepository);

        loadMoreUseCase = new LoadMoreDiscussionUseCase(
                jobExecutor, uiThread, resCenterRepository
        );

        replyDiscussionValidationUseCase = new ReplyDiscussionValidationUseCase(
                jobExecutor, uiThread, resCenterRepository
        );

        GenerateHostActService generateHostActService = new GenerateHostActService();

        GenerateHostMapper generateHostMapper = new GenerateHostMapper();

        UploadImageService uploadImageService = new UploadImageService();

        UploadImageMapper uploadImageMapper = new UploadImageMapper();

        CreatePictureMapper createPictureMapper = new CreatePictureMapper();

        SubmitImageMapper submitImageMapper = new SubmitImageMapper();

        UploadImageSourceFactory uploadImageSourceFactory = new UploadImageSourceFactory(context,
                generateHostActService,
                uploadImageService,
                resCenterActService,
                generateHostMapper,
                uploadImageMapper,
                createPictureMapper,
                submitImageMapper

        );

        UploadImageRepository uploadImageRepository
                = new UploadImageRepositoryImpl(uploadImageSourceFactory);

        generateHostUseCase = new GenerateHostUseCase(
                jobExecutor, uiThread, uploadImageRepository
        );

        uploadImageUseCase = new UploadImageUseCase(
                jobExecutor, uiThread, uploadImageRepository
        );

        createPictureUseCase = new CreatePictureUseCase(
                jobExecutor, uiThread, uploadImageRepository
        );

        replyDiscussionSubmitUseCase = new ReplyDiscussionSubmitUseCase(
                jobExecutor, uiThread, uploadImageRepository
        );

        sendDiscussionUseCase = new SendDiscussionUseCase(
                jobExecutor, uiThread,
                generateHostUseCase,
                replyDiscussionValidationUseCase,
                uploadImageUseCase,
                createPictureUseCase,
                replyDiscussionSubmitUseCase

        );

        pass = new SendReplyDiscussionParam();
    }


    @Override
    public void initData() {
        viewListener.showLoading();
        viewListener.setViewEnabled(false);
        getDiscussionUseCase.execute(RequestParams.EMPTY,
                new GetDiscussionSubscriber(viewListener));

    }

    @Override
    public void sendReply() {
        if (isValid()) {
            sendDiscussionUseCase.execute(getSendReplyRequestParams(),
                    new ReplyDiscussionSubscriber(viewListener));
        }
    }

    private RequestParams getSendReplyRequestParams() {
        RequestParams params = RequestParams.create();
        createReplyValidationParam(params);
//        createGenerateHostParam(params);
        return params;
    }

    private void createReplyValidationParam(RequestParams params) {
        params.putString(SendDiscussionUseCase.PARAM_MESSAGE, pass.getMessage());
        params.putObject(SendDiscussionUseCase.PARAM_RESOLUTION_ID, pass.getResolutionId());
        params.putInt(SendDiscussionUseCase.PARAM_FLAG_RECEIVED, pass.getFlagReceived());

        if (pass.getAttachment() != null && pass.getAttachment().size() > 0)
            params.putObject(SendDiscussionUseCase.PARAM_ATTACHMENT, pass.getAttachment());
    }

    @Override
    public void setDiscussionText(String discussionText) {
        pass.setMessage(discussionText);
    }

    @Override
    public void loadMore() {
        viewListener.showLoading();
        viewListener.setViewEnabled(false);
        loadMoreUseCase.execute(getLoadMoreParam(), new LoadMoreSubscriber(viewListener));
    }

    private RequestParams getLoadMoreParam() {
        RequestParams params = RequestParams.create();
        params.putString(LoadMoreDiscussionUseCase.PARAM_LAST_CONVERSATION_ID, viewListener.getLastConversationId());
        return params;
    }

    @Override
    public void unsubscribeObservable() {
        loadMoreUseCase.unsubscribe();
        getDiscussionUseCase.unsubscribe();
        sendDiscussionUseCase.unsubscribe();
        generateHostUseCase.unsubscribe();
        uploadImageUseCase.unsubscribe();
        replyDiscussionValidationUseCase.unsubscribe();
        createPictureUseCase.unsubscribe();
        replyDiscussionSubmitUseCase.unsubscribe();
    }

    @Override
    public void setAttachment(ArrayList<AttachmentViewModel> attachmentList) {
        pass.setAttachment(attachmentList);
    }

    @Override
    public void setResolutionId(String resolutionID) {
        pass.setResolutionId(resolutionID);
    }

    @Override
    public void setFlagReceived(boolean flagReceived) {
        pass.setFlagReceived(flagReceived ? 1 : 0);
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
