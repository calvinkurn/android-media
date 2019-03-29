package com.tokopedia.transaction.others.creditcardauthentication;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;

/**
 * Created by kris on 10/12/17. Tokopedia
 */

public interface IUserInterfaceRepository {

    Observable<String> getUserInfo(TKPDMapParam<String, String> requestUserInfoParam);

}
