package com.tokopedia.seller.gmsubscribe.view.product.presenter;


import android.util.Log;
import com.tokopedia.seller.gmsubscribe.common.domain.RequestParams;
import com.tokopedia.seller.gmsubscribe.common.presentation.BasePresenter;
import com.tokopedia.seller.gmsubscribe.domain.interactor.GetGMSubscribeCurrentProductUseCase;
import com.tokopedia.seller.gmsubscribe.domain.interactor.GetGMSubscribeExtendProductUseCase;
import com.tokopedia.seller.gmsubscribe.domain.model.product.GMProductDomainModel;
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
