package com.tokopedia.gm.subscribe.di;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.gm.subscribe.data.factory.GmSubscribeProductFactory;
import com.tokopedia.gm.subscribe.data.mapper.product.GmSubscribeProductMapper;
import com.tokopedia.gm.subscribe.data.repository.GmSubscribeProductRepositoryImpl;
import com.tokopedia.gm.subscribe.data.source.product.GmSubscribeProductDataSource;
import com.tokopedia.gm.subscribe.data.source.product.cache.GmSubscribeProductCache;
import com.tokopedia.gm.subscribe.data.source.product.cloud.GmSubscribeProductCloud;
import com.tokopedia.gm.subscribe.data.source.product.cloud.service.GmSubscribeProductService;
import com.tokopedia.gm.subscribe.domain.product.interactor.ClearGmSubscribeProductCacheUseCase;
import com.tokopedia.gm.subscribe.view.presenter.GmHomePresenterImpl;

/**
 * Created by sebastianuskh on 2/9/17.
 */
public class GmHomeDependencyInjection {
    public static GmHomePresenterImpl getPresenter() {
        JobExecutor threadExecutor = new JobExecutor();
        UIThread postExecutionThread = new UIThread();

        GmSubscribeProductService gmSubscribeProductService = new GmSubscribeProductService();
        GmSubscribeProductCloud gmSubscribeProductCloud = new GmSubscribeProductCloud(gmSubscribeProductService.getApi());

        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
        GmSubscribeProductCache gmSubscribeProductCache = new GmSubscribeProductCache(globalCacheManager);

        GmSubscribeProductMapper gmSubscribeProductMapper = new GmSubscribeProductMapper();
        GmSubscribeProductDataSource gmSubscribeProductDataSource = new GmSubscribeProductDataSource(gmSubscribeProductCache, gmSubscribeProductCloud, gmSubscribeProductMapper);
        GmSubscribeProductFactory gmSubscribeProductFactory = new GmSubscribeProductFactory(gmSubscribeProductDataSource);
        GmSubscribeProductRepositoryImpl gmSubscribeProductReposistory = new GmSubscribeProductRepositoryImpl(gmSubscribeProductFactory);

        ClearGmSubscribeProductCacheUseCase clearGMSubscribeProductCache = new ClearGmSubscribeProductCacheUseCase(threadExecutor, postExecutionThread, gmSubscribeProductReposistory);

        return new GmHomePresenterImpl(clearGMSubscribeProductCache);
    }
}
