package com.tokopedia.inbox.inboxchat.viewmodel.mapper;

import com.tokopedia.inbox.attachinvoice.view.resultmodel.SelectedInvoice;
import com.tokopedia.inbox.inboxchat.domain.model.reply.Attachment;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.WebSocketResponseData;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachInvoiceSelectionViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachInvoiceSentViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachInvoiceSingleViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;

/**
 * Created by Hendri on 28/03/18.
 */

public class AttachInvoiceMapper {
    public static AttachInvoiceSelectionViewModel attachmentToAttachInvoiceSelectionModel(Attachment attachment) {
        return new AttachInvoiceSelectionViewModel(null,null);
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
