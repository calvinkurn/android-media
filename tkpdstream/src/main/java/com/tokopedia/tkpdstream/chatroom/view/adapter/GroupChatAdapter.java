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
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.PendingChatViewModel;

import java.util.ArrayList;
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
        holder.bind(list.get(position));

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
        this.list.addAll(listChat);
        notifyDataSetChanged();
    }

    public void addReply(ChatViewModel chatItem) {
        this.list.add(chatItem);
    }

    public void addDummyReply(PendingChatViewModel pendingChatViewModel) {
        list.add(pendingChatViewModel);
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
}
