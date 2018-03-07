package com.tokopedia.inbox;

import com.tokopedia.core.network.apiservices.ace.AceSearchService;
import com.tokopedia.core.network.apiservices.shop.ShopService;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.inbox.attachproduct.data.repository.AttachProductRepository;
import com.tokopedia.inbox.attachproduct.data.repository.AttachProductRepositoryImpl;
import com.tokopedia.inbox.attachproduct.data.source.service.GetShopProductService;
import com.tokopedia.inbox.attachproduct.domain.usecase.AttachProductUseCase;
import com.tokopedia.inbox.attachproduct.view.viewmodel.AttachProductItemViewModel;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Observer;


/**
 * Created by Hendri on 14/02/18.
 */
@RunWith(JUnit4.class)
public class AttachProductAPICallTest {

    class MockAceSearchService extends AceSearchService {
        @Override
        protected void initApiService(Retrofit retrofit) {
            Retrofit retrofit2 = RetrofitFactory.createRetrofitDefaultConfig(retrofit.baseUrl().toString()).build();
            super.initApiService(retrofit2);
        }
    }

    @Test
    public void testUseCase() throws Exception{
        AceSearchService aceSearchService = new MockAceSearchService();
        AttachProductRepository attachProductRepository = new AttachProductRepositoryImpl(new GetShopProductService());
        AttachProductUseCase attachProductUseCase = new AttachProductUseCase(attachProductRepository);
        Observable<List<AttachProductItemViewModel>> obs = attachProductUseCase.getProductList("","864231",0);
        obs.subscribe(new Observer<List<AttachProductItemViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                Assert.assertNotNull(throwable);
            }

            @Override
            public void onNext(List<AttachProductItemViewModel> attachProductItemViewModels) {
                Assert.assertNotNull(attachProductItemViewModels);
            }
        });
    }
}
