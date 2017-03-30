package com.tokopedia.inbox.rescenter.discussion.view.subscriber;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.discussion.domain.model.DiscussionModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.DiscussionItemData;
import com.tokopedia.inbox.rescenter.discussion.view.listener.ResCenterDiscussionView;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by nisie on 3/30/17.
 */

public class GetDiscussionSubscriber extends Subscriber<DiscussionModel> {

    private ResCenterDiscussionView viewListener;

    public GetDiscussionSubscriber(ResCenterDiscussionView viewListener) {
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
    public void onNext(DiscussionModel discussionModel) {
        viewListener.finishLoading();
        viewListener.onSuccessGetDiscussion(mappingToViewModel(discussionModel));
    }

    private List<DiscussionItemViewModel> mappingToViewModel(DiscussionModel discussionModel) {
        List<DiscussionItemViewModel> list = new ArrayList<>();

        for(DiscussionItemData item : discussionModel.getListDiscussionData()){
            DiscussionItemViewModel viewModel = new DiscussionItemViewModel();
            viewModel.setMessage(item.getSolutionRemark());
            viewModel.setMessageReplyTimeFmt(item.getCreateTimeStr());
            viewModel.setUserName("ASDASD");
            viewModel.setUserLabelId(1);
            viewModel.setUserLabel("USERLABEL");
            list.add(viewModel);
        }

        return list;
    }
}
