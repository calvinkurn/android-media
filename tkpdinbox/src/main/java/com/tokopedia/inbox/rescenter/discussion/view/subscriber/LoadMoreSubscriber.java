package com.tokopedia.inbox.rescenter.discussion.view.subscriber;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.discussion.domain.model.loadmore.LoadMoreAttachment;
import com.tokopedia.inbox.rescenter.discussion.domain.model.loadmore.LoadMoreItemData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.loadmore.LoadMoreModel;
import com.tokopedia.inbox.rescenter.discussion.view.listener.ResCenterDiscussionView;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.AttachmentViewModel;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by nisie on 3/31/17.
 */

public class LoadMoreSubscriber extends Subscriber<LoadMoreModel> {

    private ResCenterDiscussionView viewListener;

    public LoadMoreSubscriber(ResCenterDiscussionView viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof UnknownHostException) {
            viewListener.onErrorGetDiscussion(
                    viewListener.getString(R.string.msg_no_connection));
        } else if (e instanceof RuntimeException &&
                e.getLocalizedMessage() != null &&
                e.getLocalizedMessage().length() <= 3) {
            new ErrorHandler(new ErrorListener() {
                @Override
                public void onUnknown() {
                    viewListener.onErrorGetDiscussion(
                            viewListener.getString(R.string.default_request_error_unknown));
                }

                @Override
                public void onTimeout() {
                    viewListener.onErrorGetDiscussion(
                            viewListener.getString(R.string.default_request_error_timeout));
                }

                @Override
                public void onServerError() {
                    viewListener.onErrorGetDiscussion(
                            viewListener.getString(R.string.default_request_error_internal_server));
                }

                @Override
                public void onBadRequest() {
                    viewListener.onErrorGetDiscussion(
                            viewListener.getString(R.string.default_request_error_bad_request));
                }

                @Override
                public void onForbidden() {
                    viewListener.onErrorGetDiscussion(
                            viewListener.getString(R.string.default_request_error_forbidden_auth));
                }
            }, Integer.parseInt(e.getLocalizedMessage()));
        } else if (e instanceof MessageErrorException
                && e.getLocalizedMessage() != null) {
            viewListener.onErrorGetDiscussion(e.getLocalizedMessage());
        } else {
            viewListener.onErrorGetDiscussion(
                    viewListener.getString(R.string.default_request_error_unknown));
        }
    }

    @Override
    public void onNext(LoadMoreModel loadMoreModel) {
        viewListener.finishLoading();
        viewListener.onSuccessLoadMore(mappingToViewModel(loadMoreModel), loadMoreModel.canLoadMore());
    }

    private List<DiscussionItemViewModel> mappingToViewModel(LoadMoreModel loadMoreModel) {
        List<DiscussionItemViewModel> list = new ArrayList<>();

        for(LoadMoreItemData item : loadMoreModel.getListDiscussionData()){
            DiscussionItemViewModel viewModel = new DiscussionItemViewModel();
            viewModel.setMessage(item.getSolutionRemark());
            viewModel.setMessageReplyTimeFmt(item.getCreateTimeStr());
            viewModel.setUserLabelId(item.getActionBy());
            viewModel.setUserLabel(item.getActionByStr());
            viewModel.setAttachment(mappingAttachment(item.getAttachment()));
            viewModel.setConversationId(String.valueOf(item.getResConvId()));
            viewModel.setMessageCreateBy(item.getCreateBy());
            list.add(viewModel);
        }

        return list;
    }

    private List<AttachmentViewModel>  mappingAttachment(List<LoadMoreAttachment> listAttachments) {
        List<AttachmentViewModel> list = new ArrayList<>();
        for (LoadMoreAttachment item : listAttachments) {
            AttachmentViewModel attachment = new AttachmentViewModel();
            attachment.setUrl(item.getUrl());
            attachment.setImgThumb(item.getImageThumb());
            attachment.setImgLarge(item.getImageThumb());
            list.add(attachment);
        }
        return list;
    }


}

