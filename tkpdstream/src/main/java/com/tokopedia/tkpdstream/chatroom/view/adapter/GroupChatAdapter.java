package com.tokopedia.tkpdstream.chatroom.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdstream.chatroom.view.adapter.typefactory.GroupChatTypeFactory;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.BaseChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.PendingChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.UserActionViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author by nisie on 2/7/18.
 */

public class GroupChatAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> list;
    private List<Visitable> listDummy;
    private GroupChatTypeFactory typeFactory;
    private EmptyModel emptyModel;
    private LoadingModel loadingModel;
    private boolean canLoadMore;

    public GroupChatAdapter(GroupChatTypeFactory typeFactory) {
        this.list = new ArrayList<>();
        this.listDummy = new ArrayList<>();
        this.typeFactory = typeFactory;
        this.emptyModel = new EmptyModel();
        this.loadingModel = new LoadingModel();
        this.canLoadMore = false;
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {

        if (list.get(position) instanceof BaseChatViewModel
                && position == list.size() - 1) {
            ((BaseChatViewModel) list.get(position)).setShowHeaderTime(true);
            ((BaseChatViewModel) list.get(position)).setHeaderTime(
                    ((BaseChatViewModel) list.get(position)).getUpdatedAt());
        } else if (list.get(position) instanceof BaseChatViewModel
                && headerTimeIsDifferent(
                ((BaseChatViewModel) list.get(position)).getUpdatedAt(),
                ((BaseChatViewModel) list.get(position + 1)).getUpdatedAt())) {
            ((BaseChatViewModel) list.get(position)).setShowHeaderTime(true);
            ((BaseChatViewModel) list.get(position)).setHeaderTime(
                    ((BaseChatViewModel) list.get(position)).getUpdatedAt());
        } else if (list.get(position) instanceof BaseChatViewModel) {
            ((BaseChatViewModel) list.get(position)).setShowHeaderTime(false);
        }

        holder.bind(list.get(position));

    }

    private boolean headerTimeIsDifferent(long headerTimeNow, long headerTimeAbovePost) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(headerTimeNow);
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTimeInMillis(headerTimeAbovePost);
        return newCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR) != 0;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static GroupChatAdapter createInstance(GroupChatTypeFactory groupChatTypeFactory) {
        return new GroupChatAdapter(groupChatTypeFactory);
    }

    public void addList(List<Visitable> listChat) {
        int positionStart = this.list.size();
        this.list.addAll(listChat);
        notifyItemRangeInserted(positionStart, listChat.size());

        if (positionStart != 0) {
            notifyItemChanged(positionStart - 1);
        }
    }

    public void addReply(ChatViewModel chatItem) {
        this.list.add(0, chatItem);
    }

    public void addDummyReply(PendingChatViewModel pendingChatViewModel) {
        list.add(0, pendingChatViewModel);
    }

    public void removeDummy(PendingChatViewModel pendingChatViewModel) {
        list.remove(pendingChatViewModel);
    }

    public void setRetry(PendingChatViewModel pendingChatViewModel) {
        if (list.get(list.indexOf(pendingChatViewModel)) instanceof PendingChatViewModel) {
            ((PendingChatViewModel) list.get(list.indexOf(pendingChatViewModel))).setRetry(true);
        }
        notifyItemChanged(list.indexOf(pendingChatViewModel));
    }

    public void addIncomingMessage(Visitable messageItem) {
        this.list.add(0, messageItem);

    }

    public void addAction(UserActionViewModel userActionViewModel) {
        this.list.add(0, userActionViewModel);
    }

    public void showLoading() {
        if (!this.list.contains(loadingModel)) {
            this.list.add(list.size(), loadingModel);
            notifyItemInserted(list.size());
        }
    }

    public void dismissLoading() {
        this.list.remove(loadingModel);
        notifyItemRemoved(list.size());
    }

    public boolean isLoading() {
        return this.list.contains(loadingModel);
    }
}
