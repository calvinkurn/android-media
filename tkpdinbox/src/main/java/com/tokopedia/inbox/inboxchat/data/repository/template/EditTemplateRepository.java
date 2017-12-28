package com.tokopedia.inbox.inboxchat.data.repository.template;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.viewmodel.EditTemplateViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.GetTemplateViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public interface EditTemplateRepository {
    Observable<EditTemplateViewModel> editTemplate(JsonObject parameters);

    Observable<EditTemplateViewModel> createTemplate(JsonObject object);
}
