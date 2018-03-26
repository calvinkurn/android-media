package com.tokopedia.inbox.attachinvoice.view;

import android.content.Context;

import com.tokopedia.inbox.attachinvoice.view.model.InvoiceViewModel;
import com.tokopedia.inbox.attachproduct.view.presenter.AttachProductContract;
import com.tokopedia.inbox.attachproduct.view.resultmodel.ResultProduct;
import com.tokopedia.inbox.attachproduct.view.viewmodel.AttachProductItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hendri on 22/03/18.
 */

public interface AttachInvoiceContract {
    interface View {
        void addInvoicesToList(List<InvoiceViewModel> invoices, boolean hasNextPage);
        void hideAllLoadingIndicator();
        void showErrorMessage(Throwable throwable);
        void updateButtonBasedOnChecked(int checkedCount);
    }
    interface Activity {
        String getUserId();
        void finishActivityWithResult(ArrayList<ResultProduct> products);
//        void goToAddProduct(String shopId);
    }
    interface Presenter {
        void loadInvoiceData(String query,String userId, int page, Context context);
//        void updateCheckedList(List<AttachProductItemViewModel> products);
//        void resetCheckedList();
        void invoiceSelected(InvoiceViewModel invoiceViewModel);
        void attachView(AttachInvoiceContract.View view);
        void attachActivityContract(AttachInvoiceContract.Activity activityContract);
    }
}
