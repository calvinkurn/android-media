package com.tokopedia.loyalty.domain.repository;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.loyalty.domain.entity.response.TokoPointDrawerDataResponse;
import com.tokopedia.loyalty.domain.exception.TokoPointDBServiceException;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 07/12/17.
 */

public class TokoPointDBService implements ITokoPointDBService {

    private final GlobalCacheManager globalCacheManager;
    private final Gson gson;

    @Inject
    public TokoPointDBService(GlobalCacheManager globalCacheManager, Gson gson) {
        this.globalCacheManager = globalCacheManager;
        this.gson = gson;
    }

    @Override
    public Observable<TokoPointDrawerDataResponse> getPointDrawer() {
        return Observable.just(TkpdCache.Key.KEY_TOKOPOINT_DRAWER_DATA)
                .map(new Func1<String, TokoPointDrawerDataResponse>() {
                    @Override
                    public TokoPointDrawerDataResponse call(String s) {
                        try {
                            String cacheStr = globalCacheManager.getValueString(s);
                            if (cacheStr != null && !cacheStr.isEmpty()) {
                                TokoPointDrawerDataResponse tokoPointDrawerDataResponse =
                                        gson.fromJson(cacheStr, TokoPointDrawerDataResponse.class);
                                if (tokoPointDrawerDataResponse.getHasNotif() == 1) {
                                    globalCacheManager.delete(TkpdCache.Key.KEY_TOKOPOINT_DRAWER_DATA);
                                    throw new TokoPointDBServiceException("cant pull from db, cause data has notif flag active");
                                }

                                return tokoPointDrawerDataResponse;
                            } else {
                                throw new TokoPointDBServiceException("Cache null or not completed data");
                            }
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                            throw new TokoPointDBServiceException("Cache not found or expired");
                        }

                    }
                });
    }

    @Override
    public Observable<TokoPointDrawerDataResponse> storePointDrawer(
            TokoPointDrawerDataResponse tokoPointDrawerDataResponse
    ) {
        return Observable.just(tokoPointDrawerDataResponse).doOnNext(new Action1<TokoPointDrawerDataResponse>() {
            @Override
            public void call(TokoPointDrawerDataResponse tokoPointDrawerDataResponse) {
                if (tokoPointDrawerDataResponse.getHasNotif() != 1) {
                    globalCacheManager.setCacheDuration(60);
                    globalCacheManager.setKey(TkpdCache.Key.KEY_TOKOPOINT_DRAWER_DATA);
                    globalCacheManager.setValue(gson.toJson(tokoPointDrawerDataResponse));
                    globalCacheManager.store();
                }
            }
        });
//                .map(new Func1<TokoPointDrawerDataResponse, TokoPointDrawerDataResponse>() {
//                    @Override
//                    public TokoPointDrawerDataResponse call(
//                            TokoPointDrawerDataResponse tokoPointDrawerDataResponse
//                    ) {
//                        if (tokoPointDrawerDataResponse.getHasNotif() != 1) {
//                            globalCacheManager.setCacheDuration(60);
//                            globalCacheManager.setKey(TkpdCache.Key.KEY_TOKOPOINT_DRAWER_DATA);
//                            globalCacheManager.setValue(gson.toJson(tokoPointDrawerDataResponse));
//                            globalCacheManager.store();
//                        }
//                        return tokoPointDrawerDataResponse;
//                    }
//                });
    }
}
