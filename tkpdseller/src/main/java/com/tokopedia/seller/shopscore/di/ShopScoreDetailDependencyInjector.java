package com.tokopedia.seller.shopscore.di;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.goldmerchant.GoldMerchantService;
import com.tokopedia.core.network.apiservices.goldmerchant.apis.GoldMerchantApi;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.shopscore.data.factory.ShopScoreFactory;
import com.tokopedia.seller.shopscore.data.mapper.ShopScoreDetailMapper;
import com.tokopedia.seller.shopscore.data.repository.ShopScoreRepositoryImpl;
import com.tokopedia.seller.shopscore.data.source.cloud.ShopScoreCloud;
import com.tokopedia.seller.shopscore.data.source.disk.ShopScoreCache;
import com.tokopedia.seller.shopscore.domain.interactor.GetShopScoreDetailUseCase;
import com.tokopedia.seller.shopscore.view.presenter.ShopScoreDetailPresenterImpl;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailDependencyInjector {
    public static ShopScoreDetailPresenterImpl getPresenter(Context context) {
        JobExecutor threadExecutor = new JobExecutor();
        UIThread postExecutionThread = new UIThread();

        GoldMerchantService service = new GoldMerchantService();
        GoldMerchantApi api = service.getApi();
        SessionHandler sessionHandler = new SessionHandler(context);
        ShopScoreCloud shopScoreCloud = new ShopScoreCloud(api, sessionHandler);
        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
        ShopScoreCache shopScoreCache = new ShopScoreCache(globalCacheManager);
        ShopScoreDetailMapper shopScoreDetailMapper = new ShopScoreDetailMapper(context);
        ShopScoreFactory shopScoreFactory =
                new ShopScoreFactory(shopScoreCloud, shopScoreCache, shopScoreDetailMapper);
        ShopScoreRepositoryImpl shopScoreRepository = new ShopScoreRepositoryImpl(shopScoreFactory);
        GetShopScoreDetailUseCase getShopScoreDetailUseCase =
                new GetShopScoreDetailUseCase(
                        threadExecutor, postExecutionThread, shopScoreRepository
                );
        return new ShopScoreDetailPresenterImpl(getShopScoreDetailUseCase);
    }
}
