package com.tokopedia.inbox.inboxchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.model.LoadingModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.ChatTimeConverter;
import com.tokopedia.inbox.inboxchat.domain.model.ListReplyViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.ReplyParcelableModel;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.WebSocketResponse;
import com.tokopedia.inbox.inboxchat.viewholder.MyChatViewHolder;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.TypingChatModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.TimeMachineChatModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class ChatRoomAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private final ChatRoomTypeFactory typeFactory;
    private List<Visitable> list;
    private EmptyModel emptyModel;
    private LoadingModel loadingModel;
    private TimeMachineChatModel timeMachineChatModel;
    private TypingChatModel typingModel;
    private boolean isTyping;

    public ChatRoomAdapter(ChatRoomTypeFactory typeFactory) {
        this.list = new ArrayList<>();
        this.typeFactory = typeFactory;
        this.emptyModel = new EmptyModel();
        this.loadingModel = new LoadingModel();
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
        if (list.get(position) instanceof ListReplyViewModel) {
            showTime(holder.itemView.getContext(), holder.getAdapterPosition());
            showHour(holder.itemView.getContext(), holder.getAdapterPosition());
        }
        holder.bind(list.get(holder.getAdapterPosition()));
    }

    @Override
    public void onViewRecycled(AbstractViewHolder holder) {
        super.onViewRecycled(holder);
        if(holder instanceof MyChatViewHolder) {
            ((MyChatViewHolder) holder).onViewRecycled();
        }
    }

    private void showTime(Context context, int position) {
        if (position != 0) {
            try {
                ListReplyViewModel now = (ListReplyViewModel) list.get(position);
                ListReplyViewModel prev = (ListReplyViewModel) list.get(position - 1);
                long myTime = Long.parseLong(now.getReplyTime());
                long prevTime = Long.parseLong(prev.getReplyTime());

                Calendar time1 = ChatTimeConverter.unixToCalendar(myTime);
                Calendar calBefore = ChatTimeConverter.unixToCalendar(prevTime);
                if (compareTime(context, myTime, prevTime)) {
                    ((ListReplyViewModel) list.get(position)).setShowTime(false);
                } else {
                    ((ListReplyViewModel) list.get(position)).setShowTime(true);
                }
            } catch (NumberFormatException | ClassCastException e) {
                ((ListReplyViewModel) list.get(position)).setShowTime(true);
            }
        } else {
            try {
                ((ListReplyViewModel) list.get(position)).setShowTime(true);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    private void showHour(Context context, int position) {
//        if (position >= 0) {
//            try {
//                ListReplyViewModel now = (ListReplyViewModel) list.get(position);
//                ListReplyViewModel prev = (ListReplyViewModel) list.get(position - 1);
//                ListReplyViewModel next = (ListReplyViewModel) list.get(position + 1);
//                String myTime = ChatTimeConverter.formatTime(Long.parseLong(now.getReplyTime()));
//                String prevTime = ChatTimeConverter.formatTime(Long.parseLong(prev.getReplyTime()));
//                String nextTime = ChatTimeConverter.formatTime(Long.parseLong(next.getReplyTime()));
//
//
//                if(now.isOpposite() ^ prev.isOpposite() || now.isOpposite() ^ next.isOpposite()){
//                    ((ListReplyViewModel) list.get(position)).setShowHour(true);
//                }else {
//                    if (compareHour(context, myTime, nextTime)) {
//                        ((ListReplyViewModel) list.get(position)).setShowHour(false);
//                    }else {
//                        ((ListReplyViewModel) list.get(position)).setShowHour(true);
//                    }
//                }
//
//            } catch (NumberFormatException | ClassCastException | IndexOutOfBoundsException e) {
//                ((ListReplyViewModel) list.get(position)).setShowHour(true);
//            }
//        } else {
//            try {
//                ((ListReplyViewModel) list.get(position)).setShowHour(true);
//            } catch (ClassCastException e) {
//                e.printStackTrace();
//            }
//        }
        ((ListReplyViewModel) list.get(position)).setShowHour(true);
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

    public void addList(List<Visitable> list) {
        this.list.addAll(0, list);
        notifyItemRangeInserted(0, list.size());
        notifyItemRangeChanged(0, list.size() + 1);
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

    public void removeLast() {
        if (list != null && !list.isEmpty()) {
            list.remove(list.size() - 1);
            notifyItemRemoved(list.size());
        }
    }

    public void remove(MyChatViewModel model) {
        int position = list.indexOf(model);
        list.remove(model);
        notifyItemRemoved(position);
    }

    public void addReply(Visitable item) {
        this.list.add(item);
        notifyItemInserted(this.list.size() - 1);
        notifyItemRangeChanged(list.size() - 2, 2);
    }

    public void showLoading() {
        this.list.add(0, loadingModel);
        notifyItemInserted(0);
    }

    public void removeLoading() {
        this.list.remove(loadingModel);
        notifyItemRemoved(0);
    }

    public void showTyping() {
        this.isTyping = true;
        this.list.add(typingModel);
        notifyItemInserted(list.size() - 1);
    }

    public void removeTyping() {
        this.isTyping = false;
        this.list.remove(typingModel);
        notifyItemRemoved(list.size());
    }

    public boolean checkLoadMore(int index) {
        if (index >= 0) {
            return (list.get(index) instanceof LoadingModel);
        }
        return false;
    }


    public ReplyParcelableModel getLastItem() {
        ListReplyViewModel item;
        if (list.size() > 0 && list.get(list.size() - 1) instanceof ListReplyViewModel) {
            item = (ListReplyViewModel) list.get(list.size() - 1);
            return new ReplyParcelableModel(String.valueOf(item.getMsgId()), item.getMsg(), item
                    .getReplyTime
                            ());
        } else if (list.size() > 1 && list.get(list.size() - 2) instanceof ListReplyViewModel) {
            item = (ListReplyViewModel) list.get(list.size() - 2);
            return new ReplyParcelableModel(String.valueOf(item.getMsgId()), item.getMsg(), item
                    .getReplyTime());
        } else {
            return null;
        }
    }

    public void showTimeMachine() {
        this.list.add(0, timeMachineChatModel);
        notifyItemInserted(0);
    }

    public void setReadStatus(WebSocketResponse response) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i) instanceof MyChatViewModel) {
                if (((MyChatViewModel) list.get(i)).isReadStatus()) {
                    break;
                } else {
                    ((MyChatViewModel) list.get(i)).setReadStatus(true);
                    notifyItemRangeChanged(i, 1);
                }
            }
        }
    }

    public boolean isTyping() {
        return isTyping;
    }

    public void showRetryFor(MyChatViewModel model, boolean b) {
        int position = list.indexOf(model);
        ((MyChatViewModel) list.get(position)).setRetry(true);
        notifyItemChanged(position);
    }
}
