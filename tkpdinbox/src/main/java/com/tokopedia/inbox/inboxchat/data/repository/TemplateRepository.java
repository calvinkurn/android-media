package com.tokopedia.inbox.inboxchat.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.viewmodel.GetTemplateViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public interface TemplateRepository {

    Observable<GetTemplateViewModel> getTemplate(TKPDMapParam<String, Object> parameters);


}
