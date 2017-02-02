package com.tokopedia.seller.gmsubscribe.view.product.presenter;


import android.util.Log;
import com.tokopedia.seller.common.domain.RequestParams;
import com.tokopedia.seller.common.presentation.BasePresenter;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetGMSubscribeCurrentProductUseCase;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetGMSubscribeExtendProductUseCase;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GMProductDomainModel;
import com.tokopedia.seller.gmsubscribe.view.product.viewmodel.GMProductViewModel;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;

/**
 * Created by sebastianuskh on 11/23/16.
 */

public class GMProductPresenterImpl extends BasePresenter<GMProductView> implements GMProductPresenter {
    private final GetGMSubscribeCurrentProductUseCase getGMSubscribeCurrentProductUseCase;
    private final GetGMSubscribeExtendProductUseCase getGMSubscribeExtendProductUseCase;

    public GMProductPresenterImpl(GetGMSubscribeCurrentProductUseCase getGMSubscribeCurrentProductUseCase, GetGMSubscribeExtendProductUseCase getGMSubscribeExtendProductUseCase) {
        this.getGMSubscribeCurrentProductUseCase = getGMSubscribeCurrentProductUseCase;
        this.getGMSubscribeExtendProductUseCase = getGMSubscribeExtendProductUseCase;
    }

    @Override
    public void getCurrentPackageSelection() {
        checkViewAttached();
        getGMSubscribeCurrentProductUseCase.execute(RequestParams.EMPTY, new ProductListSubscriber());
    }

    @Override
    public void getExtendPackageSelection() {
        checkViewAttached();
        getGMSubscribeExtendProductUseCase.execute(RequestParams.EMPTY, new ProductListSubscriber());
    }

    @Override
    public void detachView() {
        super.detachView();
        getGMSubscribeCurrentProductUseCase.unsubscribe();
        getGMSubscribeExtendProductUseCase.unsubscribe();
    }

    private class ProductListSubscriber extends Subscriber<List<GMProductDomainModel>> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<GMProductDomainModel> gmProductDomainModels) {
            Log.d(TAG, "Present the data");
            if(isViewAttached()) {
                List<GMProductViewModel> viewModels = new ArrayList<>();
                for (GMProductDomainModel domainModel : gmProductDomainModels) {
                    viewModels.add(new GMProductViewModel(domainModel));
                }
                getView().renderProductList(viewModels);
            }
        }
    }
}
