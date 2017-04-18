package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.domain.interactor.TopAdsCheckExistGroupUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSearchGroupAdsNameUseCase;
import com.tokopedia.seller.topads.view.listener.TopAdsManageGroupPromoView;

import junit.framework.Assert;

import org.junit.Test;

import java.util.List;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by zulfikarrahman on 2/22/17.
 */
public class TopAdsManageGroupPromoPresenterImplTest {

    private TopAdsManageGroupPromoPresenterImpl topadsManagement;

    @Test
    void settingUseCase(){
        TopAdsCheckExistGroupUseCase usecase2 = mock(TopAdsCheckExistGroupUseCase.class);
        TopAdsSearchGroupAdsNameUseCase usecase = mock(TopAdsSearchGroupAdsNameUseCase.class);
        when(usecase2.createObservable(any(RequestParams.class))).thenReturn(Observable.just(true));
        topadsManagement = new TopAdsManageGroupPromoPresenterImpl(usecase, usecase2);
        TopAdsmanageGroupPromoViewTest view = new TopAdsmanageGroupPromoViewTest(topadsManagement);
        view.checkIsGroupExist();
        Assert.assertEquals(view.isBool(), true);

    }

    /**
     * Created by zulfikarrahman on 2/22/17.
     */

    public static class TopAdsmanageGroupPromoViewTest implements TopAdsManageGroupPromoView {


        private final TopAdsManageGroupPromoPresenterImpl topadsManagement;
        private String message;
        private boolean bool = false;

        public TopAdsmanageGroupPromoViewTest(TopAdsManageGroupPromoPresenterImpl topadsManagement) {
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
        public void onCheckGroupExistError() {
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

        @Override
        public void onGetGroupAdList(List<GroupAd> groupAds) {

        }

        @Override
        public void onGetGroupAdListError() {

        }

        @Override
        public void showLoading() {

        }

        @Override
        public void showErrorShouldFillGroupName() {

        }

        @Override
        public void dismissLoading() {

        }

        @Override
        public void onGroupNotExistOnSubmitNewGroup() {

        }
    }
}