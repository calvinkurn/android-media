package com.tokopedia.inbox.attachproduct.view.presenter;

import com.tokopedia.inbox.attachproduct.view.resultmodel.ResultProduct;
import com.tokopedia.inbox.attachproduct.view.viewmodel.AttachProductItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hendri on 19/02/18.
 */

public interface AttachProductContract {
    interface View {
        void addProductToList(List<AttachProductItemViewModel> products, boolean hasNextPage);
        void hideAllLoadingIndicator();
        void showErrorMessage(Throwable throwable);
        void updateButtonBasedOnChecked(int checkedCount);
    }
    interface Activity {
        boolean isSeller();
        String getShopId();
        void finishActivityWithResult(ArrayList<ResultProduct> products);
        void goToAddProduct(String shopId);
    }
    interface Presenter {
        void loadProductData(String query,String shopId, int page);
        void updateCheckedList(List<AttachProductItemViewModel> products);
        void resetCheckedList();
        void completeSelection();
        void attachView(AttachProductContract.View view);
        void attachActivityContract(AttachProductContract.Activity activityContract);
    }
}
