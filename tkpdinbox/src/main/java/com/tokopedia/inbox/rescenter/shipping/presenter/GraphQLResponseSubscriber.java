package com.tokopedia.inbox.rescenter.shipping.presenter;

import com.tokopedia.graphql.data.model.GraphqlResponse;

import rx.Subscriber;

public class GraphQLResponseSubscriber extends Subscriber<GraphqlResponse> {
    interface ResponseCallbacks {
        void onError(Throwable e);

        void onRequestVerified(GraphqlResponse graphqlResponse);

        void onCreateEditTicket(GraphqlResponse graphqlResponse);
    }

    private ResponseCallbacks responseCallbacks;

    private boolean verifyCall;

    void setVerifyCall(boolean verifyCall) {
        this.verifyCall = verifyCall;
    }

    void setResponseCallbacks(ResponseCallbacks callbacks) {
        this.responseCallbacks = callbacks;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        responseCallbacks.onError(e);
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        if (verifyCall)
            responseCallbacks.onRequestVerified(graphqlResponse);
        else
            responseCallbacks.onCreateEditTicket(graphqlResponse);
    }

    public void unSubscribe() {
        responseCallbacks = null;
        if (!isUnsubscribed())
            unsubscribe();
    }
}
