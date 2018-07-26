package com.tokopedia.session.addchangeemail.data.source;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.ProfileSourceFactory;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.addchangeemail.data.mapper.AddEmailMapper;
import com.tokopedia.session.addchangeemail.data.mapper.CheckEmailMapper;
import com.tokopedia.session.addchangeemail.data.mapper.RequestVerificationMapper;
import com.tokopedia.session.addchangeemail.domain.usecase.AddEmailUseCase;
import com.tokopedia.session.addchangeemail.view.viewmodel.AddEmailViewModel;
import com.tokopedia.session.addchangeemail.view.viewmodel.CheckEmailViewModel;
import com.tokopedia.session.addchangeemail.view.viewmodel.RequestVerificationViewModel;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by yfsx on 09/03/18.
 */

public class AddEmailSource {

    private final AccountsService accountsService;
    private AddEmailMapper addEmailMapper;
    private CheckEmailMapper checkEmailMapper;
    private RequestVerificationMapper requestVerificationMapper;
    private GlobalCacheManager cacheManager;

    public AddEmailSource(AccountsService accountsService,
                          AddEmailMapper addEmailMapper,
                          CheckEmailMapper checkEmailMapper,
                          RequestVerificationMapper requestVerificationMapper,
                          GlobalCacheManager cacheManager) {
        this.accountsService = accountsService;
        this.addEmailMapper = addEmailMapper;
        this.checkEmailMapper = checkEmailMapper;
        this.requestVerificationMapper = requestVerificationMapper;
        this.cacheManager = cacheManager;
    }

    public Observable<AddEmailViewModel> addEmail(RequestParams params) {
        return accountsService.getApi()
                .addEmail(params.getParameters())
                .map(addEmailMapper);
//                .doOnNext(updateCache(params.getString(AddEmailUseCase.PARAM_EMAIL, "")));
    }

    public Observable<CheckEmailViewModel> checkEmail(RequestParams params) {
        return accountsService.getApi().checkEmail(params.getParameters())
                .map(checkEmailMapper);
    }

    public Observable<RequestVerificationViewModel> requestVerification(RequestParams params) {
        return accountsService.getApi().requestVerification(params.getParameters())
                .map(requestVerificationMapper);
    }

    private Action1<AddEmailViewModel> updateCache(final String email) {
        return new Action1<AddEmailViewModel>() {
            @Override
            public void call(AddEmailViewModel addEmailViewModel) {
                if (cacheManager != null) {
                    ProfileModel profileModel = CacheUtil.convertStringToModel(getCache(),
                            new TypeToken<ProfileModel>(){
                            }.getType());
                    profileModel.getProfileData().getUserInfo().setUserEmail(email);
                    cacheManager.setKey(ProfileSourceFactory.KEY_PROFILE_DATA);
                    cacheManager.setValue(CacheUtil.convertModelToString(profileModel,
                            new TypeToken<ProfileModel>() {
                            }.getType()));
                    cacheManager.setCacheDuration(18000);
                    cacheManager.store();
                }
            }
        };
    }
    private String getCache() {
        return cacheManager.getValueString(ProfileSourceFactory.KEY_PROFILE_DATA);
    }
}
