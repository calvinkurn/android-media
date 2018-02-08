package com.tokopedia.inbox.inboxmessage.presenter;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.inbox.inboxmessage.fragment.SendMessageFragment;
import com.tokopedia.inbox.inboxmessage.interactor.SendMessageRetrofitInteractor;
import com.tokopedia.inbox.inboxmessage.interactor.SendMessageRetrofitInteractorImpl;
import com.tokopedia.inbox.inboxmessage.model.SendMessagePass;

import java.util.Map;

/**
 * Created by Nisie on 5/26/16.
 */
public class SendMessagePresenterImpl implements SendMessagePresenter {

    SendMessageFragment viewListener;
    SendMessageRetrofitInteractor networkInteractor;

    public SendMessagePresenterImpl(SendMessageFragment viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = new SendMessageRetrofitInteractorImpl();
    }


    @Override
    public boolean isValidMessage() {
        Boolean isValid = true;

        if (viewListener.getContent().trim().length() == 0) {
            isValid = false;
            viewListener.setContentError(viewListener.getString(R.string.error_field_required));
        }

        if (viewListener.getSubject().trim().length() == 0) {
            isValid = false;
            viewListener.setSubjectError(viewListener.getString(R.string.error_field_required));
        }

        return isValid;
    }

    @Override
    public void doSendMessage() {
        viewListener.removeError();

        if (isValidMessage()) {
            viewListener.showLoading();
            viewListener.setActionsEnabled(false);
            networkInteractor.sendMessage(viewListener.getActivity(), getSendMessageParam(), new SendMessageRetrofitInteractor.SendMessageListener() {
                @Override
                public void onSuccess() {
                    viewListener.finishLoading();
                    viewListener.setActionsEnabled(true);
                    CommonUtils.UniversalToast(viewListener.getActivity(), viewListener.getString(R.string.success_send_msg));
                    viewListener.getActivity().finish();
                }

                @Override
                public void onTimeout() {
                    viewListener.setActionsEnabled(true);
                    viewListener.showError(viewListener.getString(R.string.msg_connection_timeout));
                }

                @Override
                public void onError(String error) {
                    viewListener.setActionsEnabled(true);
                    viewListener.showError(error);
                }

                @Override
                public void onNullData() {

                }

                @Override
                public void onNoNetworkConnection() {
                    viewListener.setActionsEnabled(true);
                    viewListener.showError(viewListener.getString(R.string.msg_no_connection));
                }
            });
        }
    }

    private Map<String, String> getSendMessageParam() {
        SendMessagePass param = new SendMessagePass();
        param.setMessage(viewListener.getContent());
        param.setMessage_subject(viewListener.getSubject());
        param.setTo_shop_id(viewListener.getArguments().getString(SendMessageFragment.PARAM_SHOP_ID,""));
        param.setTo_user_id(viewListener.getArguments().getString(SendMessageFragment.PARAM_USER_ID,""));
        param.setSource(viewListener.getArguments().getString(SendMessageFragment.PARAM_SOURCE,""));
        return param.getSendMessageParam();
    }

    @Override
    public void onDestroyView() {
        networkInteractor.unSubscribeObservable();
    }
}
