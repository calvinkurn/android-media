package com.tokopedia.inbox.inboxticket.inboxticket.intentservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.inbox.inboxticket.inboxticket.InboxTicketConstant;
import com.tokopedia.inbox.inboxticket.inboxticket.interactor.InboxTicketActRetrofitInteractor;
import com.tokopedia.inbox.inboxticket.inboxticket.interactor.InboxTicketActRetrofitInteractorImpl;
import com.tokopedia.inbox.inboxticket.inboxticket.model.InboxTicketParam;
import com.tokopedia.inbox.inboxticket.inboxticket.model.giverating.GiveRating;

/**
 * Created by Nisie on 7/5/16.
 */
public class InboxTicketIntentService extends IntentService implements InboxTicketConstant {

    InboxTicketActRetrofitInteractor networkInteractor;

    public InboxTicketIntentService() {
        super(INTENT_NAME);
        this.networkInteractor = new InboxTicketActRetrofitInteractorImpl();
    }

    public static void startAction(Context context, Bundle bundle,
                                   InboxTicketResultReceiver receiver, int type) {
        Intent intent = new Intent(context, InboxTicketIntentService.class);
        intent.putExtra(EXTRA_TYPE, type);
        intent.putExtra(EXTRA_BUNDLE, bundle);
        intent.putExtra(EXTRA_RECEIVER, receiver);
        context.startService(intent);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int action = intent.getIntExtra(EXTRA_TYPE, 0);
            Bundle bundle = intent.getBundleExtra(EXTRA_BUNDLE);
            ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RECEIVER);

            switch (action) {
                case ACTION_SET_RATING:
                    handleSetRating(bundle, receiver, action);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown Action");
            }

        } else {
            CommonUtils.dumper("Failed onHandle Intent");
        }
    }

    private void handleSetRating(Bundle bundle, final ResultReceiver receiver, int action) {
        InboxTicketParam param = bundle.getParcelable(PARAM_SET_RATING);
        final Bundle resultData = new Bundle();
        resultData.putInt(EXTRA_TYPE, action);
        resultData.putAll(bundle);


        if (param != null) {
            networkInteractor.setRating(getBaseContext(), param.getParamSetRating(),
                    new InboxTicketActRetrofitInteractor.SetRatingInboxTicketListener() {

                        @Override
                        public void onSuccess(GiveRating result) {
                            receiver.send(STATUS_SUCCESS, resultData);

                        }

                        @Override
                        public void onTimeout(String message) {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_connection_timeout));
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onNullData() {

                        }

                        @Override
                        public void onNoInternetConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }
    }
}
