package com.tokopedia.seller.opportunity.presenter.subscriber;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.data.AcceptReplacementModel;
import com.tokopedia.seller.opportunity.listener.OpportunityView;
import com.tokopedia.seller.opportunity.presentation.ActionViewData;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by nisie on 4/6/17.
 */

public class AcceptOpportunitySubscriber extends Subscriber<AcceptReplacementModel> {

    private final OpportunityView view;

    public AcceptOpportunitySubscriber(OpportunityView view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof UnknownHostException) {
            view.onErrorTakeOpportunity(view.getString(R.string.msg_no_connection));
        } else if (e instanceof SocketTimeoutException) {
            view.onErrorTakeOpportunity(view.getString(R.string.default_request_error_timeout));
        } else if (e instanceof IOException) {
            view.onErrorTakeOpportunity(view.getString(R.string.default_request_error_internal_server));
        } else if (e.getLocalizedMessage() != null
                && e instanceof ErrorMessageException) {
            view.onErrorTakeOpportunity(e.getLocalizedMessage());
        } else if (e instanceof RuntimeException
                && e.getLocalizedMessage() != null &&
                e.getLocalizedMessage().length() <= 3) {
            new ErrorHandler(new ErrorListener() {
                @Override
                public void onUnknown() {
                    view.onErrorTakeOpportunity(view.getString(R.string.default_request_error_unknown));
                }

                @Override
                public void onTimeout() {
                    view.onErrorTakeOpportunity(view.getString(R.string.default_request_error_timeout));

                }

                @Override
                public void onServerError() {
                    view.onErrorTakeOpportunity(view.getString(R.string.default_request_error_internal_server));

                }

                @Override
                public void onBadRequest() {
                    view.onErrorTakeOpportunity(view.getString(R.string.default_request_error_bad_request));

                }

                @Override
                public void onForbidden() {
                    view.onErrorTakeOpportunity(view.getString(R.string.default_request_error_forbidden_auth));

                }
            }, Integer.parseInt(e.toString()));
        } else {
            view.onErrorTakeOpportunity(view.getString(R.string.default_request_error_unknown));
        }
    }

    @Override
    public void onNext(AcceptReplacementModel acceptReplacementModel) {
        if (acceptReplacementModel.isSuccess())
            view.onSuccessTakeOpportunity(mappingAcceptReplacementViewModel(acceptReplacementModel));
        else
            view.onErrorTakeOpportunity(mappingAcceptReplacementViewModel(acceptReplacementModel).getMessage());
    }

    private ActionViewData mappingAcceptReplacementViewModel(AcceptReplacementModel model) {
        ActionViewData data = new ActionViewData();
        data.setSuccess(model.isSuccess());
        data.setMessage(model.getMessage());
        return data;
    }
}
