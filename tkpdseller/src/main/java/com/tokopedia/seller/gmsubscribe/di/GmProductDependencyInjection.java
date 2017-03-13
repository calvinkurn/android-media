package com.tokopedia.seller.gmsubscribe.di;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.seller.gmsubscribe.data.factory.GmSubscribeProductFactory;
import com.tokopedia.seller.gmsubscribe.data.mapper.product.GmSubscribeProductMapper;
import com.tokopedia.seller.gmsubscribe.data.repository.GmSubscribeProductRepositoryImpl;
import com.tokopedia.seller.gmsubscribe.data.source.product.GmSubscribeProductDataSource;
import com.tokopedia.seller.gmsubscribe.data.source.product.cache.GmSubscribeProductCache;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.GmSubscribeProductCloud;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.service.GmSubscribeProductService;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetGmSubscribeCurrentProductUseCase;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetGmSubscribeExtendProductUseCase;
import com.tokopedia.seller.gmsubscribe.view.presenter.GmProductPresenterImpl;

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
