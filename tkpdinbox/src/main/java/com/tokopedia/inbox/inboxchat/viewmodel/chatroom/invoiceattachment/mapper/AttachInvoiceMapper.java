package com.tokopedia.inbox.inboxchat.viewmodel.chatroom.invoiceattachment.mapper;

import com.tokopedia.inbox.attachinvoice.view.resultmodel.SelectedInvoice;
import com.tokopedia.inbox.inboxchat.domain.model.ListReplyViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.reply.Attachment;
import com.tokopedia.inbox.inboxchat.domain.model.reply.AttachmentInvoice;
import com.tokopedia.inbox.inboxchat.helper.AttachmentChatHelper;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.invoiceattachment.AttachInvoiceSelectionViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.invoiceattachment.AttachInvoiceSingleViewModel;

import java.util.ArrayList;

/**
 * Created by Hendri on 28/03/18.
 */

public class AttachInvoiceMapper {
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
}
