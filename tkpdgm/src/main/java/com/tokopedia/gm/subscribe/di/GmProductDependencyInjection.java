package com.tokopedia.gm.subscribe.di;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.gm.subscribe.data.factory.GmSubscribeProductFactory;
import com.tokopedia.gm.subscribe.data.mapper.product.GmSubscribeProductMapper;
import com.tokopedia.gm.subscribe.data.repository.GmSubscribeProductRepositoryImpl;
import com.tokopedia.gm.subscribe.data.source.product.GmSubscribeProductDataSource;
import com.tokopedia.gm.subscribe.data.source.product.cache.GmSubscribeProductCache;
import com.tokopedia.gm.subscribe.data.source.product.cloud.GmSubscribeProductCloud;
import com.tokopedia.gm.subscribe.data.source.product.cloud.service.GmSubscribeProductService;
import com.tokopedia.gm.subscribe.domain.product.interactor.GetGmSubscribeCurrentProductUseCase;
import com.tokopedia.gm.subscribe.domain.product.interactor.GetGmSubscribeExtendProductUseCase;
import com.tokopedia.gm.subscribe.view.presenter.GmProductPresenterImpl;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public class GmProductDependencyInjection {

    public static GmProductPresenterImpl getPresenter() {
        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
        GmSubscribeProductCache gmSubscribeProductCache = new GmSubscribeProductCache(globalCacheManager);

        GmSubscribeProductService gmSubscribeProductService = new GmSubscribeProductService();
        GmSubscribeProductCloud gmSubscribeProductCloud = new GmSubscribeProductCloud(gmSubscribeProductService.getApi());
        GmSubscribeProductMapper gmSubscribeProductMapper = new GmSubscribeProductMapper();
        GmSubscribeProductDataSource gmSubscribeProductDataSource = new GmSubscribeProductDataSource(gmSubscribeProductCache, gmSubscribeProductCloud, gmSubscribeProductMapper);
        GmSubscribeProductFactory gmSubscribeProductFactory = new GmSubscribeProductFactory(gmSubscribeProductDataSource);
        GmSubscribeProductRepositoryImpl gmSubscribeProductRepository = new GmSubscribeProductRepositoryImpl(gmSubscribeProductFactory);
        ThreadExecutor threadExecutor = new JobExecutor();
        PostExecutionThread postExecutionThread = new UIThread();
        GetGmSubscribeCurrentProductUseCase getGmSubscribeCurrentProductUseCase = new GetGmSubscribeCurrentProductUseCase(threadExecutor, postExecutionThread, gmSubscribeProductRepository);
        GetGmSubscribeExtendProductUseCase getGmSubscribeExtendProductUseCase = new GetGmSubscribeExtendProductUseCase(threadExecutor, postExecutionThread, gmSubscribeProductRepository);
        return new GmProductPresenterImpl(getGmSubscribeCurrentProductUseCase, getGmSubscribeExtendProductUseCase);
    }
}
