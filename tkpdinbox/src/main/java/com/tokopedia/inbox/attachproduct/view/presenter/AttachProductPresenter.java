package com.tokopedia.inbox.attachproduct.view.presenter;

import com.tokopedia.core.util.getproducturlutil.model.GetProductPass;
import com.tokopedia.inbox.attachproduct.domain.usecase.AttachProductUseCase;
import com.tokopedia.inbox.attachproduct.view.resultmodel.ResultProduct;
import com.tokopedia.inbox.attachproduct.view.subscriber.AttachProductGetProductListSubscriber;
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

public class AttachProductPresenter implements AttachProductContract.Presenter {
    private final AttachProductUseCase useCase;
    private AttachProductContract.View view;
    private AttachProductContract.Activity activityContract;
    private List<AttachProductItemViewModel> checkedList;

    @Inject
    public AttachProductPresenter(AttachProductUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public void attachView(AttachProductContract.View view) {
        this.view = view;
    }

    @Override
    public void attachActivityContract(AttachProductContract.Activity activityContract) {
        this.activityContract = activityContract;
    }

    @Override
    public void loadProductData(String query, String shopId, int page) {
        useCase.execute(AttachProductUseCase.createRequestParams(query, shopId, page),
                new AttachProductGetProductListSubscriber(view));
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
