package com.tokopedia.session.changename.data.source;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.ProfileSourceFactory;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.changename.data.mapper.ChangeNameMapper;
import com.tokopedia.session.changename.domain.usecase.ChangeNameUseCase;
import com.tokopedia.session.changename.view.viewmodel.ChangeNameViewModel;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by yfsx on 22/03/18.
 */

public class ChangeNameSource {

    private final AccountsService accountsService;
    private ChangeNameMapper changeNameMapper;
    private GlobalCacheManager cacheManager;

    public ChangeNameSource(AccountsService accountsService, ChangeNameMapper changeNameMapper, GlobalCacheManager cacheManager) {
        this.accountsService = accountsService;
        this.changeNameMapper = changeNameMapper;
        this.cacheManager = cacheManager;
    }

    public Observable<ChangeNameViewModel> changeName(RequestParams params) {
        return accountsService.getApi()
                .changeName(params.getParameters())
                .map(changeNameMapper);
//                .doOnNext(updateCache(params.getString(ChangeNameUseCase.PARAM_NAME, "")));
    }

    private Action1<ChangeNameViewModel> updateCache(final String name) {
        return new Action1<ChangeNameViewModel>() {
            @Override
            public void call(ChangeNameViewModel changeNameViewModel) {
                if (cacheManager != null) {
                   ProfileModel profileModel = CacheUtil.convertStringToModel(getCache(),
                           new TypeToken<ProfileModel>(){
                           }.getType());
                   profileModel.getProfileData().getUserInfo().setUserName(name);
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
