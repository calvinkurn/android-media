package com.tokopedia.inbox.attachproduct.di;

import com.tokopedia.core.network.apiservices.ace.AceSearchService;
import com.tokopedia.core.shopinfo.facades.authservices.ShopService;
import com.tokopedia.inbox.attachproduct.data.repository.AttachProductRepository;
import com.tokopedia.inbox.attachproduct.data.repository.AttachProductRepositoryImpl;
import com.tokopedia.inbox.attachproduct.data.source.service.GetShopProductService;
import com.tokopedia.inbox.attachproduct.domain.usecase.AttachProductUseCase;
import com.tokopedia.inbox.attachproduct.view.AttachProductPresenter;
import com.tokopedia.inbox.attachproduct.view.AttachProductPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Hendri on 19/02/18.
 */

@Module
public class AttachProductModule {

    @Provides
    public static GetShopProductService provideShopService(){
        return new GetShopProductService();
    }

    @Provides
    public static AttachProductRepository provideAttachProductRepository(GetShopProductService shopService){
        return new AttachProductRepositoryImpl(shopService);
    }

    @Provides
    public static AttachProductUseCase provideAttachProductUseCase(AttachProductRepository repository){
        return new AttachProductUseCase(repository);
    }

    @Provides
    public static AttachProductPresenter.Presenter provideProductPresenter(AttachProductUseCase useCase){
        return new AttachProductPresenterImpl(useCase);
    }
}
