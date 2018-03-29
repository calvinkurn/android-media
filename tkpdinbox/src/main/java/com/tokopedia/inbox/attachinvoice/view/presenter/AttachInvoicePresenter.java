package com.tokopedia.inbox.attachinvoice.view.presenter;

import android.content.Context;

import com.tokopedia.inbox.attachinvoice.domain.usecase.AttachInvoicesUseCase;
import com.tokopedia.inbox.attachinvoice.view.AttachInvoiceContract;
import com.tokopedia.inbox.attachinvoice.view.model.InvoiceViewModel;
import com.tokopedia.inbox.attachinvoice.view.subscriber.AttachInvoicesLoadInvoiceDataSubscriber;
import com.tokopedia.inbox.attachproduct.view.presenter.AttachProductContract;
import com.tokopedia.inbox.attachproduct.view.viewmodel.AttachProductItemViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Hendri on 22/03/18.
 */

public class AttachInvoicePresenter implements AttachInvoiceContract.Presenter {
    private final AttachInvoicesUseCase useCase;
    AttachInvoiceContract.Activity activity;
    AttachInvoiceContract.View view;

    @Inject
    public AttachInvoicePresenter(AttachInvoicesUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public void loadInvoiceData(String query, String userId, int page, int messageId ,Context context) {
        useCase.execute(AttachInvoicesUseCase.createRequestParam(query,userId,page,messageId,context),new AttachInvoicesLoadInvoiceDataSubscriber(view));
    }
//
//    @Override
//    public void updateCheckedList(List<AttachProductItemViewModel> products) {
//
//    }
//
//    @Override
//    public void resetCheckedList() {
//
//    }

    @Override
    public void invoiceSelected(InvoiceViewModel invoiceViewModel) {

    }

    @Override
    public void attachView(AttachInvoiceContract.View view) {
        this.view = view;
    }

    @Override
    public void attachActivityContract(AttachInvoiceContract.Activity activityContract) {
        this.activity = activityContract;
    }
}
