package com.tokopedia.inbox.inboxchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.model.LoadingModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.presenter.InboxChatPresenter;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatListViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.STATE_CHAT_UNREAD;

/**
 * Created by stevenfredian on 10/25/17.
 */

public class NewInboxChatAdapter extends RecyclerView.Adapter<AbstractViewHolder>{

    private final InboxChatTypeFactory typeFactory;
    private List<Visitable> list;
    private List<ChatListViewModel> listMove;
    private EmptyModel emptyModel;
    private LoadingModel loadingModel;
    private int selected = 0;
    private InboxChatPresenter presenter;

    public NewInboxChatAdapter(InboxChatTypeFactory typeFactory, InboxChatPresenter presenter) {
        this.typeFactory = typeFactory;
        this.list = new ArrayList<>();
        this.emptyModel = new EmptyModel();
        this.loadingModel = new LoadingModel();
        this.presenter = presenter;
        this.listMove = new ArrayList<>();
    }
//
//    @Override
//    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Context context = parent.getContext();
//        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
//        return typeFactory.createViewHolder(view, viewType);
//    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(list.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<Visitable> getList() {
        return list;
    }

    public List<ChatListViewModel> getListMove() {
        return listMove;
    }

    public void addChecked(int position) {
        ChatListViewModel item = (ChatListViewModel) list.get(position);
        item.setChecked(true);
        listMove.add(item);
        notifyItemChanged(position);
    }

    public void removeChecked(int position) {
        ChatListViewModel item = (ChatListViewModel) list.get(position);
        item.setChecked(false);
        listMove.remove(item);
        notifyItemChanged(position);
    }

    public void removeAllChecked() {
//        for (ChatListViewModel moveItem : listMove) {
//            for (ChatListViewModel inboxMessageItem : list) {
//                if (moveItem.getId() == inboxMessageItem.getId()) {
//                    list.remove(inboxMessageItem);
//                    break;
//                }
//            }
//        }
//
//        listMove.clear();
//        notifyDataSetChanged();
    }

    public void setList(List<ChatListViewModel> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addList(ArrayList<ChatListViewModel> list) {
        int index = this.list.size();
        this.list.addAll(list);
        notifyItemRangeInserted(index, list.size());
    }

    public void showLoading(){
        this.list.add(loadingModel);
        notifyItemInserted(list.size()-1);
    }

    public void removeLoading(){
        this.list.remove(loadingModel);
        notifyItemRemoved(list.size()-1);
    }

    public void showEmptyFull(boolean b) {

    }

    public void showLoadingFull(boolean b) {

    }

    public boolean checkLoadMore(int index) {
        if(index>=0) {
            return (list.get(index) instanceof LoadingModel);
        }
        return false;
    }

    public void moveToTop(String senderId, String lastReply, boolean showNotif) {
        String currentId;
        for (int i = 0; i < list.size(); i++) {
            ChatListViewModel temp = (ChatListViewModel) list.get(i);
            currentId = String.valueOf(temp.getId());
            if(currentId.equals(senderId)){
                if(showNotif){
                    int unread = temp.getUnreadCounter();
                    unread++;
                    temp.setMessage(lastReply);
                    temp.setUnreadCounter(unread);
                    temp.setReadStatus(STATE_CHAT_UNREAD);
                }
                list.remove(i);
                notifyItemRemoved(i);
                list.add(0, temp);
                notifyItemInserted(0);
                notifyItemRangeChanged(0, i);
                presenter.moveViewToTop();
                break;
            }
        }
    }

    public void clearSelection() {

    }
}
