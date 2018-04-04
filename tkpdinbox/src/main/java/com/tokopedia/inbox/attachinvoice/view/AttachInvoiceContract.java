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
    }
    interface Activity {
        String getUserId();
        int getMessageId();
    }
    interface Presenter {
        void loadInvoiceData(String query,String userId, int page, int messageId ,Context context);
        void attachView(AttachInvoiceContract.View view);
        void attachActivityContract(AttachInvoiceContract.Activity activityContract);
    }
}
