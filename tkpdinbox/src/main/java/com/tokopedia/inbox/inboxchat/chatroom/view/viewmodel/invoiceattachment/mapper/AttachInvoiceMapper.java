package com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.invoiceattachment.mapper;

import com.tokopedia.inbox.attachinvoice.view.resultmodel.SelectedInvoice;
import com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.invoicesent.InvoiceLinkAttributePojo;
import com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.invoicesent.InvoiceLinkPojo;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.invoiceattachment.AttachInvoiceSingleViewModel;

/**
 * Created by Hendri on 28/03/18.
 */

public class AttachInvoiceMapper {
    @Deprecated
    public static SelectedInvoice selectedInvoiceViewModelToSelectedInvoice
            (AttachInvoiceSingleViewModel viewModel) {
        return new SelectedInvoice(viewModel.getId(),
                viewModel.getCode(),
                viewModel.getTypeString(),
                viewModel.getType(),
                viewModel.getTitle(),
                viewModel.getImageUrl(),
                viewModel.getDescription(),
                viewModel.getAmount(),
                viewModel.getCreatedTime(),
                viewModel.getUrl(),
                viewModel.getStatus(),
                viewModel.getStatusId());
    }

    public static InvoiceLinkPojo invoiceViewModelToDomainInvoicePojo(AttachInvoiceSingleViewModel
                                                                              selectedInvoice) {
        InvoiceLinkAttributePojo invoiceLinkAttributePojo = new InvoiceLinkAttributePojo();
        invoiceLinkAttributePojo.setCode(selectedInvoice.getCode());
        invoiceLinkAttributePojo.setCreateTime(selectedInvoice.getCreatedTime());
        invoiceLinkAttributePojo.setDescription(selectedInvoice.getDescription());
        invoiceLinkAttributePojo.setHrefUrl(selectedInvoice.getUrl());
        invoiceLinkAttributePojo.setId(selectedInvoice.getId());
        invoiceLinkAttributePojo.setImageUrl(selectedInvoice.getImageUrl());
        invoiceLinkAttributePojo.setStatus(selectedInvoice.getStatus());
        invoiceLinkAttributePojo.setStatusId(selectedInvoice.getStatusId());
        invoiceLinkAttributePojo.setTitle(selectedInvoice.getTitle());
        invoiceLinkAttributePojo.setTotalAmount(selectedInvoice.getAmount());

        InvoiceLinkPojo invoiceLinkPojo = new InvoiceLinkPojo();
        invoiceLinkPojo.setType(selectedInvoice.getTypeString());
        invoiceLinkPojo.setTypeId(selectedInvoice.getType());
        invoiceLinkPojo.setAttributes(invoiceLinkAttributePojo);
        return invoiceLinkPojo;
    }

    public static InvoiceLinkPojo convertInvoiceToDomainInvoiceModel(SelectedInvoice selectedInvoice){
        InvoiceLinkAttributePojo invoiceLinkAttributePojo = new InvoiceLinkAttributePojo();
        invoiceLinkAttributePojo.setCode(selectedInvoice.getInvoiceNo());
        invoiceLinkAttributePojo.setCreateTime(selectedInvoice.getDate());
        invoiceLinkAttributePojo.setDescription(selectedInvoice.getDescription());
        invoiceLinkAttributePojo.setHrefUrl(selectedInvoice.getInvoiceUrl());
        invoiceLinkAttributePojo.setId(selectedInvoice.getInvoiceId());
        invoiceLinkAttributePojo.setImageUrl(selectedInvoice.getTopProductImage());
        invoiceLinkAttributePojo.setStatus(selectedInvoice.getStatus());
        invoiceLinkAttributePojo.setStatusId(selectedInvoice.getStatusId());
        invoiceLinkAttributePojo.setTitle(selectedInvoice.getTopProductName());
        invoiceLinkAttributePojo.setTotalAmount(selectedInvoice.getAmount());

        InvoiceLinkPojo invoiceLinkPojo = new InvoiceLinkPojo();
        invoiceLinkPojo.setType(selectedInvoice.getInvoiceTypeStr());
        invoiceLinkPojo.setTypeId(selectedInvoice.getInvoiceType());
        invoiceLinkPojo.setAttributes(invoiceLinkAttributePojo);
        return invoiceLinkPojo;
    }
}
