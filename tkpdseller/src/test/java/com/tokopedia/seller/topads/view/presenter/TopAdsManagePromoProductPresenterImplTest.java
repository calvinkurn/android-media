package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.topads.domain.interactor.TopAdsCheckExistGroupUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSearchGroupAdsNameUseCase;
import com.tokopedia.seller.topads.view.listener.TopAdsManagePromoProductView;

import junit.framework.Assert;

import org.junit.Test;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by zulfikarrahman on 2/22/17.
 */
public class TopAdsManagePromoProductPresenterImplTest {

    private TopAdsManagePromoProductPresenterImpl topadsManagement;

    @Test
    void settingUseCase(){
        TopAdsCheckExistGroupUseCase usecase2 = mock(TopAdsCheckExistGroupUseCase.class);
        TopAdsSearchGroupAdsNameUseCase usecase = mock(TopAdsSearchGroupAdsNameUseCase.class);
        when(usecase2.createObservable(any(RequestParams.class))).thenReturn(Observable.just(true));
        topadsManagement = new TopAdsManagePromoProductPresenterImpl(usecase, usecase2);
        TopAdsmanagePromoProductViewTest view = new TopAdsmanagePromoProductViewTest(topadsManagement);
        view.checkIsGroupExist();
        Assert.assertEquals(view.isBool(), true);

    }

    /**
     * Created by zulfikarrahman on 2/22/17.
     */

    public static class TopAdsmanagePromoProductViewTest  implements TopAdsManagePromoProductView {


        private final TopAdsManagePromoProductPresenterImpl topadsManagement;
        private String message;
        private boolean bool = false;

        public TopAdsmanagePromoProductViewTest(TopAdsManagePromoProductPresenterImpl topadsManagement) {
            this.topadsManagement = topadsManagement;
            topadsManagement.attachView(this);
        }

        public boolean isBool() {
            return bool;
        }

        public String getMessage() {
            return message;
        }

        public void checkIsGroupExist(){
            topadsManagement.checkIsGroupExist("Alalalaa");
        }



        @Override
        public void onCheckGroupExistError(String message) {
            this.message = message;
        }

        @Override
        public void onGroupExist() {
            bool =  true;
        }

        @Override
        public void onGroupNotExist() {
            bool =  true;
        }
    }
}