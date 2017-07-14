package com.tokopedia.seller.reputation.view.presenter;

import com.tokopedia.seller.reputation.data.model.request.SellerReputationRequest;
import com.tokopedia.sellerapp.RxSchedulersOverrideRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

/**
 * Created by normansyahputa on 7/9/17.
 */

public class DemoTest {
    @Rule
    public RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

    //    @Mock
//    SellerReputationView sellerReputationView;
//    @Mock
//    ThreadExecutor threadExecutor;
//    @Mock
//    PostExecutionThread postExecutionThread;
//    @Mock
//    ReviewReputationUseCase reviewReputationUseCase;
//    @Mock
//    ShopInfoUseCase shopInfoUseCase;
//    @Mock
//    GCMHandler gcmHandler;
//    @Mock
//    SessionHandler sessionHandler;
    @Mock
    SellerReputationRequest sellerReputationRequest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test() throws Exception {
        assertEquals(4, 2 + 2);
    }
}
