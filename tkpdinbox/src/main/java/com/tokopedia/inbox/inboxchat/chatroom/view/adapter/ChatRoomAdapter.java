package com.tokopedia.inbox.inboxchat.chatroom.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.chatroom.view.adapter.viewholder.chatbot.AttachedInvoiceSentViewHolder;
import com.tokopedia.inbox.inboxchat.chatroom.view.adapter.viewholder.chatroom.ImageAnnouncementViewHolder;
import com.tokopedia.inbox.inboxchat.chatroom.view.adapter.viewholder.chatroom.ProductAttachmentViewHolder;
import com.tokopedia.inbox.inboxchat.chatroom.view.adapter.viewholder.common.ImageUploadViewHolder;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.BaseChatViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.ReplyParcelableModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.SendableViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.TimeMachineChatModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.TypingChatModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.imageupload.ImageUploadViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.productattachment.ProductAttachmentViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.rating.ChatRatingViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class ChatRoomAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private final ChatRoomTypeFactory typeFactory;
    private List<Visitable> list;
    private EmptyModel emptyModel;
    private LoadingMoreModel loadingModel;
    private TimeMachineChatModel timeMachineChatModel;
    private TypingChatModel typingModel;

    public ChatRoomAdapter(ChatRoomTypeFactory typeFactory) {
        this.list = new ArrayList<>();
        this.typeFactory = typeFactory;
        this.emptyModel = new EmptyModel();
        this.loadingModel = new LoadingMoreModel();
        this.timeMachineChatModel = new TimeMachineChatModel("");
        this.typingModel = new TypingChatModel();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        if (list.get(position) instanceof BaseChatViewModel) {
            showTimeBaseChat(holder.itemView.getContext(), holder.getAdapterPosition());
        }
        holder.bind(list.get(holder.getAdapterPosition()));
    }

    @Override
    public void onViewRecycled(AbstractViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof ImageUploadViewHolder) {
            ((ImageUploadViewHolder) holder).onViewRecycled();
        } else if (holder instanceof ImageAnnouncementViewHolder) {
            ((ImageAnnouncementViewHolder) holder).onViewRecycled();
        } else if (holder instanceof ProductAttachmentViewHolder) {
            ((ProductAttachmentViewHolder) holder).onViewRecycled();
        } else if (holder instanceof AttachedInvoiceSentViewHolder) {
            ((AttachedInvoiceSentViewHolder) holder).onViewRecycled();
        }
    }

    private void showTimeBaseChat(Context context, int position) {
        if (position != list.size() - 1) {
            try {
                BaseChatViewModel now = (BaseChatViewModel) list.get(position);
                long myTime = Long.parseLong(now.getReplyTime());
                long prevTime = 0;

                if (list.get(position + 1) != null && list.get(position + 1) instanceof BaseChatViewModel) {
                    BaseChatViewModel prev = (BaseChatViewModel) list.get(position + 1);
                    prevTime = Long.parseLong(prev.getReplyTime());
                }

                if (compareTime(context, myTime, prevTime)) {
                    ((BaseChatViewModel) list.get(position)).setShowTime(false);
                } else {
                    ((BaseChatViewModel) list.get(position)).setShowTime(true);
                }
            } catch (NumberFormatException | ClassCastException e) {
                ((BaseChatViewModel) list.get(position)).setShowTime(false);
            }
        } else {
            try {
                ((BaseChatViewModel) list.get(position)).setShowTime(true);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean compareTime(Context context, long calCurrent, long calBefore) {
        return DateFormat.getLongDateFormat(context).format(new Date(calCurrent))
                .equals(DateFormat.getLongDateFormat(context).format(new Date(calBefore)));
    }

    private boolean compareHour(Context context, String calCurrent, String calBefore) {
        return calCurrent.equals(calBefore);
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<Visitable> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addList(List<Visitable> newItems) {
        int positionStart = this.list.size();
        this.list.addAll(newItems);
        notifyItemRangeInserted(positionStart, newItems.size());
        notifyItemRangeChanged(positionStart - 10, 10);
    }

    public void showEmpty() {
        this.list.add(emptyModel);
    }

    public void removeEmpty() {
        this.list.remove(emptyModel);
    }

    public void clearData() {
        this.list.clear();
    }

    public List<Visitable> getList() {
        return list;
    }

    public void setNav(String string) {

    }

    public void removeLastProductWithId(Integer productId) {
        if ((list != null && !list.isEmpty())) {
            ListIterator<Visitable> iterator = list.listIterator(list.size());
            while (iterator.hasPrevious()) {
                int position = iterator.previousIndex();
                Visitable visitable = iterator.previous();
                ProductAttachmentViewModel attachment = getProductAttachmentFromVisitable(visitable);
                if (isAttachmentMatched(attachment, productId)) {
                    iterator.remove();
                    notifyItemRemoved(position);
                }
            }
        }
    }

    public void removeLastMessageWithStartTime(String startTime) {
        if ((list != null && !list.isEmpty())) {
            ListIterator<Visitable> iterator = list.listIterator(list.size());
            while (iterator.hasPrevious()) {
                int position = iterator.previousIndex();
                Visitable visitable = iterator.previous();
                if (visitable instanceof SendableViewModel
                        && ((SendableViewModel) visitable).getStartTime().equals(startTime)) {
                    iterator.remove();
                    notifyItemRemoved(position);
                }
            }
        }
    }

    private boolean isAttachmentMatched(ProductAttachmentViewModel attachment, Integer productId) {
        if (attachment != null) {
            if (attachment.getProductId().toString().equals(productId.toString())) {
                return true;
            }
        }
        return false;
    }

    private ProductAttachmentViewModel getProductAttachmentFromVisitable(Visitable visitable) {
        if ((visitable instanceof ProductAttachmentViewModel)) {
            ProductAttachmentViewModel viewModel = (ProductAttachmentViewModel) visitable;
            return viewModel;
        }
        return null;
    }

    public void removeLast() {
        if (list != null && !list.isEmpty()) {
            list.remove(0);
            notifyItemRemoved(0);
        }
    }

    public void remove(Visitable model) {
        int position = list.indexOf(model);
        list.remove(model);
        notifyItemRemoved(position);
    }

    public void addReply(Visitable item) {
        this.list.add(0, item);
        notifyItemInserted(0);
//        notifyItemRangeChanged(0, 2);
    }

    public void addReply(List<ImageUploadViewModel> list) {
        for (int i = 0; i < list.size(); i++) {
            this.list.add(0, list.get(i));
            notifyItemInserted(0);
//            notifyItemRangeChanged(0, 2);
        }
    }

    public void showLoading() {
        this.list.add(list.size(), loadingModel);
        notifyItemInserted(list.size());
    }

    public void removeLoading() {
        int index = list.indexOf(loadingModel);
        this.list.remove(loadingModel);
        notifyItemRemoved(index);
    }

    public void showTyping() {
        this.list.add(0, typingModel);
        notifyItemInserted(0);
    }

    public void removeTyping() {
        this.list.remove(typingModel);
        notifyItemRemoved(0);
    }


    public boolean checkLoadMore(int index) {
        if (index == getItemCount() - 1) {
            return (list.get(index) instanceof LoadingMoreModel);
        }
        return false;
    }

    public ReplyParcelableModel getLastItem() {
        if (list.size() > 0 && list.get(0) instanceof BaseChatViewModel) {
            BaseChatViewModel viewModel = (BaseChatViewModel) list.get(0);
            return new ReplyParcelableModel(String.valueOf(viewModel.getMessageId()), viewModel
                    .getMessage(), viewModel.getReplyTime());
        } else if (list.size() > 1 && list.get(1) instanceof BaseChatViewModel) {
            BaseChatViewModel viewModel = (BaseChatViewModel) list.get(1);
            return new ReplyParcelableModel(String.valueOf(viewModel.getMessageId()), viewModel
                    .getMessage(), viewModel.getReplyTime());
        } else {
            return null;
        }
    }

    public void showTimeMachine() {
        this.list.add(timeMachineChatModel);
        notifyItemInserted(list.size());
    }

    public void setReadStatus() {
        for (int i = 0; i < list.size(); i++) {
            Visitable currentItem = list.get(i);
            if (currentItem instanceof SendableViewModel) {
                if (((SendableViewModel) list.get(i)).isRead()) {
                    break;
                } else {
                    ((SendableViewModel) list.get(i)).setIsRead(true);
                    notifyItemRangeChanged(i, 1);
                }
            }
        }

    }

    public boolean isTyping() {
        return list.contains(typingModel);
    }

    public void showRetryFor(ImageUploadViewModel model, boolean b) {
        int position = list.indexOf(model);
        if (position >= 0 && list.get(position) instanceof ImageUploadViewModel) {
            ((ImageUploadViewModel) list.get(position)).setRetry(true);
            notifyItemChanged(position);
        }
    }

    public void changeRating(ChatRatingViewModel model) {
        int position = list.indexOf(model);
        ((ChatRatingViewModel) list.get(position)).setRatingStatus(model.getRatingStatus());
        notifyItemChanged(position);
    }


}
