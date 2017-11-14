package com.tokopedia.inbox.inboxchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.model.LoadingModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.ChatTimeConverter;
import com.tokopedia.inbox.inboxchat.viewmodel.DeleteChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.EmptyChatModel;
import com.tokopedia.inbox.inboxchat.presenter.InboxChatPresenter;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.TimeMachineListViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.STATE_CHAT_READ;
import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.STATE_CHAT_UNREAD;

/**
 * Created by stevenfredian on 10/25/17.
 */

public class NewInboxChatAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private static final int MAX_MESSAGE_DELETE = 3;
    private final InboxChatTypeFactory typeFactory;
    private List<Visitable> list;
    private List<Visitable> listSearch;
    private List<Pair> listMove;
    private EmptyModel emptyModel;
    private LoadingModel loadingModel;
    private int selected = 0;
    private InboxChatPresenter presenter;
    private TimeMachineListViewModel timeMachineChatModel;
    private EmptyChatModel emptyChatModel;
    private EmptyChatModel emptySearchModel;

    public NewInboxChatAdapter(InboxChatTypeFactory typeFactory, InboxChatPresenter presenter) {
        this.typeFactory = typeFactory;
        this.list = new ArrayList<>();
        this.listSearch = new ArrayList<>();
        this.emptyModel = new EmptyModel();
        this.emptyChatModel = new EmptyChatModel();
        this.emptySearchModel = new EmptyChatModel(EmptyChatModel.SEARCH);
        this.loadingModel = new LoadingModel();
        this.presenter = presenter;
        this.listMove = new ArrayList<>();
        this.timeMachineChatModel = new TimeMachineListViewModel("");
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        if (list.get(position) instanceof ChatListViewModel) {
            showTitle(holder.itemView.getContext(), holder.getAdapterPosition());
        }
        holder.bind(list.get(holder.getAdapterPosition()));
    }

    private void showTitle(Context context, int position) {
        ChatListViewModel now = (ChatListViewModel) list.get(position);
        if (position > 0) {

            ChatListViewModel prev = (ChatListViewModel) list.get(position - 1);

            if (now.getSectionSize() > 0 && !compareType(now.getSpanMode(), prev.getSpanMode())) {
                now.setHaveTitle(true);
            }

        } else {
            if (now.getSectionSize() > 0) {
                now.setHaveTitle(true);
            }
        }
    }

    private boolean compareType(int recentMode, int prevMode) {
        return recentMode == prevMode;
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

    public List<Pair> getListMove() {
        return listMove;
    }

    public void addChecked(int position) {
        if (listMove.size() + 1 <= MAX_MESSAGE_DELETE) {
            ChatListViewModel item = (ChatListViewModel) list.get(position);
            item.setChecked(true);
            Pair<ChatListViewModel, Integer> pair = new Pair<>(item, position);
            listMove.add(pair);
            notifyItemChanged(position);
        } else {
            removeChecked(position);
            presenter.getView().showErrorWarningDelete(MAX_MESSAGE_DELETE);
        }
    }

    public void removeChecked(int position) {
        ChatListViewModel item = (ChatListViewModel) list.get(position);
        item.setChecked(false);
        for (Pair pair : listMove) {
            if (position == (int) pair.second) {
                listMove.remove(pair);
                break;
            }
        }
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

    public void setList(List<Visitable> newList) {
        if (newList != null) {
            List<Visitable> temp = newList;
            int size = this.list.size();
            this.list.clear();
            notifyItemRangeRemoved(0, size);
            this.list.addAll(temp);
            notifyItemRangeInserted(0, temp.size());
        }
    }

    public void addList(ArrayList<Visitable> newList) {
        if (newList != null) {
            int index = this.list.size();
            this.list.addAll(newList);
            notifyItemRangeInserted(index, newList.size());
        }
    }


    public void addList(int index, ArrayList<Visitable> newList) {
        if (newList != null) {
            this.list.addAll(index, newList);
            notifyItemRangeInserted(index, newList.size());
        }
    }

    public void showLoading() {
        this.list.add(loadingModel);
        notifyItemInserted(list.size());
    }

    public void removeLoading() {
        this.list.remove(loadingModel);
        notifyItemRemoved(list.size());
    }

    public void showEmptyFull(boolean b) {
        if (b) {
            this.list.add(emptyChatModel);
            notifyItemInserted(0);
        } else {
            if (list.contains(emptyChatModel)) {
                this.list.remove(emptyChatModel);
                notifyItemRemoved(0);
            }
        }
    }

    public void showEmptySearch(boolean b) {
        if (b) {
            this.list.add(emptySearchModel);
            notifyItemInserted(0);
        } else {
            if (list.contains(emptySearchModel)) {
                this.list.remove(emptySearchModel);
                notifyItemRemoved(0);
            }
        }
    }

    public void showLoadingFull(boolean b) {

    }

    public boolean checkLoadMore(int index) {
        if (index >= 0) {
            return (list.get(index) instanceof LoadingModel);
        }
        return false;
    }

    public void moveToTop(String messageId, String lastReply, boolean showNotif) {
        String currentId;
        for (int i = 0; i < list.size(); i++) {
            try {
                ChatListViewModel temp = (ChatListViewModel) list.get(i);
                currentId = String.valueOf(temp.getId());
                if (currentId.equals(messageId)) {
                    if (showNotif) {
                        int unread = temp.getUnreadCounter();
                        unread++;
                        temp.setMessage(lastReply);
                        temp.setUnreadCounter(unread);
                        temp.setReadStatus(STATE_CHAT_UNREAD);
                        temp.setTime(String.valueOf(new Date().getTime()));
                        temp.setTyping(false);
                    } else {
                        temp.setMessage(lastReply);
                        temp.setUnreadCounter(0);
                        temp.setReadStatus(STATE_CHAT_READ);
                        temp.setTime(String.valueOf(new Date().getTime()));
                        temp.setTyping(false);
                    }
                    list.remove(i);
                    notifyItemRemoved(i);
                    list.add(0, temp);
                    notifyItemInserted(0);
                    notifyItemRangeChanged(0, i);
                    presenter.moveViewToTop();
                    break;
                }

            } catch (ClassCastException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void clearSelection() {
        for (Visitable message : list) {
            if (message instanceof ChatListViewModel)
                ((ChatListViewModel) message).setChecked(false);
        }
        listMove.clear();
        notifyDataSetChanged();
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void showTimeMachine() {
        if (list.size() == 1
                && list.get(0) instanceof EmptyChatModel) {
            ((EmptyChatModel) this.list.get(0)).setHasTimeMachine(true);
        } else if (list.size() > 0) {
            this.list.add(timeMachineChatModel);
            notifyItemInserted(list.size() - 1);
        }
    }

    public void removeList(List<Pair> originList, List<DeleteChatViewModel> list) {
        for (Pair pair: originList) {
            ChatListViewModel first = (ChatListViewModel) pair.first;
            int position = (int) pair.second;
            for (DeleteChatViewModel model : list) {
                if (model.getMsgId() == Integer.valueOf(first.getId())) {
                    this.list.remove(position);
                    notifyItemRemoved(position);
                    break;
                }
            }
        }
    }


    public void showTyping(int msgId) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof ChatListViewModel) {
                ChatListViewModel tempModel = ((ChatListViewModel) list.get(i));
                String temp = tempModel.getId();
                if (msgId == Integer.valueOf(temp)) {
                    tempModel.setTyping(true);
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    public void removeTyping(int msgId) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof ChatListViewModel) {
                ChatListViewModel tempModel = ((ChatListViewModel) list.get(i));
                String temp = tempModel.getId();
                if (msgId == Integer.valueOf(temp)) {
                    tempModel.setTyping(false);
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }
}
