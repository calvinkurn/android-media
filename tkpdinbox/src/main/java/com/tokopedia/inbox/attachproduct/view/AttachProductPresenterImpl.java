package com.tokopedia.inbox.attachproduct.view;

import com.tokopedia.core.network.apiservices.ace.AceSearchService;
import com.tokopedia.core.util.getproducturlutil.model.GetProductPass;
import com.tokopedia.inbox.attachproduct.data.repository.AttachProductRepository;
import com.tokopedia.inbox.attachproduct.data.repository.AttachProductRepositoryImpl;
import com.tokopedia.inbox.attachproduct.data.source.service.GetShopProductService;
import com.tokopedia.inbox.attachproduct.domain.usecase.AttachProductUseCase;
import com.tokopedia.inbox.attachproduct.view.resultmodel.ResultProduct;
import com.tokopedia.inbox.attachproduct.view.viewmodel.AttachProductItemViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Hendri on 19/02/18.
 */

public class AttachProductPresenterImpl implements AttachProductPresenter.Presenter {
    private final AttachProductUseCase useCase;
    AttachProductPresenter.View view;
    AttachProductPresenter.Activity activityContract;
    List<AttachProductItemViewModel> checkedList;

    @Inject
    public AttachProductPresenterImpl(AttachProductUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public void attachView(AttachProductPresenter.View view) {
        this.view = view;
    }

    @Override
    public void attachActivityContract(AttachProductPresenter.Activity activityContract) {
        this.activityContract = activityContract;
    }

    @Override
    public void loadProductData(String query, String shopId, int page) {
        useCase.getProductList(query,shopId,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<AttachProductItemViewModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        view.showErrorMessage(throwable);
                    }

                    @Override
                    public void onNext(List<AttachProductItemViewModel> attachProductItemViewModels) {
                        view.hideAllLoadingIndicator();
                        view.addProductToList(attachProductItemViewModels,(attachProductItemViewModels.size() >= Integer.parseInt(GetProductPass.DEFAULT_ROWS)));
                    }
                });
    }

    @Override
    public void updateCheckedList(List<AttachProductItemViewModel> products) {
        if (checkedList == null)
            checkedList = new ArrayList<>();
        resetCheckedList();
        checkedList.addAll(products);
        view.updateButtonBasedOnChecked(checkedList.size());
    }

    @Override
    public void resetCheckedList() {
        checkedList.removeAll(checkedList);
    }

    @Override
    public void completeSelection() {
        ArrayList<ResultProduct> resultProducts = new ArrayList<>();
        for(AttachProductItemViewModel product:checkedList){
            resultProducts.add(new ResultProduct(product));
        }
        activityContract.finishActivityWithResult(resultProducts);
    }
}
