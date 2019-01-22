package com.tokopedia.transaction.others.creditcardauthentication;

import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.people.model.PeopleInfoData;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 10/12/17. Tokopedia
 */

public class UserInfoRepository implements IUserInterfaceRepository{

    private PeopleService service;

    public UserInfoRepository(PeopleService service) {
        this.service = service;
    }

    @Override
    public Observable<String> getUserInfo(TKPDMapParam<String, String> requestUserInfoParam) {
        return service.getApi().getPeopleInfo(requestUserInfoParam).map(new Func1<Response<TkpdResponse>, String>() {
            @Override
            public String call(Response<TkpdResponse> response) {
                return getUserEmail(response.body().convertDataObj(PeopleInfoData.class));
            }
        });
    }

    private String getUserEmail(PeopleInfoData data) {
        return data.getUserInfo().getUserEmail();
    }

}
