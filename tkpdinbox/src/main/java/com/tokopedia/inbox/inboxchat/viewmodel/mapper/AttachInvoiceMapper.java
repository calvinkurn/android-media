package com.tokopedia.inbox.inboxchat.viewmodel.mapper;

import com.tokopedia.inbox.attachinvoice.view.resultmodel.SelectedInvoice;
import com.tokopedia.inbox.inboxchat.domain.model.reply.Attachment;
import com.tokopedia.inbox.inboxchat.domain.model.reply.AttachmentInvoice;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.WebSocketResponseData;
import com.tokopedia.inbox.inboxchat.helper.AttachmentChatHelper;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachInvoiceSelectionViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachInvoiceSentViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachInvoiceSingleViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Hendri on 28/03/18.
 */

public class AttachInvoiceMapper {
    public static AttachInvoiceSelectionViewModel attachmentToAttachInvoiceSelectionModel(Attachment attachment) {
        if(attachment.getType().equals(AttachmentChatHelper.INVOICE_LIST_ATTACHED)) {
            AttachInvoiceSelectionViewModel invoiceSelectionViewModel = new AttachInvoiceSelectionViewModel(null, null);
            ArrayList<AttachInvoiceSingleViewModel> listSingleInvoice = new ArrayList<>();

            for(AttachmentInvoice invoice:attachment.getAttributes().getInvoices()){
                listSingleInvoice.add(new AttachInvoiceSingleViewModel(
                        invoice.getTypeString(),
                        invoice.getType(),
                        invoice.getAttributes().getCode(),
                        invoice.getAttributes().getCreatedTime(),
                        invoice.getAttributes().getDescription(),
                        invoice.getAttributes().getUrl(),
                        invoice.getAttributes().getId(),
                        invoice.getAttributes().getImageUrl(),
                        invoice.getAttributes().getStatus(),
                        invoice.getAttributes().getStatusId(),
                        invoice.getAttributes().getTitle(),
                        invoice.getAttributes().getAmount()
                ));
            }
            invoiceSelectionViewModel.setList(listSingleInvoice);
            return invoiceSelectionViewModel;
        }
        return null;
    }

    public static AttachInvoiceSelectionViewModel webSocketResponseToAttachInvoiceSelectionModel(WebSocketResponseData webSocketData){

        return new AttachInvoiceSelectionViewModel(null,null);
        /*return new AttachInvoiceSingleViewModel(null,
                0,
                null,
                null,
                null,
                null,
                0,
                null,
                null,
                0,
                null,
                null);
                */
    }

    public static AttachInvoiceSentViewModel myChatViewModelToAttachInvoiceSentModel(MyChatViewModel myChatViewModel){
        return new AttachInvoiceSentViewModel(myChatViewModel);
    }

    public static SelectedInvoice selectedInvoiceViewModelToSelectedInvoice(AttachInvoiceSingleViewModel viewModel){
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
}
