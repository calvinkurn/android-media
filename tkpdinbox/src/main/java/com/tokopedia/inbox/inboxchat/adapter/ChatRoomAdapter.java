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
import com.tokopedia.inbox.inboxchat.ChatTimeConverter;
import com.tokopedia.inbox.inboxchat.domain.model.ListReplyViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ListReply;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class ChatRoomAdapter extends RecyclerView.Adapter<AbstractViewHolder>{

    private final ChatRoomTypeFactory typeFactory;
    private List<Visitable> list;
    private EmptyModel emptyModel;
    private LoadingModel loadingModel;

    public ChatRoomAdapter(ChatRoomTypeFactory typeFactory){
        this.list = new ArrayList<>();
        this.typeFactory = typeFactory;
        this.emptyModel = new EmptyModel();
        this.loadingModel = new LoadingModel();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        if(list.get(position) instanceof ListReplyViewModel){
            showTime(position);
        }
        holder.bind(list.get(position));
    }

    private void showTime(int position) {
        if (position != 0) {
            try {
                ListReplyViewModel now = (ListReplyViewModel) list.get(position);
                ListReplyViewModel prev = (ListReplyViewModel) list.get(position - 1);
                long myTime = Long.parseLong(now.getReplyTime());
                long prevTime = Long.parseLong(prev.getReplyTime());

                Calendar time1 = ChatTimeConverter.unixToCalendar(myTime);
                Calendar calBefore = ChatTimeConverter.unixToCalendar(prevTime);
                if (compareTime(time1, calBefore)) {
                    ((ListReplyViewModel) list.get(position)).setShowTime(false);
                }
            }catch (NumberFormatException e){
                ((ListReplyViewModel) list.get(position)).setShowTime(true);
            }catch (ClassCastException e){
                e.printStackTrace();
            }
        }else {
            try {
                ((ListReplyViewModel) list.get(position)).setShowTime(true);
            }catch (ClassCastException e){
                e.printStackTrace();
            }
        }
    }

    private boolean compareTime(Calendar calCurrent, Calendar calBefore) {
        return calCurrent.get(Calendar.DAY_OF_YEAR) == calBefore.get(Calendar.DAY_OF_YEAR)
                && calCurrent.get(Calendar.MONTH) == calBefore.get(Calendar.MONTH)
                && calCurrent.get(Calendar.YEAR) == calBefore.get(Calendar.YEAR);
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
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<Visitable> list) {
        this.list.addAll(0, list);
        notifyItemRangeInserted(0, list.size());
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
        list.remove(list.size() - 1);
        notifyItemRemoved(list.size());
    }

    public void addReply(Visitable item) {
        this.list.add(item);
        notifyItemInserted(this.list.size()-1);
    }

    public void showLoading(){
        this.list.add(0, loadingModel);
        notifyItemInserted(0);
    }

    public void removeLoading(){
        this.list.remove(loadingModel);
        notifyItemRemoved(0);
    }


    public boolean checkLoadMore(int index) {
        return (list.get(index) instanceof LoadingModel);
    }
}
