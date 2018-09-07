package com.tokopedia.session.register.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.register.data.model.RegisterEmailModel;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by nisie on 4/13/17.
 */

public interface RegisterEmailRepository {

    Observable<RegisterEmailModel> registerEmail(HashMap<String, Object> parameters);
}
