package com.tokopedia.inbox.inboxchat.data.mapper.template;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.domain.model.template.TemplateData;
import com.tokopedia.inbox.inboxchat.viewmodel.EditTemplateViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.GetTemplateViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.TemplateChatModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class EditTemplateChatMapper implements Func1<Response<TkpdResponse>, EditTemplateViewModel> {

    @Override
    public EditTemplateViewModel call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || !response.body().isNullData() && response.body().getErrorMessages() == null) {
                TemplateData data = response.body().convertDataObj(TemplateData.class);
                return convertToDomain(data);
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException(MainApplication.getAppContext().getString
                            (R.string.default_request_error_unknown));
                }
            }
        } else {
            String messageError = ErrorHandler.getErrorMessage(response);
            if (!TextUtils.isEmpty(messageError)) {
                throw new ErrorMessageException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }

    private EditTemplateViewModel convertToDomain(TemplateData data) {
        EditTemplateViewModel model = new EditTemplateViewModel();
        model.setSuccess(data.isSuccess());
        model.setEnabled(data.isIsEnable());
        return model;
    }


}
