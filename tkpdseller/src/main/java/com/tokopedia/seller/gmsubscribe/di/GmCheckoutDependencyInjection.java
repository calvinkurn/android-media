package com.tokopedia.seller.gmsubscribe.di;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.seller.gmsubscribe.data.factory.GmSubscribeCartFactory;
import com.tokopedia.seller.gmsubscribe.data.factory.GmSubscribeProductFactory;
import com.tokopedia.seller.gmsubscribe.data.mapper.GmSubscribeCheckoutMapper;
import com.tokopedia.seller.gmsubscribe.data.mapper.cart.GmSubscribeVoucherMapper;
import com.tokopedia.seller.gmsubscribe.data.mapper.product.GmSubscribeProductMapper;
import com.tokopedia.seller.gmsubscribe.data.repository.GmSubscribeCartRepositoryImpl;
import com.tokopedia.seller.gmsubscribe.data.repository.GmSubscribeProductRepositoryImpl;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.GmSubscribeCartCloud;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.service.GmSubscribeCartService;
import com.tokopedia.seller.gmsubscribe.data.source.product.GmSubscribeProductDataSource;
import com.tokopedia.seller.gmsubscribe.data.source.product.cache.GmSubscribeProductCache;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.GmSubscribeProductCloud;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.service.GmSubscribeProductService;
import com.tokopedia.seller.gmsubscribe.domain.cart.interactor.CheckGmSubscribeVoucherUseCase;
import com.tokopedia.seller.gmsubscribe.domain.cart.interactor.CheckoutGmSubscribeUseCase;
import com.tokopedia.seller.gmsubscribe.domain.cart.interactor.CheckoutGmSubscribeWithVoucherCheckUseCase;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetGmAutoSubscribeSelectedProductUseCase;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetGmCurrentSelectedProductUseCase;
import com.tokopedia.seller.gmsubscribe.view.presenter.GmCheckoutPresenterImpl;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GmCheckoutDependencyInjection {
    public static GmCheckoutPresenterImpl createPresenter() {
        JobExecutor threadExecutor = new JobExecutor();
        UIThread postExecutionThread = new UIThread();


        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
        GmSubscribeProductCache gmSubscribeProductCache = new GmSubscribeProductCache(globalCacheManager);

        GmSubscribeProductService gmSubscribeProductService = new GmSubscribeProductService();
        GmSubscribeProductCloud gmSubscribeProductCloud = new GmSubscribeProductCloud(gmSubscribeProductService.getApi());

        GmSubscribeCartService gmSubscribeCartService = new GmSubscribeCartService();
        GmSubscribeCartCloud cartCloud = new GmSubscribeCartCloud(gmSubscribeCartService.getApi());

        GmSubscribeProductMapper gmSubscribeProductMapper = new GmSubscribeProductMapper();
        GmSubscribeVoucherMapper voucherMapper = new GmSubscribeVoucherMapper();
        GmSubscribeCheckoutMapper checkoutMapper = new GmSubscribeCheckoutMapper();

        GmSubscribeProductDataSource gmSubscribeProductDataSource = new GmSubscribeProductDataSource(gmSubscribeProductCache, gmSubscribeProductCloud, gmSubscribeProductMapper);
        GmSubscribeProductFactory gmSubscribeProductFactory = new GmSubscribeProductFactory(gmSubscribeProductDataSource);
        GmSubscribeCartFactory gmSubscribeCartFactory = new GmSubscribeCartFactory(cartCloud, voucherMapper, checkoutMapper);
        GmSubscribeCartRepositoryImpl gmSubscribeCartRepository = new GmSubscribeCartRepositoryImpl(gmSubscribeCartFactory);
        GmSubscribeProductRepositoryImpl gmSubscribeProductRepository = new GmSubscribeProductRepositoryImpl(gmSubscribeProductFactory);

        GetGmCurrentSelectedProductUseCase getCurrentSelectedProduct = new GetGmCurrentSelectedProductUseCase(threadExecutor, postExecutionThread, gmSubscribeProductRepository);
        GetGmAutoSubscribeSelectedProductUseCase getGmAutoSubscribeSelectedProductUseCase = new GetGmAutoSubscribeSelectedProductUseCase(threadExecutor, postExecutionThread, gmSubscribeProductRepository);
        CheckGmSubscribeVoucherUseCase checkGmSubscribeVoucherUseCase = new CheckGmSubscribeVoucherUseCase(threadExecutor, postExecutionThread, gmSubscribeCartRepository);
        CheckoutGmSubscribeUseCase checkoutGmSubscribeUseCase = new CheckoutGmSubscribeUseCase(threadExecutor, postExecutionThread, gmSubscribeCartRepository);
        CheckoutGmSubscribeWithVoucherCheckUseCase checkoutGMSubscribeWithVoucherCheckUseCase = new CheckoutGmSubscribeWithVoucherCheckUseCase(threadExecutor, postExecutionThread, gmSubscribeCartRepository);

        return new GmCheckoutPresenterImpl(getCurrentSelectedProduct, getGmAutoSubscribeSelectedProductUseCase, checkGmSubscribeVoucherUseCase, checkoutGmSubscribeUseCase, checkoutGMSubscribeWithVoucherCheckUseCase);
    }

}
